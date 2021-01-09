package com.milad200281.github.ui;

import com.milad200281.github.commen.Option;
import com.milad200281.github.commen.TodoData;
import java.io.IOException;
import java.util.Optional;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 *
 * @author milad
 */
public class Main extends Application {

    //The Application
    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("application.fxml"));
        //this line will choose the template
        setUserAgentStylesheet(STYLESHEET_MODENA);
        primaryStage.setTitle("MySchedule");
        primaryStage.setScene(new Scene(root, 900, 500));
        primaryStage.show();
    }

    @Override
    public void init() throws Exception {
        try {
            Option.getInstance().loadOption();
            TodoData.getInstance().loadTodoItems();
            /*
            try {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Todo Item");
                alert.setHeaderText("Delete item: ");
                alert.setContentText("Are you sure? Press OK to confirm, or cancel to back out.");
                Optional<ButtonType> result = alert.showAndWait();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }*/
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        try {
            Option.getInstance().saveOption();
            TodoData.getInstance().storeTodoItems();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
