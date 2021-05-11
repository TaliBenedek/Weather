package benedek.openweathermap;

import io.reactivex.rxjava3.core.Single;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;

public class OpenWeatherMapControllerTest
{
    private  OpenWeatherMapServiceFactory factory;
    private OpenWeatherMapService service;
    private OpenWeatherMapController controller;
    private TextField locationTextField;
    private ComboBox comboBox;
    private Text currentTemp;
    private ImageView currentIcon;
    private List<Text> dateTexts;
    private List<Text> tempTexts;
    private List<ImageView> iconImages;

    @BeforeClass
    public static void beforeClass() {
        com.sun.javafx.application.PlatformImpl.startup(()->{});
    }

    @Test
    public void initialize()
    {
        //given
        givenOpenWeatherMapController();
        doReturn("New York").when(controller.locationTextField).getText();
        doReturn(Single.never()).when(service).getCurrentWeather("New York", "imperial");
        doReturn(Single.never()).when(service).getWeatherForecast("New York", "imperial");

        //when
        controller.initialize();

        //then
        verify(locationTextField).setText("New York");
        verify(service).getCurrentWeather("New York", "imperial");
        verify(service).getWeatherForecast("New York", "imperial");
    }

    @Test
    public void go_Fahrenheit()
    {
        //given
        givenOpenWeatherMapController();
        doReturn("London").when(controller.locationTextField).getText();
        doReturn("Fahrenheit").when(controller.comboBox).getValue();
        doReturn(Single.never()).when(service).getCurrentWeather("London", "imperial");
        doReturn(Single.never()).when(service).getWeatherForecast("London", "imperial");

        //when
        controller.go(mock(ActionEvent.class));

        //then
        //verify(controller.service).getWeatherForecast(controller.locationTextField.getText(), "imperial");
        verify(service).getCurrentWeather("London", "imperial");
        verify(service).getWeatherForecast("London", "imperial");
    }

    @Test
    public void go_Celsius()
    {
        //given
        givenOpenWeatherMapController();
        doReturn("London").when(controller.locationTextField).getText();
        doReturn("Celsius").when(controller.comboBox).getValue();
        doReturn(Single.never()).when(service).getCurrentWeather("London", "metric");
        doReturn(Single.never()).when(service).getWeatherForecast("London", "metric");

        //when
        controller.go(mock(ActionEvent.class));

        //then
        verify(service).getCurrentWeather("London", "metric");
        verify(service).getWeatherForecast("London", "metric");
    }

    @Test
    public void callService_Imperial()
    {
        //given
        givenOpenWeatherMapController();
        doReturn(Single.never()).when(service).getCurrentWeather("London", "imperial");
        doReturn(Single.never()).when(service).getWeatherForecast("London", "imperial");

        //when
        controller.callService("London", "imperial");

        //then
        verify(service).getCurrentWeather("London", "imperial");
        verify(service).getWeatherForecast("London", "imperial");
    }

    @Test
    public void callService_Metric()
    {
        //given
        givenOpenWeatherMapController();
        doReturn(Single.never()).when(service).getCurrentWeather("London", "metric");
        doReturn(Single.never()).when(service).getWeatherForecast("London", "metric");

        //when
        controller.callService("London", "metric");

        //then
        verify(service).getCurrentWeather("London", "metric");
        verify(service).getWeatherForecast("London", "metric");
    }

    @Test
    public void onOpenWeatherMapFeed()
    {
        //given
        givenOpenWeatherMapController();
        OpenWeatherMapFeed feed = mock(OpenWeatherMapFeed.class);
        feed.main = mock(OpenWeatherMapFeed.Main.class);
        feed.weather = Arrays.asList(
                mock(OpenWeatherMapFeed.Weather.class),
                mock(OpenWeatherMapFeed.Weather.class));
        doReturn(50.0).when(feed.main).getTemperature();
        doReturn("http://openweathermap.org/img/wn/").when(feed.weather.get(0)).getIconUrl();
        doReturn("50\u00B0").when(controller.currentTemp).getText();
        doReturn(mock(Image.class)).when(controller.currentIcon).getImage();

        //when
        controller.onOpenWeatherMapFeed(feed);

        //then
        verify(controller.currentTemp).setText("50\u00B0");
        verify(controller.currentIcon).setImage(any(Image.class));
    }

    @Test
    public void onOpenWeatherMapForecast()
    {
        //given
        givenOpenWeatherMapController();
        OpenWeatherMapForecast forecast = mock(OpenWeatherMapForecast.class);
        OpenWeatherMapForecast.HourlyForecast hourlyForecast
                = mock(OpenWeatherMapForecast.HourlyForecast.class);
        hourlyForecast.weather = Arrays.asList(
                mock(OpenWeatherMapForecast.HourlyForecast.Weather.class),
                mock(OpenWeatherMapForecast.HourlyForecast.Weather.class));
        hourlyForecast.main = mock(OpenWeatherMapForecast.HourlyForecast.Main.class);
        hourlyForecast.main.temp = 50;
        //String date = (hourlyForecast.getDate().toString()).split(" ")[0];

        doReturn(hourlyForecast).when(forecast).getForecastFor(anyInt());
        doReturn(forecast.getForecastFor(anyInt()).getDate()).when(hourlyForecast).getDate();
        Date date = new Date();
        String[] splitDate = date.toString().split(" ");
        doReturn(date).when(hourlyForecast).getDate();
        doReturn("http://openweathermap.org/img/wn/").when(hourlyForecast.weather.get(0)).getIconUrl();
        for(int ix = 0; ix < controller.dateTexts.size(); ix++)
        {

            doReturn(splitDate[0] + " " + splitDate[1] + " " + splitDate[2]).when(dateTexts.get(ix)).getText();
            doReturn("50\u00B0").when(tempTexts.get(ix)).getText();
            doReturn(mock(Image.class)).when(iconImages.get(ix)).getImage();
        }

        //when
        controller.onOpenWeatherMapForecast(forecast);

        //then
        for(int ix = 0; ix < controller.dateTexts.size(); ix++)
        {
            verify(controller.dateTexts.get(ix)).setText(splitDate[0] + " " + splitDate[1] + " " + splitDate[2]);
            verify(controller.tempTexts.get(ix)).setText("50\u00B0");
            verify(controller.iconImages.get(ix)).setImage(any(Image.class));
        }
    }

    private void givenOpenWeatherMapController()
    {
        service = mock(OpenWeatherMapService.class);
        controller = new OpenWeatherMapController(service);
        
        locationTextField = mock(TextField.class);
        controller.locationTextField = locationTextField;
        
        comboBox = mock(ComboBox.class);
        controller.comboBox = comboBox;
        
        currentTemp = mock(Text.class);
        controller.currentTemp = currentTemp;
        
        currentIcon = mock(ImageView.class);
        controller.currentIcon = currentIcon;
        dateTexts = asList(
                mock(Text.class),
                mock(Text.class)
        );
        controller.dateTexts = dateTexts;
        
        tempTexts = asList(
                mock(Text.class),
                mock(Text.class)
        );
        controller.tempTexts = tempTexts;
        
        iconImages = asList(
                mock(ImageView.class),
                mock(ImageView.class)
        );
        controller.iconImages = iconImages;
    }
}