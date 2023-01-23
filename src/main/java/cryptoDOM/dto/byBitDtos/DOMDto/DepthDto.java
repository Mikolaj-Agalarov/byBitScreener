package cryptoDOM.dto.byBitDtos.DOMDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class DepthDto {
    private String tickerName;
    private Map<Float, Float> asks;
    private Map<Float, Float> bids;
    private Optional<Float> biggestAsk;
    private Optional<Float> biggestBid;
    private Float currentPrice;
    private Optional<Float> percentageFromBiggestBidToCurrentPrice;
    private Optional<Float> percentageFromBiggestAskToCurrentPrice;
}
