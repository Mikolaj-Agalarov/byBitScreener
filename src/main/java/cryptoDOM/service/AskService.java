package cryptoDOM.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import cryptoDOM.entity.Ask;
import cryptoDOM.entity.DOM;
import cryptoDOM.entity.Notification;
import cryptoDOM.entity.TickerName;
import cryptoDOM.repository.AskRepository;
import cryptoDOM.repository.DOMRepository;
import cryptoDOM.repository.TickerNameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class AskService {
    @Autowired
    private AskRepository askRepository;
    @Autowired
    private NotificationService notificationService;

    public void processAsks(JsonArray asks, TickerName tickerName, DOM dom) {
        List<Ask> existingAsks = askRepository.findByDom(dom);

        Map<BigDecimal, Ask> existingAskMap = existingAsks.stream()
                .collect(Collectors.toMap(Ask::getPrice, Function.identity()));

        for (JsonElement askJson : asks.getAsJsonArray()) {
            JsonArray askData = askJson.getAsJsonArray();
            BigDecimal price = new BigDecimal(askData.get(0).getAsString());
            BigDecimal amount = new BigDecimal(askData.get(1).getAsString());
            BigDecimal range = price.subtract(dom.getLowest_ask_price())
                    .divide(price, 4, RoundingMode.HALF_EVEN)
                    .multiply(BigDecimal.valueOf(100))
                    .abs();


            if (tickerName.getTickerName().contains("-BTC")) {
                //TODO fix this
                //BigDecimal btcPrice = new BigDecimal(tickerNameRepository.findBy("BTC-USDT").getDom().getHighest_bid_price().toString());

                BigDecimal btcPrice = BigDecimal.ZERO;
                if (amount.multiply(price).multiply(btcPrice)
                        .compareTo(tickerName.getMinOrderValue().divide(btcPrice)) > 0
                        && range.compareTo(BigDecimal.valueOf(10)) < 0) {

                    // Update the existing ask if present, else create a new one
                    if (existingAskMap.containsKey(price)) {
                        Ask ask = existingAskMap.get(price);
                        ask.setAmount(amount);
                        ask.setRange(range);
                        askRepository.save(ask);
                        System.out.println(tickerName.getTickerName() + " Ask updated");
                    } else {
                        Ask ask = new Ask();
                        ask.setPrice(price);
                        ask.setAmount(amount);
                        ask.setDom(dom);
                        ask.setRange(range);
                        askRepository.save(ask);
                        System.out.println(tickerName.getTickerName() + " Ask added");
                    }
                }
            } else if (amount.multiply(price).compareTo(tickerName.getMinOrderValue()) > 0 &&
                    range.compareTo(BigDecimal.valueOf(10)) < 0) {

                notificationService.createNotificationIfNeeded(tickerName, price, amount, dom);

                // Update the existing ask if present, else create a new one
                if (existingAskMap.containsKey(price)) {
                    Ask ask = existingAskMap.get(price);
                    ask.setAmount(amount);
                    ask.setRange(range);
                    askRepository.save(ask);
                    System.out.println(tickerName.getTickerName() + " Ask updated");
                } else {
                    Ask ask = new Ask();
                    ask.setPrice(price);
                    ask.setAmount(amount);
                    ask.setDom(dom);
                    ask.setRange(range);
                    askRepository.save(ask);
                    System.out.println(tickerName.getTickerName() + " Ask added");
                }
            }
        }

        // Delete the asks that are no longer present in the API response
        existingAsks.stream()
                .filter(existingAsk -> !existingAskMap.containsKey(existingAsk.getPrice()))
                .forEach(existingAsk -> {
                    askRepository.delete(existingAsk);
                    System.out.println(tickerName.getTickerName() + " Ask deleted");
                });
    }


    public List<Ask> getAllAsks() {
        return askRepository.findAll();
    }
//
//    public List<Ask> getTopThreeAsksByDom(DOM dom) {
//        List<Ask> topThreeAsksByDom = new ArrayList<>();
//
//
//        List<Ask> topThreeAsks = askRepository.findTop3ByDomOrderByAmountDesc(dom);
//        topThreeAsksByDom.addAll(topThreeAsks);
//
//
//        return topThreeAsksByDom;
//    }


}

