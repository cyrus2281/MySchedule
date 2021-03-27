/**
 * MySchedule Author: Milad Mobini
 * Last Modified: 2021/3 GitHub:
 * https://github.com/milad2281/MySchedule
 * License available at legal folder
 */
package com.milad2281.github.ui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * this class is the controller for the pages about and support
 *
 * @author Milad Mobini
 *
 */
public class AboutAndSupportController {

    @FXML
    private Label msLabel;

    /**
     * Initial the page with the logo
     */
    public void initialize() {

        Image img = new Image("/com/milad2281/github/icons/MyScheduleLogo.png");
        ImageView view = new ImageView(img);
        msLabel.setGraphic(view);
    }

    /**
     * Open the license GitHub page in the default browser
     */
    @FXML
    public void handleLinkLicense() {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/milad2281/MySchedule/blob/main/License.md"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * Open the GitHub page in the default browser
     */
    @FXML
    public void handleLinkGithub() {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/milad2281/MySchedule"));
        } catch (IOException | URISyntaxException e) {
            System.out.println(e.getMessage());
        }
    }
}
