/**
 *MySchedule
 *Author: Milad Mobini
 *Last Modified: 2021/3
 * GitHub: https://github.com/milad2281/MySchedule
 * License available at legal folder
 */
package com.milad2281.github.ui;

import com.milad2281.github.ui.AppController;
import com.milad2281.github.data.TodoData;
import com.milad2281.github.data.TodoItem;
import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * This is the controller class for the new item/edit item page
 *
 * @author Milad Mobini
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

    /**
     * create and adds a new item to the list in TodoData
     *
     * @return the newly created item
     */
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

    /**
     * set the texts as to edit the item
     *
     * @param item that is going to be edited
     */
    public void proccessEdit(TodoItem item) {
        shortDescriptionField.setText(item.getShortDescription());
        detailsArea.setText(item.getDetails());
        deadlinePicker.setValue(item.getDeadline());
        System.out.println("proccess edit work");
    }

    /**
     * Checks if short description and deadline are filled, and print error text
     * if not
     *
     * @return true if both short description and deadline are filled, false if
     * not
     */
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
