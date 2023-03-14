package cryptoDOM.scheduler;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import cryptoDOM.service.DOMService;
import cryptoDOM.service.TickerNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalTime;
import java.util.Date;

@Component
public class Scheduler {
    @Autowired
    private TickerNameService tickerNameService;

    @Autowired
    private DOMService domService;

    @Scheduled(fixedRate = 60000)
    public void getData() throws Exception {
        // Getting JSON and converting it into a String from response
        RestTemplate restTemplate = new RestTemplate();

        Gson gson = new Gson();
        String url = "https://openapi-v2.kucoin.com/api/v1/market/allTickers";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String json = response.getBody();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        JsonObject data = jsonObject.get("data").getAsJsonObject();
        JsonArray tickers = data.get("ticker").getAsJsonArray();

        System.out.println("TickerNames table update started");

        tickerNameService.updateDB(tickers);

        System.out.println("TickerNames table updated");

        domService.updateDomsFromAPI();

        System.out.println("DOMS table updated");

    }
}
