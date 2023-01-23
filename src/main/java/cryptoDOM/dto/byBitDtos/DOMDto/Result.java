package cryptoDOM.dto.byBitDtos.DOMDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Result {
    private Float[][] bids;
    private Float[][] asks;
    private Long time;
}
