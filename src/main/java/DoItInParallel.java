import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class DoItInParallel implements Callable<Void> {

    private final List<Weather> weathers;

    public DoItInParallel(List<Weather> weathers) {
        this.weathers = weathers;
    }

    @Override
    public Void call() throws Exception {
        for (Weather weather : weathers) {
            //here you can call your services or just use jdbc api to retrieve data from db and do some business logic
            Map<String, String> param = new HashMap<>();
            param.put("key","ee8d601a63484366aaa110050221208");
            param.put("q",weather.Latitude+","+weather.Longitude);
            param.put("dt",weather.date.toString());
            String jsonStringResult = HttpClientUtil.doGet("http://api.weatherapi.com/v1/history.json",param);
            JSONObject jsonObjectResult = JSON.parseObject(jsonStringResult);
            try{
                JSONObject forecast = (JSONObject) jsonObjectResult.get("forecast");
                JSONArray forecastday = (JSONArray) forecast.get("forecastday");
                JSONObject forecastday0 = (JSONObject) forecastday.get(0);
                JSONArray hour = (JSONArray) forecastday0.get("hour");
                JSONObject realHour = (JSONObject) hour.get(weather.time.getHour());
                JSONObject condition = (JSONObject) realHour.get("condition");
                weather.condition = condition.get("text").toString();
                System.out.println(weather.condition);
            }catch (Exception e){
                weather.condition = "Null";
            }
        }
        return null;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        final int objectsCount = 4;
        final int threads = 4;
        final int objectsPerThread = objectsCount / threads;

        //your 75320 objects stored here
        String filePath = "C:\\Users\\Michael\\Desktop\\Test3.csv";
        List<String[]> lists = ReadCsv.readAllDataAtOnce(filePath);
        List<Weather> weatherList = new ArrayList<>(4);
        for(int i=0; i<lists.size(); i++){
            Weather weather = new Weather();
            weather.Latitude = lists.get(i)[21];
            weather.Longitude = lists.get(i)[20];
            weather.date = LocalDate.parse(lists.get(i)[68]);
            weather.time = LocalTime.parse(lists.get(i)[69]);
            weatherList.add(weather);
            System.out.println("Reading line "+i);
        }

        final CompletionService<Void> completionService = new ExecutorCompletionService<>(Executors.newFixedThreadPool(threads));

        for (int from = 0; from < objectsCount; from += objectsPerThread) {
            completionService.submit(new DoItInParallel(weatherList.subList(from, from += 1)));
        }

        for (int i = 0; i < threads; i++) {
            completionService.take().get();
        }

        try {
            FileWriter fileWriter = new FileWriter("C:\\Users\\Michael\\Desktop\\TestOutput.csv");
            CSVWriter csvWriter = new CSVWriter(fileWriter);
            List<String[]> data = new ArrayList<String[]>();
            data.add(new String[] {"condition"});
            int j = 0;
            for(Weather weather : weatherList){
                data.add(new String[] {weather.condition});
                System.out.println("Current write is NO." + j);
                j++;
            }
            csvWriter.writeAll(data);
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}