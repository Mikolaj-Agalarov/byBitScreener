package cryptoDOM.mapper;

import cryptoDOM.dto.kuCoinDtos.symbolsDto.Data;
import cryptoDOM.dto.kuCoinDtos.symbolsDto.KuCoinTickerDto;
import cryptoDOM.entity.TickerName;
import cryptoDOM.entity.User;
import lombok.ToString;

import java.math.BigDecimal;

@ToString
public class TickerMapper {

    public static TickerName fromTickerDtoDataToEntity (Data data) {
        TickerName tickerName = new TickerName();
        tickerName.setTickerName(data.getSymbol());
        tickerName.setMinOrderValue(BigDecimal.valueOf(30000));
        tickerName.setIsOn(true);
        return tickerName;
    }
}
