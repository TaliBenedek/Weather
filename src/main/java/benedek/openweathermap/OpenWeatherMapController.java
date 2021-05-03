package benedek.openweathermap;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class OpenWeatherMapController
{
    @FXML
    TextField locationTextField;

    @FXML
    ComboBox comboBox;

    @FXML
    List<Text> tempTexts;

    @FXML
    List<ImageView> iconImages;

    OpenWeatherMapService service;

    @FXML
    public void initialize()
    {
        OpenWeatherMapServiceFactory factory = new OpenWeatherMapServiceFactory();
        service = factory.newInstance();
    }

    public void go(ActionEvent actionEvent)
    {
        String location = locationTextField.getText();
        String units;

        units = String.valueOf(comboBox.getValue()).equals("Fahrenheit")
                ? "imperial"
                : "metric";

        Disposable disposable = service.getWeatherForecast(location, units)
                // request the data in the background
                .subscribeOn(Schedulers.io())
                // work with the data in the foreground
                .observeOn(Schedulers.trampoline())
                // work with the feed whenever it gets downloaded
                .subscribe(this::onOpenWeatherMapFeed, this::onError);
    }

    private void onOpenWeatherMapFeed(OpenWeatherMapForecast forecast) throws FileNotFoundException
    {
        for(int ix = 0; ix < 5; ix++)
        {
            OpenWeatherMapForecast.HourlyForecast day = forecast.getForcastFor(ix+1);
            double temp = day.main.temp;
            tempTexts.get(ix).setText(String.valueOf(temp));
            //Image image = new Image(new FileInputStream(day.weather.get(0).getIconUrl()));
            ImageView imageView = new ImageView(day.weather.get(0).getIconUrl());
            iconImages.get(ix).setImage(imageView.getImage());
        }
    }


    public void onError(Throwable throwable) {
        // this is not the correct way to handle errors
        System.out.println("error occurred");
    }
}
