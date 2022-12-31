package main.java.com.byBitScreener.dto.symblosDto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ByBitTickersDto {
    private String retCode;
    private String retMsg;
    private String ext_info;
    private Result result;
    private String time_now;
}
