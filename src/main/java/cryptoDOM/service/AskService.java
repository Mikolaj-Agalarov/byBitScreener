package cryptoDOM.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import cryptoDOM.entity.Ask;
import cryptoDOM.entity.DOM;
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



@Service
public class AskService {
    @Autowired
    private AskRepository askRepository;

    @Autowired
    private TickerNameRepository tickerNameRepository;

    @Autowired
    private DOMRepository domRepository;

    public void processAsks(JsonArray asks, TickerName tickerName, DOM dom) {
        for (JsonElement askJson : asks.getAsJsonArray()) {
            JsonArray askData = askJson.getAsJsonArray();
            BigDecimal price = new BigDecimal(askData.get(0).getAsString());
            BigDecimal amount = new BigDecimal(askData.get(1).getAsString());
            BigDecimal range = price.subtract(BigDecimal.valueOf(dom.getLowest_ask_price()))
                    .divide(price, 4, RoundingMode.HALF_EVEN)
                    .multiply(BigDecimal.valueOf(100))
                    .abs();

            // Check if the ask price is higher than the minimum order value for the ticker
            if (tickerName.getTickerName().contains("-BTC")) {
                //TODO fix this
                //BigDecimal btcPrice = new BigDecimal(tickerNameRepository.findBy("BTC-USDT").getDom().getHighest_bid_price().toString());

                BigDecimal btcPrice = BigDecimal.ZERO;
                if (amount.multiply(price).multiply(btcPrice)
                        .compareTo(tickerName.getMinOrderValue().divide(btcPrice)) > 0
                        && range.compareTo(BigDecimal.valueOf(10)) < 0) {
                    Ask ask = new Ask();
                    ask.setPrice(price);
                    ask.setAmount(amount);
                    ask.setDom(dom);
                    ask.setRange(range);

                    System.out.println(range);
                    System.out.println("------------------------");

                    askRepository.save(ask);
                }
            } else if (amount.multiply(price).compareTo(tickerName.getMinOrderValue()) > 0 &&
                    range.compareTo(BigDecimal.valueOf(10)) < 0) {

                Ask ask = new Ask();
                ask.setPrice(price);
                ask.setAmount(amount);
                ask.setDom(dom);
                ask.setRange(range);

                askRepository.save(ask);

                System.out.println(range);
                System.out.println("------------------------");
            }
        }
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

