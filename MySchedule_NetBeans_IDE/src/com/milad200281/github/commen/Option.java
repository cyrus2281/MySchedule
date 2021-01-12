package com.milad200281.github.commen;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
*MySchedule 
*Author: Milad Mobini
*Last Modified: 2021/1
* GitHub: https://github.com/milad200281/MySchedule
* License available at legal folder
*/
public class Option implements Serializable {

    private final long SerialVersionUID = 123456789L;

    private static Option instance = new Option();
    private static String filename = "option.dt";

    private static String colorPast;
    private static String colorToday;
    private static String colorTomorrow;
    private static String colorFuture;

    private static String todayBold;

    private static String shortSize;
    private static String detailSize;

    private static String dateFormat;
    
    private static boolean popUp;
    private static int todayItems;
    private static int tomorrowItems;

    private Option() {
        //these options will be saved into file
        colorPast = "#FF0000";
        colorToday = "#993300";
        colorTomorrow = "#000080";
        colorFuture = "#000000";
        todayBold = "-fx-font-weight: bold";
        shortSize = "-fx-font-size: 16";
        detailSize = "-fx-font-size: 16";
        dateFormat = "MMMM dd, yyyy";
        popUp = true;
        //these will not
        todayItems = 0;
        tomorrowItems = 0;
    }

    public static Option getInstance() {
        return instance;
    }

    public boolean loadOption() throws IOException {
        try (ObjectInputStream locFile = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
            boolean eof = false;
            while (!eof) {
                try {

                    String option = (String) locFile.readObject();
                    if (option != null) {
                        String[] opt = option.split(";");
                        colorPast = opt[0];
                        colorToday = opt[1];
                        colorTomorrow = opt[2];
                        colorFuture = opt[3];
                        todayBold = opt[4];
                        shortSize = opt[5];
                        detailSize = opt[6];
                        dateFormat = opt[7];
                        popUp = Boolean.parseBoolean(opt[8]);
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
                System.out.println("Options: "+instance.toString());
                locFile.writeObject(instance.toString());
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

    public String getTodayBold() {
        return todayBold;
    }

    public String getShortSize() {
        return shortSize;
    }

    public String getDetailSize() {
        return detailSize;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public int getTodayItems() {
        return todayItems;
    }

    public void setTodayItems(int todayItems) {
        Option.todayItems = todayItems;
    }

    public int getTomorrowItems() {
        return tomorrowItems;
    }

    public boolean isPopUp() {
        return popUp;
    }

    public  void setPopUp(boolean popUp) {
        Option.popUp = popUp;
    }

    public void setTomorrowItems(int tomorrowItems) {
        Option.tomorrowItems = tomorrowItems;
    }

    public void setDateFormat(String dateFormat) {
        Option.dateFormat = dateFormat;
    }

    public void setShortSize(String shortSize) {
        Option.shortSize = shortSize;
    }

    public void setDetailSize(String detailSize) {
        Option.detailSize = detailSize;
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

    public void setTodayBold(String todayFont) {
        Option.todayBold = todayFont;
    }

    @Override
    public String toString() {
        return colorPast + ";"
                + colorToday + ";"
                + colorTomorrow + ";"
                + colorFuture + ";"
                + todayBold + ";"
                + shortSize + ";"
                + detailSize + ";"
                + dateFormat+";"
                +popUp;
    }

}
