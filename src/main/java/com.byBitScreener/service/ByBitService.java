package main.java.com.byBitScreener.service;

import com.google.gson.Gson;
import io.micrometer.core.instrument.util.IOUtils;
import main.java.com.byBitScreener.dto.depth.ByBitDepthOfMarketDto;
import main.java.com.byBitScreener.dto.symblos.ByBitTickersDto;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;


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

    public void getDepthOfMarket() throws Exception {
        ArrayList<String> arrayWithTickers = getTickersFromTxtToArrayList();
        Gson gson = new Gson();
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet("https://api.bybit.com/spot/quote/v1/depth?symbol=ETHUSDT&limit=150");
        CloseableHttpResponse response = client.execute(get);
        String content = IOUtils.toString(response.getEntity().getContent());

        ByBitDepthOfMarketDto resultDto = gson.fromJson(content, ByBitDepthOfMarketDto.class);
        System.out.println("----------------");
        ArrayList<String> listOfAsks = new ArrayList<>();
//        Arrays.stream(resultDto.getResult().getList()).forEach(list -> listOfTickers.add(list.getName()));
//        Arrays.stream(resultDto.getResult().getAsk()).forEach(ask -> System.out.println(ask.toString()));

    }
}
