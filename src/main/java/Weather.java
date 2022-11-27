import java.time.LocalDate;
import java.time.LocalTime;

public class Weather {
    public String Latitude;
    public String Longitude;
    public LocalDate date;
    public LocalTime time;
    public String condition;

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
}
