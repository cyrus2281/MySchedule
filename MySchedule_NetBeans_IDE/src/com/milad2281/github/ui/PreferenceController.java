/**
 *MySchedule
 *Author: Milad Mobini
 *Last Modified: 2021/3
 * GitHub: https://github.com/milad2281/MySchedule
 * License available at legal folder
 */
package com.milad2281.github.ui;

import com.milad2281.github.data.Option;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;

/**
 * This class is the controller for the preference page
 *
 * @author Milad Mobini
 */
public class PreferenceController {

    @FXML
    ChoiceBox<String> dateFomratBox;
    @FXML
    ChoiceBox<String> shortSizeBox;
    @FXML
    ChoiceBox<String> detailsSizeBox;
    @FXML
    ColorPicker futureColorPicker;
    @FXML
    ColorPicker pastColorPicker;
    @FXML
    ColorPicker todayColorPicker;
    @FXML
    ColorPicker tmwColorPicker;
    @FXML
    CheckBox boldToday;
    @FXML
    CheckBox popUp;

    /**
     * initialize the page by setting the values from option class
     */
    public void initialize() {

        futureColorPicker.setValue(javafx.scene.paint.Color.valueOf(Option.getInstance().getColorFuture()));
        pastColorPicker.setValue(javafx.scene.paint.Color.valueOf(Option.getInstance().getColorPast()));
        todayColorPicker.setValue(javafx.scene.paint.Color.valueOf(Option.getInstance().getColorToday()));
        tmwColorPicker.setValue(javafx.scene.paint.Color.valueOf(Option.getInstance().getColorTomorrow()));
        shortSizeBox.setValue(Option.getInstance().getShortSize().replaceAll("\\D", "") + "px");
        detailsSizeBox.setValue(Option.getInstance().getDetailSize().replaceAll("\\D", "") + "px");
        dateFomratBox.setValue(Option.getInstance().getDateFormat());
        if (!Option.getInstance().getTodayBold().equals("")) {
            boldToday.setSelected(true);
        } else {
            boldToday.setSelected(false);
        }
        if (Option.getInstance().isPopUp()) {
            popUp.setSelected(true);
        } else {
            popUp.setSelected(false);
        }
    }

    /**
     * write the values to the option class variables
     */
    public void setValues() {
        Option.getInstance().setColorPast(pastColorPicker.getValue().toString());
        Option.getInstance().setColorToday(todayColorPicker.getValue().toString());
        Option.getInstance().setColorTomorrow(tmwColorPicker.getValue().toString());
        Option.getInstance().setColorFuture(futureColorPicker.getValue().toString());
        Option.getInstance().setDateFormat(dateFomratBox.getValue());
        Option.getInstance().setDetailSize("-fx-font-size: " + detailsSizeBox.getValue().replaceAll("\\D", ""));
        Option.getInstance().setShortSize("-fx-font-size: " + shortSizeBox.getValue().replaceAll("\\D", ""));
        if (popUp.isSelected()) {
            Option.getInstance().setPopUp(true);
        } else {
            Option.getInstance().setPopUp(false);
        }
        Option.getInstance().setShortSize("-fx-font-size: " + shortSizeBox.getValue().replaceAll("\\D", ""));
        if (boldToday.isSelected()) {
            Option.getInstance().setTodayBold("-fx-font-weight: bold");
        } else {
            Option.getInstance().setTodayBold("");
        }
    }

    /**
     * Set the value to the default values
     */
    @FXML
    public void handleRestoreDefault() {
        futureColorPicker.setValue(javafx.scene.paint.Color.valueOf("#000000"));
        pastColorPicker.setValue(javafx.scene.paint.Color.valueOf("#FF0000"));
        todayColorPicker.setValue(javafx.scene.paint.Color.valueOf("#993300"));
        tmwColorPicker.setValue(javafx.scene.paint.Color.valueOf("#000080"));
        shortSizeBox.setValue("-fx-font-size: 16".replaceAll("\\D", "") + "px");
        detailsSizeBox.setValue("-fx-font-size: 16".replaceAll("\\D", "") + "px");
        dateFomratBox.setValue("MMMM dd, yyyy");
        popUp.setSelected(true);
        boldToday.setSelected(true);
    }
}
