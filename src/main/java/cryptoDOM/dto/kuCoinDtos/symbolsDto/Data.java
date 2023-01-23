package cryptoDOM.dto.kuCoinDtos.symbolsDto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
public class Data {
    private String symbol;
    private String name;
    private String baseCurrency;
    private String quoteCurrency;
    private String feeCurrency;
    private String market;
    private String baseMinSize;
    private String quoteMinSize;
    private String baseMaxSize;
    private String quoteMaxSize;
    private String baseIncrement;
    private String quoteIncrement;
    private String priceIncrement;
    private String priceLimitRate;
    private String minFunds;
    private String isMarginEnabled;
    private String enableTrading;
}
