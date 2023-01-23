package cryptoDOM.dto.kuCoinDtos.DOMDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class GlassInstance {
    private String tickerName;
    private List<List<Float>> asks;
    private List<List<Float>> bids;
    private Float priceOfBiggestAsk;
    private Float priceOfBiggestBid;
    private Float currentPriceOfBtc;
}
