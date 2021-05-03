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


import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

public class OpenWeatherMapController
{
    @FXML
    TextField locationTextField;

    @FXML
    ComboBox comboBox;

    @FXML
    Text currentTemp;

    @FXML
    ImageView currentIcon;

    @FXML
    List<Text> dateTexts;

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
        String units = String.valueOf(comboBox.getValue()).equals("Fahrenheit")
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
        currentTemp.setText(String.valueOf(forecast.list.get(0).main.temp));
        ImageView currImageView = new ImageView(forecast.list.get(0).weather.get(0).getIconUrl());
        currentIcon.setImage(currImageView.getImage());
        for(int ix = 0; ix < 5; ix++)
        {
            OpenWeatherMapForecast.HourlyForecast day = forecast.getForcastFor(ix+1);
            Date date = day.getDate();
            String[] splitDate = date.toString().split(" ");
            dateTexts.get(ix).setText(splitDate[0] + " " + splitDate[1] + " " + splitDate[2]);
            double temp = day.main.temp;
            tempTexts.get(ix).setText(String.valueOf(temp));
            ImageView imageView = new ImageView(day.weather.get(0).getIconUrl());
            iconImages.get(ix).setImage(imageView.getImage());
        }
    }


    public void onError(Throwable throwable) {
        JOptionPane.showMessageDialog(null,
                "An error occurred, please try again.");
    }
}
