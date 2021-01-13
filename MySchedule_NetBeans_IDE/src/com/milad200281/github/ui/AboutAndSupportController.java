package com.milad200281.github.ui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * MySchedule Author: Milad Mobini Last Modified: 2021/1 GitHub:
 * https://github.com/milad200281/MySchedule License available at legal folder
 */
public class AboutAndSupportController {

    @FXML
    private Label msLabel;
    
    public void initialize() {
        
        Image img = new Image("/com/milad200281/github/icons/MyScheduleLogo.png");
        ImageView view = new ImageView(img);
        msLabel.setGraphic(view);
    }

    @FXML
    public void handleLinkLicense() {

        try {
            Desktop.getDesktop().browse(new URI("https://github.com/milad200281/MySchedule/blob/main/License.md"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void handleLinkGithub() {

        try {
            Desktop.getDesktop().browse(new URI("https://github.com/milad200281/MySchedule"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

    }
}
