/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milad200281.github.ui;

import com.milad200281.github.commen.Option;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import com.milad200281.github.ui.AppController;
import com.milad200281.github.commen.TodoData;
import com.milad200281.github.commen.TodoItem;
import java.awt.Color;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 *
 * @author milad
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

    public void initialize() {

        futureColorPicker.setValue(javafx.scene.paint.Color.valueOf(Option.getInstance().getColorFuture()));
        pastColorPicker.setValue(javafx.scene.paint.Color.valueOf(Option.getInstance().getColorPast()));
        todayColorPicker.setValue(javafx.scene.paint.Color.valueOf(Option.getInstance().getColorToday()));
        tmwColorPicker.setValue(javafx.scene.paint.Color.valueOf(Option.getInstance().getColorTomorrow()));
        shortSizeBox.setValue(Option.getInstance().getShortSize().replaceAll("\\D", "")+"px");
        detailsSizeBox.setValue(Option.getInstance().getDetailSize().replaceAll("\\D", "")+"px");
        dateFomratBox.setValue(Option.getInstance().getDateFormat());
        if (!Option.getInstance().getTodayBold().equals("")) {
            boldToday.setSelected(true);
        } else {
            boldToday.setSelected(false);
        }
    }

    public void setValues() {
        Option.getInstance().setColorPast(pastColorPicker.getValue().toString());
        Option.getInstance().setColorToday(todayColorPicker.getValue().toString());
        Option.getInstance().setColorTomorrow(tmwColorPicker.getValue().toString());
        Option.getInstance().setColorFuture(futureColorPicker.getValue().toString());
        Option.getInstance().setDateFormat(dateFomratBox.getValue());
        Option.getInstance().setDetailSize("-fx-font-size: "+detailsSizeBox.getValue().replaceAll("\\D", ""));
        Option.getInstance().setShortSize("-fx-font-size: "+shortSizeBox.getValue().replaceAll("\\D", ""));
        if (boldToday.isSelected()) {
            Option.getInstance().setTodayBold("-fx-font-weight: bold");
        } else {
            Option.getInstance().setTodayBold("");
        }
    }

}
