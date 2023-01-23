package cryptoDOM.dto.kuCoinDtos.DOMDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KuCoinDepthOfMarket {
    private String code;
    private cryptoDOM.dto.kuCoinDtos.DOMDto.Data data;

}
