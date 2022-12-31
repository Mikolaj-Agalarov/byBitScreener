package main.java.com.byBitScreener.service;

import com.google.gson.Gson;
import io.micrometer.core.instrument.util.IOUtils;
import main.java.com.byBitScreener.dto.depthDto.ByBitDepthOfMarketDto;
import main.java.com.byBitScreener.dto.depthDto.DepthDto;
import main.java.com.byBitScreener.dto.symblosDto.ByBitTickersDto;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@Service
public class ByBitService {
    private static final String API_MARKET_ENDPOINT = "https://api.bybit.com";
    private static final String SYMBOLS_ENDPOINT ="https://api.bybit.com/spot/v3/public/symbols";


    public void getAllSpotTickersFromApiAndWriteThemToTxt() throws Exception {
        //Getting JSON and converting it into a String from response
        Gson gson = new Gson();
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(SYMBOLS_ENDPOINT);
        CloseableHttpResponse response = client.execute(get);
        String content = IOUtils.toString(response.getEntity().getContent());


        if (response.getStatusLine().getStatusCode() == 200) {
            //Converting String JSON to DTO java object with gson library
            ByBitTickersDto resultDto = gson.fromJson(content, ByBitTickersDto.class);
            System.out.println("----------------");
            ArrayList<String> listOfTickers = new ArrayList<>();
            Arrays.stream(resultDto.getResult().getList()).forEach(list -> listOfTickers.add(list.getName()));

            //Clearing txt file and them writing all tickers from an array to it
            FileWriter fileWriter = new FileWriter("D:\\TMS\\byBitScreener\\byBitScreener\\src\\main\\java\\com.byBitScreener\\tickers.txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print("");
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            listOfTickers.stream().forEach(s -> {
                try {
                    bufferedWriter.write(s + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            bufferedWriter.close();
        }
    }

    public ArrayList<String> getTickersFromTxtToArrayList() {
        ArrayList<String> arrayWithTickers = new ArrayList<>();
        try (BufferedReader txtReader = new BufferedReader(new FileReader("D:\\TMS\\byBitScreener\\" +
                "byBitScreener\\src\\main\\java\\com.byBitScreener\\tickers.txt"))) {
            String str;
            while ((str = txtReader.readLine()) != null) {
                arrayWithTickers.add(str);
            }
            return arrayWithTickers;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<DepthDto> getDepthOfMarket() throws Exception {
        Gson gson = new Gson();
        CloseableHttpClient client = HttpClients.createDefault();
        ArrayList<String> arrayWithTickers = getTickersFromTxtToArrayList();
        Integer USDTLimit = 100000;
        ArrayList<DepthDto> arrayListWithDepthDto = new ArrayList<>();

        for(int i = 0; i < 5; i++) {
            HttpGet get = new HttpGet("https://api.bybit.com/spot/quote/v1/depth?symbol=" +
                    arrayWithTickers.get(i) +
                    "&limit=200");
            CloseableHttpResponse response = client.execute(get);
            String content = IOUtils.toString(response.getEntity().getContent());
            ByBitDepthOfMarketDto resultDto = gson.fromJson(content, ByBitDepthOfMarketDto.class);
            System.out.println("----------------");

            DepthDto glassInstance = new DepthDto();
            Map<Float, Float> mapOfAsks = new HashMap<>();
            Map<Float, Float> mapOfBids = new HashMap<>();
            Arrays.stream(resultDto.getResult().getAsks())
                    .forEach(strings -> {
                        if (strings[1]*strings[0] > USDTLimit) {
                            mapOfAsks.put(strings[0], strings[1]);
                        }
                    });
            Arrays.stream(resultDto.getResult().getBids())
                    .forEach(strings -> {
                        if (strings[1]*strings[0] > USDTLimit) {
                            mapOfBids.put(strings[0], strings[1]);
                        }
                    });
            glassInstance.setTickerName(arrayWithTickers.get(i));
            glassInstance.setAsks(mapOfAsks);
            glassInstance.setBids(mapOfBids);
            arrayListWithDepthDto.add(glassInstance);
        }
        System.out.println(arrayListWithDepthDto.toString());
        return arrayListWithDepthDto;
    }
}
