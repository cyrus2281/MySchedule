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
        popUpNotification();
    }

    @Override
    public void init() throws Exception {
        try {
            Option.getInstance().loadOption();
            TodoData.getInstance().loadTodoItems();
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

    public void popUpNotification() {
        if (Option.getInstance().isPopUp()) {
            if (TodoData.getInstance().getTodoItems().size() > 1) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("MySchedule");
                alert.setHeaderText("Welcome Back.");
                String strOne = "There ";
                String strTwo = "There ";
                int numOne = Option.getInstance().getTodayItems();
                int numTwo = Option.getInstance().getTomorrowItems();
                if (numOne == 0) {
                    strOne += "is no item due to today.";
                } else if (numOne == 1) {
                    strOne += "is 1 item due to today.";
                } else {
                    strOne += "are " + numOne + " items due to today.";
                }
                if (numTwo == 0) {
                    strTwo += "is no item due to tomorrow.";
                } else if (numTwo == 1) {
                    strTwo += "is 1 item due to tomorrow.";
                } else {
                    strTwo += "are " + numTwo + " item due to tomorrow.";
                }
                alert.setContentText(strOne + "\n" + strTwo);
                Optional<ButtonType> result = alert.showAndWait();
            }
        }
    }
}
