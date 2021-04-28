package benedek.openweathermap;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherMapService
{
    @GET("/data/2.5/weather?appid=492f7559e977abb236b0e006a71cdf33")
    Single<OpenWeatherMapFeed> getCurrentWeather(
            @Query("q") String location,
            @Query("units") String units
    );

    @GET("/data/2.5/forecast?appid=492f7559e977abb236b0e006a71cdf33")
    Single<OpenWeatherMapForecast> getWeatherForecast(
            @Query("q") String location,
            @Query("units") String units
            );
}
