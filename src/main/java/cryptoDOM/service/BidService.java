package cryptoDOM.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import cryptoDOM.entity.Bid;
import cryptoDOM.entity.DOM;
import cryptoDOM.entity.TickerName;
import cryptoDOM.repository.BidRepository;
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
public class BidService {
    @Autowired
    private BidRepository bidRepository;
    @Autowired
    private NotificationService notificationService;


    public void processBids(JsonArray bids, TickerName tickerName, DOM dom) {
//        BigDecimal btcPrice = BigDecimal.ZERO;

        List<Bid> existingBids = bidRepository.findByDomId(dom.getId());

        Map<BigDecimal, Bid> existingBidMap = existingBids.stream()
                .collect(Collectors.toMap(Bid::getPrice, Function.identity()));

        List<BigDecimal> pricesArray = new ArrayList<>();

        for (JsonElement bidJson : bids.getAsJsonArray()) {
            JsonArray bidData = bidJson.getAsJsonArray();
            BigDecimal price = new BigDecimal(bidData.get(0).getAsString());
            BigDecimal amount = new BigDecimal(bidData.get(1).getAsString());
            BigDecimal range = price.subtract(dom.getLowest_ask_price())
                    .divide(price, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .abs()
                    .setScale(2, RoundingMode.HALF_UP);

//            if (tickerName.getTickerName().contains("-BTC")) {
//
//
//                if (amount.multiply(price).multiply(btcPrice)
//                        .compareTo(tickerName.getMinOrderValue().divide(btcPrice)) > 0
//                        && range.compareTo(BigDecimal.valueOf(10)) < 0) {
//
//                    // Update the existing ask if present, else create a new one
//                    if (existingBidMap.containsKey(price)) {
//                        Bid bid = existingBidMap.get(price);
//                        bid.setAmount(amount);
//                        bid.setRange(range);
//                        bid.setPercentageOfDailyVolume(amount.divide(tickerName.getVol(), 2, RoundingMode.HALF_UP));
//
//                        bidRepository.save(bid);
//                        System.out.println(tickerName.getTickerName() + " Bid updated");
//                    } else {
//                        Bid bid = new Bid();
//                        bid.setPrice(price);
//                        bid.setAmount(amount);
//                        bid.setDom(dom);
//                        bid.setRange(range);
//                        bid.setPercentageOfDailyVolume(amount.divide(tickerName.getVol(), 2, RoundingMode.HALF_UP));
//
//                        bidRepository.save(bid);
//                        System.out.println(tickerName.getTickerName() + " Bid added");
//                    }
//                }
//            } else
                if (amount.multiply(price).compareTo(tickerName.getMinOrderValue()) > 0 &&
                    range.compareTo(BigDecimal.valueOf(10)) < 0) {

                    pricesArray.add(price);
                    BigDecimal volumeOfBidInUsd = amount.multiply(price).setScale(0, RoundingMode.DOWN);


                    if (amount.multiply(price).compareTo(tickerName.getMinOrderValue().multiply(BigDecimal.valueOf(1.5))) > 0) {
                    notificationService.createNotificationIfNeeded(tickerName, price, amount, dom);
                }

                if (existingBidMap.containsKey(price)) {
//                    bidRepository.delete(existingBidMap.get(price));
                    Bid bid = existingBidMap.get(price);
                    bid.setAmount(amount);
                    bid.setRange(range);
                    bid.setPercentageOfDailyVolume(amount.divide(tickerName.getVol(), 2, RoundingMode.HALF_UP));
                    bid.setVolumeInUsd(volumeOfBidInUsd);

                    bidRepository.save(bid);
                    System.out.println(tickerName.getTickerName() + " Bid updated");
                } else {
                    Bid bid = new Bid();
                    bid.setPrice(price);
                    bid.setAmount(amount);
                    bid.setDom(dom);
                    bid.setRange(range);
                    bid.setPercentageOfDailyVolume(amount.divide(tickerName.getVol(), 2, RoundingMode.HALF_UP));
                    bid.setVolumeInUsd(volumeOfBidInUsd);

                    bidRepository.save(bid);
                    System.out.println(tickerName.getTickerName() + " Bid added");
                }
            }
        }

        existingBids.stream()
                .filter(existingBid -> !pricesArray.contains(existingBid.getPrice()))
                .forEach(existingBid -> bidRepository.delete(existingBid));
    }

    public List<Bid> getAllBids() {
        return bidRepository.findAll();
    }

    public List<Bid> findInDescOrder() {
        return bidRepository.findAllByOrderByVolumeInUsdDesc();
    }
}

