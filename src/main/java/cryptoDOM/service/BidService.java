package cryptoDOM.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import cryptoDOM.entity.Ask;
import cryptoDOM.entity.Bid;
import cryptoDOM.entity.DOM;
import cryptoDOM.entity.TickerName;
import cryptoDOM.repository.AskRepository;
import cryptoDOM.repository.BidRepository;
import cryptoDOM.repository.TickerNameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BidService {
    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private TickerNameRepository tickerNameRepository;

    public void processBids(JsonArray asks, TickerName tickerName, DOM dom) {
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
                BigDecimal btcPrice = new BigDecimal(tickerNameRepository.findBy("BTC-USDT").getDom().getHighest_bid_price().toString());

                if (amount.multiply(price).multiply(btcPrice)
                        .compareTo(tickerName.getMinOrderValue().divide(btcPrice)) > 0
                        && range.compareTo(BigDecimal.valueOf(10)) < 0) {
                    Bid bid = new Bid();
                    bid.setPrice(price);
                    bid.setAmount(amount);
                    bid.setDom(dom);
                    bid.setRange(range);

                    bidRepository.save(bid);
                }
            } else if (amount.multiply(price).compareTo(tickerName.getMinOrderValue()) > 0 &&
                    range.compareTo(BigDecimal.valueOf(10)) < 0) {
                Bid bid = new Bid();
                bid.setPrice(price);
                bid.setAmount(amount);
                bid.setDom(dom);
                bid.setRange(range);

                bidRepository.save(bid);
            }
        }
    }

    public List<Bid> getAllBids() {
        return bidRepository.findAll();
    }
}

