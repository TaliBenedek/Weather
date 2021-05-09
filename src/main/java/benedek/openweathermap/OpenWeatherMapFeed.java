package benedek.openweathermap;

import java.util.Date;
import java.util.List;

public class OpenWeatherMapFeed
{
    Main main;
    String name;
    long dt;
    List<Weather> weather;
    static class Main
    {
        double temp;
        public double getTemperature()
        {
            return temp;
        }
    }

    static class Weather
    {
        String main;
        String icon;
        public String getIconUrl()
        {
            return "http://openweathermap.org/img/wn/" + icon + "@2x.png";
        }
    }

    public Date getTime()
    {
        return new Date(dt * 1000);
    }
}
