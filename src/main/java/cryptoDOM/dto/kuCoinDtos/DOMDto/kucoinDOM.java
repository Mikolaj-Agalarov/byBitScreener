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
public class kucoinDOM {
    private String tickerName;
    private List<List<Float>> asks;
    private List<List<Float>> bids;
    private Float currentPriceOfBtc;
    private Float currentPriceOfEth;
    private Float currentPriceOfKsc;
}
