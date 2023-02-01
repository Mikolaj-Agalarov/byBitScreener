package cryptoDOM.dto.byBitDtos.symbolsDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ListForDto {
    private String name;
    private String alias;
    private String baseCoin;
    private String quoteCoin;
    private String basePrecision;
    private String quotePrecision;
    private String minTradeQty;
    private String minTradeAmt;
    private String maxTradeQty;
    private String maxTradeAmt;
    private String minPricePrecision;
    private String category;
    private String showStatus;
    private String innovation;
}

