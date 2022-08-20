import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class ReadCsv {
    public static List<String[]> readAllDataAtOnce(String file)
    {
        List<String[]> allData = null;
        try {
            // Create an object of file reader
            // class with CSV file as a parameter.
            FileReader filereader = new FileReader(file);

            // create csvReader object and skip first Line
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withSkipLines(1)
                    .build();
            allData = csvReader.readAll();
            return allData;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return allData;
    }

    public static void main(String[] args) {
        String filePath = "C:\\Users\\Michael\\Desktop\\Test3.csv";
        List<String[]> lists = readAllDataAtOnce(filePath);
        List<Weather> weatherList = new ArrayList<>();
        for(int i=0; i<lists.size(); i++){
            Weather weather = new Weather();
            weather.Latitude = lists.get(i)[21];
            weather.Longitude = lists.get(i)[20];
            weather.date = LocalDate.parse(lists.get(i)[68]);
            weather.time = LocalTime.parse(lists.get(i)[69]);
            weatherList.add(weather);
            System.out.println("Reading line "+i);
        }
//        System.out.println(weatherList.get(0).toString());
        int i=0;
        for (Weather weather : weatherList) {
            System.out.println("do get "+i);
            i++;
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
            }catch (Exception e){
                weather.condition = "Sunny";
            }

        }

        try {
            FileWriter fileWriter = new FileWriter("C:\\Users\\Michael\\Desktop\\TestOutput2.csv");
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
