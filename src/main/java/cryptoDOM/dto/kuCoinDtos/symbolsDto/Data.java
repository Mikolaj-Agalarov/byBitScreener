package cryptoDOM.dto.kuCoinDtos.symbolsDto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
public class Data {
    @SerializedName("symbol")
    private String symbol;

    @SerializedName("symbolName")
    private String symbolName;

    @SerializedName("buy")
    private String buy;

    @SerializedName("sell")
    private String sell;

    @SerializedName("changeRate")
    private String changeRate;

    @SerializedName("changePrice")
    private String changePrice;

    @SerializedName("high")
    private String high;

    @SerializedName("low")
    private String low;

    @SerializedName("vol")
    private String vol;

    @SerializedName("volValue")
    private String volValue;

    @SerializedName("last")
    private String last;

    @SerializedName("averagePrice")
    private String averagePrice;

    @SerializedName("takerFeeRate")
    private String takerFeeRate;

    @SerializedName("makerFeeRate")
    private String makerFeeRate;

    @SerializedName("takerCoefficient")
    private String takerCoefficient;

    @SerializedName("makerCoefficient")
    private String makerCoefficient;
}
