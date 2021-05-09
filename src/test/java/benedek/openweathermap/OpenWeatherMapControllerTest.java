package benedek.openweathermap;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
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
        OpenWeatherMapService service = mock(OpenWeatherMapService.class);
        OpenWeatherMapController controller = new OpenWeatherMapController(service);
        doReturn(Single.never()).when(service).getCurrentWeather("New York", "imperial");
        doReturn(Single.never()).when(service).getWeatherForecast("New York", "imperial");

        //when
        controller.initialize();

        //then
        verify(service).getCurrentWeather("New York", "imperial");
        verify(service).getWeatherForecast("New York", "imperial");
    }

    @Test
    public void go_Fahrenheit()
    {
        //given
        givenOpenWeatherMapController();
        Disposable disposable = mock(Disposable.class);
        doReturn("London").when(controller.locationTextField).getText();
        doReturn("Fahrenheit").when(controller.comboBox).getValue();
        String units = String.valueOf(controller.comboBox.getValue()).equals("Fahrenheit")
                ? "imperial"
                : "metric";

        //when
        controller.go(mock(ActionEvent.class));

        //then
        verify(controller.service).getWeatherForecast(controller.locationTextField.getText(), units)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(Schedulers.trampoline())
                                    .subscribe(this::onOpenWeatherMapForecast, this::onError);
    }

    @Test
    public void go_Celsius()
    {
        //given
        givenOpenWeatherMapController();
        Disposable disposable = mock(Disposable.class);
        doReturn("London").when(controller.locationTextField).getText();
        doReturn("Celsius").when(controller.comboBox).getValue();
        String units = String.valueOf(controller.comboBox.getValue()).equals("Fahrenheit")
                ? "imperial"
                : "metric";

        //when
        controller.go(mock(ActionEvent.class));

        //then
        verify(controller.service).getWeatherForecast(controller.locationTextField.getText(), units)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.trampoline())
                .subscribe(this::onOpenWeatherMapForecast, this::onError);
    }

    @Test
    public void onOpenWeatherMapForecast() throws FileNotFoundException
    {
        //given
        givenOpenWeatherMapController();
        OpenWeatherMapForecast forecast = mock(OpenWeatherMapForecast.class);
        doReturn("50\u00B0").when(forecast.list.get(0).main.temp);
        Image mockImage = mock(Image.class);
        doReturn(mockImage).when(new ImageView(forecast.list.get(0).weather.get(0).getIconUrl()));
        doReturn("49\u00B0").when(tempTexts.get(0).getText());
        doReturn("48\u00B0").when(tempTexts.get(1).getText());
        doReturn("52\u00B0").when(tempTexts.get(2).getText());
        doReturn("Wed May 05").when(dateTexts.get(0).getText());
        doReturn("Thu May 06").when(dateTexts.get(1).getText());
        doReturn("Fri May 07").when(dateTexts.get(2).getText());
        doReturn(mockImage).when(iconImages.get(0).getImage());
        doReturn(mockImage).when(iconImages.get(1).getImage());
        doReturn(mockImage).when(iconImages.get(2).getImage());

        //when
        controller.onOpenWeatherMapForecast(forecast);

        //then
        verify(controller.currentTemp).setText("50\u00B0");
        verify(controller.currentIcon).setImage(mockImage);
        verify(controller.dateTexts).get(0).setText("Wed May 05");
        verify(controller.dateTexts).get(1).setText("Thu May 06");
        verify(controller.dateTexts).get(2).setText("Fri May 07");
        verify(controller.tempTexts).get(0).setText("49\u00B0");
        verify(controller.tempTexts).get(1).setText("48\u00B0");
        verify(controller.tempTexts).get(2).setText("52\u00B0");
        verify(controller.iconImages).get(0).setImage(mockImage);
        verify(controller.iconImages).get(1).setImage(mockImage);
        verify(controller.iconImages).get(2).setImage(mockImage);

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
        dateTexts = Arrays.asList(
                mock(Text.class),
                mock(Text.class),
                mock(Text.class)
        );
        controller.dateTexts = dateTexts;
        
        tempTexts = Arrays.asList(
                mock(Text.class),
                mock(Text.class),
                mock(Text.class)
        );
        controller.tempTexts = tempTexts;
        
        iconImages = Arrays.asList(
                mock(ImageView.class),
                mock(ImageView.class),
                mock(ImageView.class)
        );
        controller.iconImages = iconImages;
    }
}