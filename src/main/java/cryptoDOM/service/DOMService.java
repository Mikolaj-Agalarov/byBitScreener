package cryptoDOM.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cryptoDOM.entity.Ask;
import cryptoDOM.entity.DOM;
import cryptoDOM.entity.TickerName;
import cryptoDOM.repository.AskRepository;
import cryptoDOM.repository.DOMRepository;
import cryptoDOM.repository.TickerNameRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


import java.util.List;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class DOMService {
    @Autowired
    private TickerNameRepository tickerNameRepository;

    @Autowired
    private DOMRepository domRepository;

    @Autowired
    private AskService askService;

    @Autowired
    private BidService bidService;

    public DOM findByTickerName(String tickerName) {
        return domRepository.findByTickerName(tickerName);
    }

    public void updateDomsFromAPI() {
        RestTemplate restTemplate = new RestTemplate();
        ExecutorService executorService  = Executors.newFixedThreadPool(10);

        List<TickerName> tickerNames = tickerNameRepository.findByIsOnTrue();

        for (TickerName tickerName : tickerNames) {
            String url = "https://openapi-v2.kucoin.com/api/v1/market/orderbook/level2_100?symbol=" + tickerName.getTickerName();

            Callable<Void> task = () -> {
                ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
                String json = response.getBody();

                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

                JsonObject data = jsonObject.get("data").getAsJsonObject();
                JsonArray bids = data.get("bids").getAsJsonArray();
                JsonArray asks = data.get("asks").getAsJsonArray();

                DOM domFromTable = findByTickerName(tickerName.getTickerName());

                if (domFromTable == null) {
                    DOM dom = new DOM();
                    dom.setTickerName(tickerName);
                    dom.setHighest_bid_price(bids.get(0).getAsJsonArray().get(0).getAsFloat());
                    dom.setLowest_ask_price(asks.get(0).getAsJsonArray().get(0).getAsFloat());
                    domRepository.save(dom);

                    executorService.submit(() -> {
                        askService.processAsks(asks, tickerName, dom);
                    });

                    executorService.submit(() -> {
                        bidService.processBids(bids, tickerName, dom);
                    });
                } else {
                    domFromTable.setHighest_bid_price(bids.get(0).getAsJsonArray().get(0).getAsFloat());
                    domFromTable.setLowest_ask_price(asks.get(0).getAsJsonArray().get(0).getAsFloat());

                    executorService.submit(() -> {
                        askService.processAsks(asks, tickerName, domFromTable);
                    });

                    executorService.submit(() -> {
                        bidService.processBids(bids, tickerName, domFromTable);
                    });

                }
                
                return null;
            };

            executorService.submit(task); // submit the task to the executor
        }

    }

    public List<DOM> getAllDOMs() {
        return domRepository.findAll();
    }

    public void deleteAll() {
        domRepository.deleteAll();
    }
    public List<DOM> getDOMsWithAsksOrBids() {
        return domRepository.findByAsksIsNotNullOrBidsIsNotNull();
    }


}