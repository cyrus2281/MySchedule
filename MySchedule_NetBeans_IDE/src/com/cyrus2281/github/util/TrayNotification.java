/**
 *MySchedule
 *Author: Cyrus Mobini
 *Last Modified: 2021/3
 * GitHub: https://github.com/cyrus2281/MySchedule
 * License available at legal folder
 */
package com.cyrus2281.github.util;

import java.awt.*;
import java.awt.TrayIcon.MessageType;

/**
 * This is class is responsible for pushing notification to windows tray
 * @author Cyrus Mobini
 */
public class TrayNotification {
    private String titleNotif;
    private String detailNotif;

    public TrayNotification(String titleNotif, String detailNotif) {
        this.titleNotif = titleNotif;
        this.detailNotif = detailNotif;
    }

    public void setTitleNotif(String titleNotif) {
        this.titleNotif = titleNotif;
    }

    public void setDetailNotif(String detailNotif) {
        this.detailNotif = detailNotif;
    }
    public void setNotification(String titleNotif,String detailNotif) {
        this.titleNotif = titleNotif;
        this.detailNotif = detailNotif;
    }
/*
    public  void showNotification() throws AWTException {
        if (SystemTray.isSupported()) {
            TrayNotification td = new TrayNotification("","");
            td.displayTray();
        } else {
            System.err.println("System tray not supported!");
        }
    }*/

    public void displayTray() throws AWTException {
        // Obtain only one instance of the SystemTray object
        SystemTray tray = SystemTray.getSystemTray();

        // If the icon is a file
        Image image = Toolkit.getDefaultToolkit().createImage( 
                "src\\com\\cyrus2281\\github\\icons\\MyScheduleLogo.png");
        TrayIcon trayIcon = new TrayIcon(image, "MySchedule");
        // Let the system resize the image if needed
        trayIcon.setImageAutoSize(true);
        // Set tooltip text for the tray icon
        trayIcon.setToolTip("MySchedule Notification");
        tray.add(trayIcon);

        trayIcon.displayMessage(titleNotif, detailNotif, MessageType.NONE);

    }
}