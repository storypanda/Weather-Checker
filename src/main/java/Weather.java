import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class Weather {
    public String Latitude;
    public String Longitude;
    public LocalDate date;
    public LocalTime time;
    public String condition;

    public static String getFiveYearWeather(){
        Map<String, String> param = new HashMap<>();
        param.put("key","ee8d601a63484366aaa110050221208");
        param.put("q","-37.97120886,145.23721954");
        param.put("dt","2015-01-01");

        return HttpClientUtil.doGet("http://api.weatherapi.com/v1/history.json",param);
    }

    @Override
    public String toString() {
        return "Weather{" +
                "Latitude='" + Latitude + '\'' +
                ", Longitude='" + Longitude + '\'' +
                ", date=" + date +
                ", time=" + time +
                ", condition='" + condition + '\'' +
                '}';
    }

    public static void main(String[] args) {
        String jsonStringResult = getFiveYearWeather();
        System.out.println(jsonStringResult);
        JSONObject jsonObjectResult = JSON.parseObject(jsonStringResult);
        JSONObject forecast = (JSONObject) jsonObjectResult.get("forecast");
        JSONArray forecastday = (JSONArray) forecast.get("forecastday");
        JSONObject forecastday0 = (JSONObject) forecastday.get(0);
        JSONArray hour = (JSONArray) forecastday0.get("hour");
        JSONObject hour0 = (JSONObject) hour.get(0);
        JSONObject condition = (JSONObject) hour0.get("condition");
        System.out.println(condition.get("text"));
    }
}
