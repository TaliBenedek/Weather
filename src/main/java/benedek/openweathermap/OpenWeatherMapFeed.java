package benedek.openweathermap;

import java.util.Date;

public class OpenWeatherMapFeed
{
    Main main;
    String name;
    long dt;
    static class Main
    {
        double temp;
        public double getTemperature()
        {
            return temp;
        }
    }

    public Date getTime()
    {
        return new Date(dt * 1000);
    }
}
