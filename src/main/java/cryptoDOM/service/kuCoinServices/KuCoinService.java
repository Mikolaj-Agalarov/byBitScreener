package cryptoDOM.service.kuCoinServices;

import com.google.gson.Gson;
import cryptoDOM.dto.byBitDtos.DOMDto.DepthDto;
import cryptoDOM.dto.kuCoinDtos.DOMDto.GlassInstance;
import cryptoDOM.dto.kuCoinDtos.DOMDto.KuCoinDepthDto;
import cryptoDOM.dto.kuCoinDtos.DOMDto.KuCoinDepthOfMarket;
import cryptoDOM.dto.kuCoinDtos.symbolsDto.kuCoinTickerDto;
import io.micrometer.core.instrument.util.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class KuCoinService {
    private static final String API_MARKET_ENDPOINT = "https://openapi-v2.kucoin.com/";
    private static final String SYMBOLS_ENDPOINT = "https://openapi-v2.kucoin.com/api/v1/symbols";

    public void getAllSpotTickersFromApiAndWriteThemToTxt() throws Exception {
        //Getting JSON and converting it into a String from response
        Gson gson = new Gson();
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(SYMBOLS_ENDPOINT);
        CloseableHttpResponse response = client.execute(get);
        String content = IOUtils.toString(response.getEntity().getContent());


        if (response.getStatusLine().getStatusCode() == 200) {
            //Converting String JSON to DTO java object with gson library
            kuCoinTickerDto resultDto = gson.fromJson(content, kuCoinTickerDto.class);
            System.out.println("----------------");
            ArrayList<String> listOfTickers = new ArrayList<>();
            Arrays.stream(resultDto.getData()).forEach(data -> listOfTickers.add(data.getSymbol()));


            //Clearing txt file and them writing all tickers from an array to it
            FileWriter fileWriter = new FileWriter("D:\\TMS\\byBitScreener\\byBitScreener\\src\\main\\resources\\tickers\\tickersKuCoin.txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print("");
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            listOfTickers.stream().forEach(s ->
            {
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
        try (BufferedReader txtReader = new BufferedReader(new FileReader("D:\\TMS\\byBitScreener\\byBitScreener\\src\\main\\resources\\tickers\\tickersKuCoin.txt"))) {
            String str;
            while ((str = txtReader.readLine()) != null) {
                arrayWithTickers.add(str);
            }
            return arrayWithTickers;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<GlassInstance> getDepthOfMarket(Integer scanningrange, Integer minValueInUsd) throws Exception {
        Gson gson = new Gson();
        CloseableHttpClient client = HttpClients.createDefault();
        ArrayList<String> arrayWithTickers = getTickersFromTxtToArrayList();
        ArrayList<DepthDto> arrayListWithDepthDto = new ArrayList<>();

//       getting price of BTC, ETH, KCS for calculating the size of order in USDT
        Float currentPriceOfBtc = 0.0f;
        Float currentPriceOfEth = 0.0f;
        Float currentPriceOfKsc = 0.0f;

        List<String> listWithTickerNamesOfBtcEthKcs = new ArrayList<>();
        listWithTickerNamesOfBtcEthKcs.add("BTC-USDT");
        listWithTickerNamesOfBtcEthKcs.add("ETH-USDT");
        listWithTickerNamesOfBtcEthKcs.add("KCS-USDT");

        for (String tickerName : listWithTickerNamesOfBtcEthKcs)
        {
            HttpGet get = new HttpGet("https://openapi-v2.kucoin.com/api/v1/market/orderbook/level2_100?symbol=" +
                    tickerName);
            CloseableHttpResponse response = client.execute(get);
            String content = IOUtils.toString(response.getEntity().getContent());
            KuCoinDepthOfMarket resultDto = gson.fromJson(content, KuCoinDepthOfMarket.class);
            if (tickerName.equals("BTC-USDT")) {
                currentPriceOfBtc = Arrays.stream(resultDto.getData().getBids()).findFirst().get()[0];
            } else if (tickerName.equals("ETH-USDT")) {
                currentPriceOfEth = Arrays.stream(resultDto.getData().getBids()).findFirst().get()[0];
            } else {
                currentPriceOfKsc = Arrays.stream(resultDto.getData().getBids()).findFirst().get()[0];
            }
        }

        List<GlassInstance> glassInstances = new ArrayList<>();
        Float percentageRange = (float) scanningrange;
        Float minOrderValue = (float) minValueInUsd;
//        тут будет фор луп для каждого тикера, если название тикера содержит BTC-USDT or ETH-USDT or KCS-USDt записываем данные
//        for (int i = 1; i < arrayWithTickers.size(); i++)
        for (int i = 0; i < 200; i++)
        {
            long startTime = System.currentTimeMillis();

            HttpGet get = new HttpGet("https://openapi-v2.kucoin.com/api/v1/market/orderbook/level2_100?symbol=" +
                    arrayWithTickers.get(i));
            CloseableHttpResponse response = client.execute(get);
            String content = IOUtils.toString(response.getEntity().getContent());
            KuCoinDepthOfMarket resultDto = gson.fromJson(content, KuCoinDepthOfMarket.class);

            KuCoinDepthDto kuCoinDepthDto = new KuCoinDepthDto();
            List<List<Float>> listOfAsks = new ArrayList<>();
            List<List<Float>> listOfBids = new ArrayList<>();

            try {
                if (resultDto.getData().getAsks() != null && resultDto.getData().getBids() != null) {
                    Float highestBidPrice = Arrays.stream(resultDto.getData().getBids())
                            .findFirst()
                            .orElse(new Float[]{0.0f})[0];

                    Arrays.stream(resultDto.getData().getAsks()).forEach(floats ->
                    {
                        List<Float> nestedList = new ArrayList<>();
                        Float percentageFromAskToHighestBid = ((floats[0] - highestBidPrice) / floats[0]) * 100;
                        Float rounded = (float) (Math.round(percentageFromAskToHighestBid * 100) / 100.0);
                        nestedList.add(floats[0]);
                        nestedList.add(floats[1]);
                        nestedList.add(rounded);
                        listOfAsks.add(nestedList);
                    });

                    Arrays.stream(resultDto.getData().getBids()).forEach(floats ->
                    {
                        List<Float> nestedList = new ArrayList<>();
                        Float percentageFromBidToHighestBid = ((highestBidPrice - floats[0]) / highestBidPrice) * 100;
                        Float rounded = (float) (Math.round(percentageFromBidToHighestBid * 100) / 100.0);
                        nestedList.add(floats[0]);
                        nestedList.add(floats[1]);
                        nestedList.add(rounded);
                        listOfBids.add(nestedList);
                    });

                    kuCoinDepthDto.setAsks(listOfAsks);
                    kuCoinDepthDto.setBids(listOfBids);
                    kuCoinDepthDto.setCurrentPriceOfBtc(currentPriceOfBtc);
                    kuCoinDepthDto.setCurrentPriceOfEth(currentPriceOfEth);
                    kuCoinDepthDto.setCurrentPriceOfKsc(currentPriceOfKsc);

                    System.out.println(kuCoinDepthDto);

                    GlassInstance glassInstance = new GlassInstance();
                    List<Float> pricesOfBiggestAsks = new ArrayList<>();

                    System.out.println(arrayWithTickers.get(i));

                    if (arrayWithTickers.get(i).contains("-BTC"))
                    {
                        Float finalCurrentPriceOfBtc = currentPriceOfBtc;
                        kuCoinDepthDto.getAsks().stream()
                                .filter(floats -> floats.get(0) * floats.get(1) * finalCurrentPriceOfBtc >= minOrderValue && floats.get(2) <= percentageRange)
                                .sorted(Comparator.comparingDouble(floats -> floats.get(1)))
                                .limit(4)
                                .forEach(floats -> pricesOfBiggestAsks.add(floats.get(0)));
                    }
                    //            }
                    //            if (arrayWithTickers.get(i).contains("-ETH"))
                    //            {
                    //                Float finalCurrentPriceOfEth = currentPriceOfEth;
                    //                kuCoinDepthDto.getAsks().stream().forEach(floats ->
                    //                {
                    //                    if (floats.get(1) * finalCurrentPriceOfEth >= minOrderValue && floats.get(2) <= percentageRange) {
                    //                        pricesOfBiggestAsks.add(floats.get(1));
                    //                    }
                    //                });
                    //            }
                    //            if (arrayWithTickers.get(i).contains("-KSC"))
                    //            {
                    //                Float finalCurrentPriceOfKsc = currentPriceOfKsc;
                    //                kuCoinDepthDto.getAsks().stream().forEach(floats ->
                    //                {
                    //                    if (floats.get(1) * finalCurrentPriceOfKsc >= minOrderValue && floats.get(2) <= percentageRange) {
                    //                        pricesOfBiggestAsks.add(floats.get(1));
                    //                    }
                    //                });
                    //            }
                    if (arrayWithTickers.get(i).contains("-USDT")
                            || arrayWithTickers.get(i).contains("-DAI")
                            || arrayWithTickers.get(i).contains("-USDC")
                            || arrayWithTickers.get(i).contains("-BUSD")
                            || arrayWithTickers.get(i).contains("-USD"))
                    {
                        kuCoinDepthDto.getAsks().stream()
                                .filter(floats -> floats.get(0) * floats.get(1) >= minOrderValue && floats.get(2) <= percentageRange)
                                .sorted(Comparator.comparingDouble(floats -> floats.get(1)))
                                .limit(4)
                                .forEach(floats -> pricesOfBiggestAsks.add(floats.get(0)));

                        // Теперь надо проверить, сколько у меня цен асков имеется, если их менее трех, то необходимо добавить наибольшие значения асков из оставшихся и добавить
                        //                if (pricesOfBiggestAsks.size() == 1)
                        //                {
                        //                    OptionalInt indexOfOnlyOneAskInPricesList = IntStream.range(0, kuCoinDepthDto.getAsks().size())
                        //                            .filter(b -> kuCoinDepthDto.getAsks().get(b).get(0).equals(pricesOfBiggestAsks.get(0)))
                        //                            .findFirst();
                        //                    if (indexOfOnlyOneAskInPricesList.isPresent())
                        //                    {
                        //                        List<Float> largestValuesBeforeIndex = IntStream
                        //                                .range(0, indexOfOnlyOneAskInPricesList.getAsInt())
                        //                                .boxed()
                        //                                .sorted((a, b) -> kuCoinDepthDto.getAsks().get(b).get(1).compareTo(kuCoinDepthDto.getAsks().get(a).get(1)))
                        //                                .map(b -> kuCoinDepthDto.getAsks().get(b).get(0))
                        //                                .limit(2)
                        //                                .collect(Collectors.toList());
                        //                        if (largestValuesBeforeIndex.size() == 0)
                        //                        {
                        //                            List<Float> largestValuesAfterIndex = IntStream
                        //                                    .range(indexOfOnlyOneAskInPricesList.getAsInt(), kuCoinDepthDto.getAsks().size())
                        //                                    .boxed()
                        //                                    .filter(b -> kuCoinDepthDto.getAsks().get(b).get(2) <= percentageRange)
                        //                                    .map(b -> kuCoinDepthDto.getAsks().get(b).get(1))
                        //                                    .sorted(Comparator.comparingDouble(value -> value))
                        //                                    .limit(2)
                        //                                    .collect(Collectors.toList());
                        //                            largestValuesAfterIndex.stream().forEach(aFloat -> pricesOfBiggestAsks.add(aFloat));
                        //                        }
                        //                        else if (largestValuesBeforeIndex.size() == 1)
                        //                        {
                        //                            List<Float> largestValuesAfterIndex = IntStream
                        //                                    .range(indexOfOnlyOneAskInPricesList.getAsInt(), kuCoinDepthDto.getAsks().size())
                        //                                    .boxed()
                        //                                    .filter(b -> kuCoinDepthDto.getAsks().get(b).get(2) <= percentageRange)
                        //                                    .map(b -> kuCoinDepthDto.getAsks().get(b).get(1))
                        //                                    .sorted(Comparator.comparingDouble(value -> value))
                        //                                    .limit(1)
                        //                                    .collect(Collectors.toList());
                        //
                        //                            largestValuesAfterIndex.stream().forEach(aFloat -> pricesOfBiggestAsks.add(aFloat));
                        //                            largestValuesBeforeIndex.stream().forEach(aFloat -> pricesOfBiggestAsks.add(aFloat));
                        //                        } else if (largestValuesBeforeIndex.size() == 2) {
                        //                            largestValuesBeforeIndex.stream().forEach(aFloat -> pricesOfBiggestAsks.add(aFloat));
                        //                        }
                        //                    }
                        //                }
                        //                else if (pricesOfBiggestAsks.size() == 2)
                        //                {
                        //                    OptionalInt indexOfFirstValue = IntStream.range(0, kuCoinDepthDto.getAsks().size())
                        //                            .filter(b -> kuCoinDepthDto.getAsks().get(b).get(0).equals(pricesOfBiggestAsks.get(0)))
                        //                            .findFirst();
                        //                    OptionalInt indexOfSecondValue = IntStream.range(0, kuCoinDepthDto.getAsks().size())
                        //                            .filter(b -> kuCoinDepthDto.getAsks().get(b).get(0).equals(pricesOfBiggestAsks.get(1)))
                        //                            .findFirst();
                        //                    if (indexOfFirstValue.isPresent() && indexOfSecondValue.isPresent())
                        //                    {
                        //                        List<Float> largestValuesBeforeFirstIndex = IntStream
                        //                                .range(0, indexOfFirstValue.getAsInt())
                        //                                .boxed()
                        //                                .map(b -> kuCoinDepthDto.getAsks().get(b).get(1))
                        //                                .sorted(Comparator.comparingDouble(value -> value))
                        //                                .limit(1)
                        //                                .collect(Collectors.toList());
                        //                        if (largestValuesBeforeFirstIndex.size() == 0)
                        //                        {
                        //                            List<Float> largestValuesAfterFirstIndexAndBeforeSecondIndex = IntStream
                        //                                    .range(indexOfFirstValue.getAsInt(), indexOfSecondValue.getAsInt())
                        //                                    .boxed()
                        //                                    .filter(b -> kuCoinDepthDto.getAsks().get(b).get(2) <= percentageRange)
                        //                                    .map(b -> kuCoinDepthDto.getAsks().get(b).get(1))
                        //                                    .sorted(Comparator.comparingDouble(value -> value))
                        //                                    .limit(1)
                        //                                    .collect(Collectors.toList());
                        //                            if (largestValuesAfterFirstIndexAndBeforeSecondIndex.size() == 0)
                        //                            {
                        //                                List<Float> largestValuesAfterSecondIndex = IntStream
                        //                                        .range(indexOfSecondValue.getAsInt(), kuCoinDepthDto.getAsks().size())
                        //                                        .boxed()
                        //                                        .filter(b -> kuCoinDepthDto.getAsks().get(b).get(2) <= percentageRange)
                        //                                        .map(b -> kuCoinDepthDto.getAsks().get(b).get(1))
                        //                                        .sorted(Comparator.comparingDouble(value -> value))
                        //                                        .limit(2)
                        //                                        .collect(Collectors.toList());
                        //                                largestValuesAfterSecondIndex.stream().forEach(aFloat -> pricesOfBiggestAsks.add(aFloat));
                        //                            }
                        //                            else if (largestValuesAfterFirstIndexAndBeforeSecondIndex.size() == 1)
                        //                            {
                        //                                List<Float> largestValuesAfterSecondIndex = IntStream
                        //                                        .range(indexOfSecondValue.getAsInt(), kuCoinDepthDto.getAsks().size())
                        //                                        .boxed()
                        //                                        .filter(b -> kuCoinDepthDto.getAsks().get(b).get(2) <= percentageRange)
                        //                                        .map(b -> kuCoinDepthDto.getAsks().get(b).get(1))
                        //                                        .sorted(Comparator.comparingDouble(value -> value))
                        //                                        .limit(1)
                        //                                        .collect(Collectors.toList());
                        //                                largestValuesAfterSecondIndex.stream().forEach(aFloat -> pricesOfBiggestAsks.add(aFloat));
                        //                                largestValuesAfterFirstIndexAndBeforeSecondIndex.stream().forEach(aFloat -> pricesOfBiggestAsks.add(aFloat));
                        //                            }
                        //                        }
                        //                    }
                        //                }
                    }

                    // Ищу наибольшие значения для Bids
                    List<Float> pricesOfBiggestBids = new ArrayList<>();

                    if (arrayWithTickers.get(i).contains("-BTC"))
                    {
                        Float finalCurrentPriceOfBtc = currentPriceOfBtc;
                        kuCoinDepthDto.getBids().stream()
                                .filter(floats -> floats.get(0) * floats.get(1) * finalCurrentPriceOfBtc >= minOrderValue &&
                                        floats.get(2) <= percentageRange)
                                .sorted(Comparator.comparingDouble(floats -> floats.get(1)))
                                .limit(4)
                                .forEach(floats -> pricesOfBiggestBids.add(floats.get(0)));
                    }

                    if (arrayWithTickers.get(i).contains("-USDT")
                            || arrayWithTickers.get(i).contains("-DAI")
                            || arrayWithTickers.get(i).contains("-USDC")
                            || arrayWithTickers.get(i).contains("-BUSD")
                            || arrayWithTickers.get(i).contains("-USD")) {
                        kuCoinDepthDto.getBids().stream()
                                .filter(floats -> floats.get(0) * floats.get(1) >= minOrderValue &&
                                        floats.get(2) <= percentageRange)
                                .sorted(Comparator.comparingDouble(floats -> floats.get(1)))
                                .limit(4)
                                .forEach(floats -> pricesOfBiggestBids.add(floats.get(0)));
                    }

                    // Конвертирую значения из biggestBids and BiggestAsks в соответствующие для фронта значения нового класса GlassInstance
                    List<List<Float>> fullBids = pricesOfBiggestBids.stream()
                            .map(price -> kuCoinDepthDto.getBids().stream()
                                    .filter(bid -> bid.get(0).equals(price))
                                    .findFirst())
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .sorted(Comparator.comparing(bid -> -bid.get(0)))
                            .collect(Collectors.toList());

                    List<List<Float>> fullAsks = pricesOfBiggestAsks.stream()
                            .map(price -> kuCoinDepthDto.getAsks().stream()
                                    .filter(ask -> ask.get(0).equals(price))
                                    .findFirst())
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .sorted(Comparator.comparing(ask -> -ask.get(0)))
                            .collect(Collectors.toList());

                    if (!pricesOfBiggestAsks.isEmpty() || !pricesOfBiggestBids.isEmpty()) {
                        if (!pricesOfBiggestBids.isEmpty()) {
                            glassInstance.setPriceOfBiggestBid(pricesOfBiggestBids.get(0));
                        }
                        if (!pricesOfBiggestAsks.isEmpty()) {
                            glassInstance.setPriceOfBiggestAsk(pricesOfBiggestAsks.get(0));
                        }
                        glassInstance.setTickerName(arrayWithTickers.get(i));
                        glassInstance.setAsks(fullAsks);
                        glassInstance.setBids(fullBids);
                        glassInstance.setCurrentPriceOfBtc(currentPriceOfBtc);
                        glassInstances.add(glassInstance);
                    }

                    long endTime = System.currentTimeMillis();
                    long duration = endTime - startTime;

                    System.out.println("Duration: " + duration + " milliseconds");
                } else {
                    GlassInstance glassInstance = new GlassInstance();
                    glassInstances.add(glassInstance);
                }
            }
            catch (Exception ignored) {

            }


        }

        return glassInstances;
    }
}
