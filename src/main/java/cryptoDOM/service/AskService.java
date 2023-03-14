package cryptoDOM.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import cryptoDOM.entity.Ask;
import cryptoDOM.entity.DOM;
import cryptoDOM.entity.TickerName;
import cryptoDOM.repository.AskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@Transactional
public class AskService {
    @Autowired
    private AskRepository askRepository;
    @Autowired
    private NotificationService notificationService;

    public void updateAsks(JsonArray asks, TickerName tickerName, DOM dom) {
//        BigDecimal btcPrice = BigDecimal.ZERO;

        List<Ask> existingAsks = askRepository.findByDomId(dom.getId());

        Map<BigDecimal, Ask> existingAskMap = existingAsks.stream()
                .collect(Collectors.toMap(Ask::getPrice, Function.identity()));

        List<BigDecimal> pricesArray = new ArrayList<>();

        for (JsonElement askJson : asks.getAsJsonArray()) {
            JsonArray askData = askJson.getAsJsonArray();
            BigDecimal price = new BigDecimal(askData.get(0).getAsString());
            BigDecimal amount = new BigDecimal(askData.get(1).getAsString());
            BigDecimal range = price.subtract(dom.getLowest_ask_price())
                    .divide(price, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .abs()
                    .setScale(2, RoundingMode.HALF_UP);


//            if (tickerName.getTickerName().contains("-BTC")) {
//                if (amount.multiply(price)
//                        .compareTo(tickerName.getMinOrderValue().divide(btcPrice)) > 0
//                        && range.compareTo(BigDecimal.valueOf(10)) < 0) {
//
//                    // Update the existing ask if present, else create a new one
//                    if (existingAskMap.containsKey(price)) {
//                        Ask ask = existingAskMap.get(price);
//                        ask.setAmount(amount);
//                        ask.setRange(range);
//                        ask.setPercentageOfDailyVolume(amount.divide(tickerName.getVol(), 2, RoundingMode.HALF_UP));
//
//                        askRepository.save(ask);
//                        System.out.println(tickerName.getTickerName() + " Ask updated");
//                    } else {
//                        Ask ask = new Ask();
//                        ask.setPrice(price);
//                        ask.setAmount(amount);
//                        ask.setDom(dom);
//                        ask.setRange(range);
//                        ask.setPercentageOfDailyVolume(amount.divide(tickerName.getVol(), 2, RoundingMode.HALF_UP));
//
//                        askRepository.save(ask);
//                        System.out.println(tickerName.getTickerName() + " Ask added");
//                    }
//                }
//            } else
                if (amount.multiply(price).compareTo(tickerName.getMinOrderValue()) > 0 &&
                        range.compareTo(BigDecimal.valueOf(10)) < 0) {

                    pricesArray.add(price);

                    BigDecimal volumeOfAskInUsd = amount.multiply(price).setScale(0, RoundingMode.DOWN);


                    if (amount.multiply(price).compareTo(tickerName.getMinOrderValue().multiply(BigDecimal.valueOf(1.5))) > 0) {
                        notificationService.createNotificationIfNeeded(tickerName, price, amount, dom);
                    }

                    if (existingAskMap.containsKey(price)) {
//                        askRepository.deleteAsk(existingAskMap.get(price));
                        Ask ask = existingAskMap.get(price);
                        ask.setAmount(amount);
                        ask.setRange(range);
                        ask.setPercentageOfDailyVolume(amount.divide(tickerName.getVol(), 2, RoundingMode.HALF_UP));
                        ask.setVolumeInUsd(volumeOfAskInUsd);

                        askRepository.save(ask);
                        System.out.println(tickerName.getTickerName() + " Ask updated");
                    } else {
                        Ask ask = new Ask();
                        ask.setPrice(price);
                        ask.setAmount(amount);
                        ask.setDom(dom);
                        ask.setRange(range);
                        ask.setPercentageOfDailyVolume(amount.divide(tickerName.getVol(), 2, RoundingMode.HALF_UP));
                        ask.setVolumeInUsd(volumeOfAskInUsd);

                        askRepository.save(ask);
                        System.out.println(tickerName.getTickerName() + " Ask added");
                    }
                }
            }

        existingAsks.stream()
                .filter(existingAsk -> !pricesArray.contains(existingAsk.getPrice()))
                .forEach(existingAsk -> askRepository.delete(existingAsk));
        }



    public List<Ask> getAllAsks() {
        return askRepository.findAll();
    }

    public List<Ask> findInDescOrder() {
        return askRepository.findAllByOrderByVolumeInUsdDesc();
    }




}

