
package com.milad200281.github;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;


public class aboutController {
    
    
    @FXML
    public void handleLinkLicense(){
  
        try{
            Desktop.getDesktop().browse(new URI("https://github.com/milad200281/MySchedule/blob/main/README.md"));
        }catch(IOException | URISyntaxException e){
            e.printStackTrace();
        }

    }
    
    @FXML
    public void handleLinkGithub(){
  
        try{
            Desktop.getDesktop().browse(new URI("https://github.com/milad200281/MySchedule"));
        }catch(IOException | URISyntaxException e){
            e.printStackTrace();
        }

    }
}
