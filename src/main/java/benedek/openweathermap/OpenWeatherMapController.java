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


    // Dependency injection
    public OpenWeatherMapController(OpenWeatherMapService service)
    {
        this.service = service;
    }

    public OpenWeatherMapController()
    {
        service = new OpenWeatherMapServiceFactory().newInstance();
    }

    @FXML
    public void initialize()
    {
        locationTextField.setText("New York");
        Disposable disposable = service.getWeatherForecast("New York", "imperial")
                // request the data in the background
                .subscribeOn(Schedulers.io())
                // work with the data in the foreground
                .observeOn(Schedulers.trampoline())
                // work with the feed whenever it gets downloaded
                .subscribe(this::onOpenWeatherMapForecast, this::onError);
    }

    public void go(ActionEvent actionEvent)
    {
        String location = locationTextField.getText();
        String units = String.valueOf(comboBox.getValue()).equals("Fahrenheit")
                ? "imperial"
                : "metric";


    }

    public void onOpenWeatherMapForecast(OpenWeatherMapForecast forecast) throws FileNotFoundException
    {
        currentTemp.setText(String.format("%.0f\u00B0", forecast.list.get(0).main.temp));
        ImageView currImageView = new ImageView(forecast.list.get(0).weather.get(0).getIconUrl());
        currentIcon.setImage(currImageView.getImage());
        for(int ix = 0; ix < 5; ix++)
        {
            OpenWeatherMapForecast.HourlyForecast day = forecast.getForcastFor(ix+1);
            Date date = day.getDate();
            String[] splitDate = date.toString().split(" ");
            dateTexts.get(ix).setText(splitDate[0] + " " + splitDate[1] + " " + splitDate[2]);
            double temp = day.main.temp;
            tempTexts.get(ix).setText(String.format("%.0f\u00B0", temp));
            ImageView imageView = new ImageView(day.weather.get(0).getIconUrl());
            iconImages.get(ix).setImage(imageView.getImage());
        }
    }

    public void onError(Throwable throwable) {
        JOptionPane.showMessageDialog(null,
                "An error occurred, please try again.");
    }
}
