package cryptoDOM.dto.kuCoinDtos.symbolsDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class kuCoinTickerDto {
    private String code;
    private cryptoDOM.dto.kuCoinDtos.symbolsDto.Data[] data;
}
