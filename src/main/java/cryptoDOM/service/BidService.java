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
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BidService {
    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private TickerNameRepository tickerNameRepository;

    public void processBids(JsonArray bids, TickerName tickerName, DOM dom) {
        List<Bid> existingBids = bidRepository.findByDom(dom);

        Map<BigDecimal, Bid> existingBidMap = existingBids.stream()
                .collect(Collectors.toMap(Bid::getPrice, Function.identity()));


        for (JsonElement bidJson : bids.getAsJsonArray()) {
            JsonArray bidData = bidJson.getAsJsonArray();
            BigDecimal price = new BigDecimal(bidData.get(0).getAsString());
            BigDecimal amount = new BigDecimal(bidData.get(1).getAsString());
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

                    // Update the existing ask if present, else create a new one
                    if (existingBidMap.containsKey(price)) {
                        Bid bid = existingBidMap.get(price);
                        bid.setAmount(amount);
                        bid.setRange(range);
                        bidRepository.save(bid);
                        System.out.println(tickerName.getTickerName() + " Bid updated");
                    } else {
                        Bid bid = new Bid();
                        bid.setPrice(price);
                        bid.setAmount(amount);
                        bid.setDom(dom);
                        bid.setRange(range);
                        bidRepository.save(bid);
                        System.out.println(tickerName.getTickerName() + " Bid added");
                    }
                }
            } else if (amount.multiply(price).compareTo(tickerName.getMinOrderValue()) > 0 &&
                    range.compareTo(BigDecimal.valueOf(10)) < 0) {

                // Update the existing ask if present, else create a new one
                if (existingBidMap.containsKey(price)) {
                    Bid bid = existingBidMap.get(price);
                    bid.setAmount(amount);
                    bid.setRange(range);
                    bidRepository.save(bid);
                    System.out.println(tickerName.getTickerName() + " Bid updated");
                } else {
                    Bid bid = new Bid();
                    bid.setPrice(price);
                    bid.setAmount(amount);
                    bid.setDom(dom);
                    bid.setRange(range);
                    bidRepository.save(bid);
                    System.out.println(tickerName.getTickerName() + " Bid added");
                }
            }
        }

        // Delete the asks that are no longer present in the API response
        existingBids.stream()
                .filter(existingBid -> !existingBidMap.containsKey(existingBid.getPrice()))
                .forEach(existingBid -> {
                    bidRepository.delete(existingBid);
                    System.out.println(tickerName.getTickerName() + " Bid deleted");
                });
    }

    public List<Bid> getAllBids() {
        return bidRepository.findAll();
    }
}

