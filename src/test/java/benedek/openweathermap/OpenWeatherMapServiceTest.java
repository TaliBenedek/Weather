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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        OpenWeatherMapService service = retrofit.create(OpenWeatherMapService.class);

        //when
        Single<OpenWeatherMapFeed> single = service.getCurrentWeather("London");

        // DO NOT USE BLOCKING GET!
        OpenWeatherMapFeed feed = single.blockingGet();

        //then
        Assert.assertNotNull(feed);
        Assert.assertNotNull(feed.main);
        Assert.assertTrue(feed.main.temp > 0);
    }
}