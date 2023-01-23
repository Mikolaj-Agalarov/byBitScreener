package cryptoDOM.dto.byBitDtos.DOMDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ByBitDepthOfMarketDto {
    private String ret_code;
    private String ret_msg;
    private Result result;
    private String ext_info;
    private String ext_code;
}
