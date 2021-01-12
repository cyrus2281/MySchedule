package com.milad200281.github.ui;

import com.milad200281.github.ui.AppController;
import com.milad200281.github.commen.TodoData;
import com.milad200281.github.commen.TodoItem;
import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
/**
*MySchedule 
*Author: Milad Mobini
*Last Modified: 2021/1
* GitHub: https://github.com/milad200281/MySchedule
* License available at legal folder
*/
public class TodoItemDialogController {

    @FXML
    private TextField shortDescriptionField;
    @FXML
    private TextArea detailsArea;
    @FXML
    private DatePicker deadlinePicker;
    @FXML
    private Label shortDiscriptionLabel;
    @FXML
    private Label deadlineLabel;

    public TodoItem proccessResults() {
        

        String shortDescription = shortDescriptionField.getText().trim();
        String details;
        if (detailsArea.getText() != null && !detailsArea.getText().trim().equals("")) {
            details = detailsArea.getText().trim();
        } else {
            details = "";
        }
        LocalDate deadlineValue = deadlinePicker.getValue();

        TodoItem newItem = new TodoItem(shortDescription, details, deadlineValue);
        TodoData.getInstance().addTodoItem(newItem);
        AppController.saveAll();
        return newItem;

    }

    public void proccessEdit(TodoItem item) {

        shortDescriptionField.setText(item.getShortDescription());
        detailsArea.setText(item.getDetails());
        deadlinePicker.setValue(item.getDeadline());

        System.out.println("proccess edit work");

    }
    
    public boolean validation() {
        deadlineLabel.setText("");
        shortDiscriptionLabel.setText("");
        boolean valid = true;
        System.out.println("validation invoked");
        if (!(shortDescriptionField.getText() != null && !shortDescriptionField.getText().trim().equals(""))) {
            valid = false;
            shortDiscriptionLabel.setText("This area can not be empty!");
        }
        
        if (!(deadlinePicker.getValue() != null)) {
            valid = false;
            deadlineLabel.setText("A data should be picked!");
        }

        return !valid;
    }
}
