package cryptoDOM.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cryptoDOM.entity.TickerName;
import cryptoDOM.repository.TickerNameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TickerNameService {
    private final TickerNameRepository tickerNameRepository;

    public List<TickerName> getAllActiveTickers() {
        return tickerNameRepository.findByIsOnTrue();
    }
    public TickerName findtickerByTickername(String tickerName) {
        return tickerNameRepository.findBy(tickerName);
    }

    public void updateDB (JsonArray tickers) {
        for (JsonElement tickerJson : tickers.getAsJsonArray()) {
            JsonObject tickerData = tickerJson.getAsJsonObject();
            String name = tickerData.get("symbol").getAsString();
            if (name.contains("USDT-TUSD")
                    || name.contains("USDP-USDT")
                    || name.contains("BUSD-USDC")
                    || name.contains("USDC-USDT")
                    || name.contains("USDT-USDC")) {
                continue;
            }
            BigDecimal vol = new BigDecimal(tickerData.get("vol").getAsString());
            BigDecimal volValue = new BigDecimal(tickerData.get("volValue").getAsString());

            try {
                TickerName tickerEntity = findtickerByTickername(name);

                tickerEntity.setVol(vol.setScale(10, RoundingMode.HALF_UP));
                tickerEntity.setVolValue(volValue);
                if (volValue.multiply(BigDecimal.valueOf(0.05)).compareTo(BigDecimal.valueOf(30000)) > 0) {
                    tickerEntity.setMinOrderValue(volValue.multiply(BigDecimal.valueOf(0.05)));
                } else {
                    tickerEntity.setMinOrderValue(BigDecimal.valueOf(30000));
                }
                if (tickerEntity.getVolValue().compareTo(BigDecimal.valueOf(50000)) > 0) {
                    tickerEntity.setIsOn(true); }

                tickerNameRepository.save(tickerEntity);


            } catch (Exception e) {
                e.printStackTrace();
                TickerName tickerName = new TickerName();
                tickerName.setTickerName(name);
                tickerName.setVol(vol);
                tickerName.setVolValue(volValue);
                if (volValue.multiply(BigDecimal.valueOf(0.05)).compareTo(BigDecimal.valueOf(30000)) > 0){
                    tickerName.setMinOrderValue(volValue.multiply(BigDecimal.valueOf(0.05)));
                } else {
                    tickerName.setMinOrderValue(BigDecimal.valueOf(30000));
                }
                if (tickerName.getVolValue().compareTo(BigDecimal.valueOf(50000)) < 0) {
                    tickerName.setIsOn(false);
                } else {
                    tickerName.setIsOn(true);
                }
                tickerNameRepository.save(tickerName);
            }


        }
    }

    public void deleteAll () {
        tickerNameRepository.deleteAll();
    }

    public List<TickerName> getAll () {
        return tickerNameRepository.findAll();
    }

    public TickerName findByTickerName(String tickerName) {
       return tickerNameRepository.findBy(tickerName);
    }

    public List<TickerName> getAllTickerNames() {
        return tickerNameRepository.findAll();
    }

    public List<TickerName> findByIsOnTrue() {
        return tickerNameRepository.findByIsOnTrue();
    }
}
