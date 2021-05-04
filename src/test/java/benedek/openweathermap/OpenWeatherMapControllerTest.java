package benedek.openweathermap;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class OpenWeatherMapControllerTest
{
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
    }

    @Test
    public void go()
    {
    }

    @Test
    public void onOpenWeatherMapForecast()
    {
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
                mock(Text.class),
                mock(Text.class),
                mock(Text.class)
        );
        controller.dateTexts = dateTexts;

        tempTexts = Arrays.asList(
                mock(Text.class),
                mock(Text.class),
                mock(Text.class),
                mock(Text.class),
                mock(Text.class)
        );
        controller.tempTexts = tempTexts;

        iconImages = Arrays.asList(
                mock(ImageView.class),
                mock(ImageView.class),
                mock(ImageView.class),
                mock(ImageView.class),
                mock(ImageView.class)
        );
        controller.iconImages = iconImages;
    }
}