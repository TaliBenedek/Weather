package benedek.openweathermap;

import io.reactivex.rxjava3.core.Single;
import org.junit.Assert;
import org.junit.Test;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.*;

public class OpenWeatherMapServiceTest
{

    @Test
    public void getCurrentWeather()
    {
        //given
        OpenWeatherMapServiceFactory factory = new OpenWeatherMapServiceFactory();
        OpenWeatherMapService service = factory.newInstance();

        //when
        Single<OpenWeatherMapFeed> single = service.getCurrentWeather("London", "imperial");

        // DO NOT USE BLOCKING GET!
        OpenWeatherMapFeed feed = single.blockingGet();

        //then
        Assert.assertNotNull(feed);
        Assert.assertNotNull(feed.main);
        Assert.assertTrue(feed.main.temp > 0);
        Assert.assertTrue(feed.main.temp < 150);
        Assert.assertEquals("London", feed.name);
        Assert.assertTrue(feed.dt > 0);
    }

    @Test
    public void getWeatherForecast()
    {
        //given
        OpenWeatherMapServiceFactory factory = new OpenWeatherMapServiceFactory();
        OpenWeatherMapService service = factory.newInstance();

        //when
        Single<OpenWeatherMapForecast> single = service.getWeatherForecast("London", "imperial");

        // DO NOT USE BLOCKING GET!
        OpenWeatherMapForecast forecast = single.blockingGet();

        //then
        Assert.assertNotNull(forecast);
        Assert.assertNotNull(forecast.list);
        assertFalse(forecast.list.isEmpty());
        assertTrue(forecast.list.get(0).dt > 0);
        assertNotNull(forecast.list.get(0).weather);

    }
}