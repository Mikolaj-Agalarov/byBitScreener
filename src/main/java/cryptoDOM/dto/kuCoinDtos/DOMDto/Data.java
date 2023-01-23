package cryptoDOM.dto.kuCoinDtos.DOMDto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Data {
    private String time;
    private String sequence;
    private Float[][] bids;
    private Float[][] asks;
}
