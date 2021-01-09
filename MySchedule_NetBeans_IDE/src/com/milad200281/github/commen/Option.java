/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milad200281.github.commen;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author milad
 */
public class Option {

    private static Option instance = new Option();
    private static String filename = "option.dt";

    private static String colorPast;
    private static String colorToday;
    private static String colorTomorrow;
    private static String colorFuture;
    private static String todayFont;

    private static DateTimeFormatter dateFormater;

    private Option() {
        colorPast = "#FF0000";
        colorToday = "#993300";
        colorTomorrow = "#000080";
        colorFuture = "#000000";
        todayFont = "-fx-font-weight: bold";
        dateFormater = DateTimeFormatter.ofPattern("MMMM d, yyyy");

    }

    public static Option getInstance() {
        return instance;
    }

    
    public boolean loadOption() throws IOException {
        try (ObjectInputStream locFile = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
            boolean eof = false;
            while (!eof) {
                try {
                     Option option = (Option) locFile.readObject();
                     if (option != null){
                         instance =  option;
                     }

                } catch (EOFException e) {
                    eof = true;
                }
            } 
            return true;
        } catch (IOException io) {
            System.out.println("File Corrupted\nIO Exception" + io.getMessage());
            return false;
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException " + e.getMessage());
            return false;

        }
    }

    public void saveOption() throws IOException {
        synchronized (this) {
            try (ObjectOutputStream locFile = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)))) {
                    locFile.writeObject(instance);
            }
        }
    }
     
    public String getColorPast() {
        return colorPast;
    }

    public String getColorToday() {
        return colorToday;
    }

    public String getColorTomorrow() {
        return colorTomorrow;
    }

    public String getColorFuture() {
        return colorFuture;
    }

    public DateTimeFormatter getDateFormater() {
        return dateFormater;
    }

    public String getTodayFont() {
        return todayFont;
    }

    public void setColorPast(String colorPast) {
        Option.colorPast = colorPast;
    }

    public void setColorToday(String colorToday) {
        Option.colorToday = colorToday;
    }

    public void setColorTomorrow(String colorTomorrow) {
        Option.colorTomorrow = colorTomorrow;
    }

    public void setColorFuture(String colorFuture) {
        Option.colorFuture = colorFuture;
    }

    public void setTodayFont(String todayFont) {
        Option.todayFont = todayFont;
    }

    public void setDateFormater(DateTimeFormatter dateFormater) {
        Option.dateFormater = dateFormater;
    }

}
