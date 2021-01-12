/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milad200281.github.ui;

import com.milad200281.github.commen.Option;
import com.milad200281.github.commen.TodoData;
import com.milad200281.github.commen.TodoItem;
import java.time.LocalDate;
import java.util.List;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.paint.Color;
import javafx.util.Callback;

/**
*MySchedule 
*Author: Milad Mobini
*Last Modified: 2021/1
* GitHub: https://github.com/milad200281/MySchedule
* License available at legal folder
*/
public class SelectiveExportController {

    @FXML
    private ListView<TodoItem> todoListView;
    @FXML
    private Label labelText;

    public void initialize() {
        todoListView.getSelectionModel()
                .setSelectionMode(SelectionMode.MULTIPLE);

        todoListView.setCellFactory(
                new Callback<ListView<TodoItem>, ListCell<TodoItem>>() {
            @Override
            public ListCell<TodoItem> call(ListView<TodoItem> param
            ) {
                ListCell<TodoItem> cell = new ListCell<TodoItem>() {
                    @Override
                    protected void updateItem(TodoItem item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(item.getShortDescription());
                            if (item.getDeadline().isBefore(LocalDate.now().plusDays(0))) {
                                setTextFill(Color.valueOf(Option.getInstance().getColorPast()));

                            } else if (item.getDeadline().isBefore(LocalDate.now().plusDays(1))) {
                                setTextFill(Color.valueOf(Option.getInstance().getColorToday()));
                                setStyle(Option.getInstance().getTodayBold());
                            } else if (item.getDeadline().equals(LocalDate.now().plusDays(1))) {
                                setTextFill(Color.valueOf(Option.getInstance().getColorTomorrow()));

                            } else {
                                setTextFill(Color.valueOf(Option.getInstance().getColorFuture()));

                            }
                        }
                    }
                };
                return cell;
            }
        }
        );

        FilteredList<TodoItem> filteredList = new FilteredList<>(TodoData.getInstance().getTodoItems(), (TodoItem todoItem) -> true);
        SortedList<TodoItem> sortedList = new SortedList<>(filteredList, (TodoItem o1, TodoItem o2) -> o1.getDeadline().compareTo(o2.getDeadline()));

        todoListView.setItems(sortedList);
    }

    public List<TodoItem> getSelectedItems() {
        return todoListView.getSelectionModel().getSelectedItems();
    }

    @FXML
    public void onMouseClick() {
        labelText.setTextFill(Color.BLACK);
        labelText.setStyle("-fx-font-size: 16");
        int num = todoListView.getSelectionModel().getSelectedItems().size();
        if (num == 0) {
            labelText.setText("No item selected");
        } else if (num == 1) {
            labelText.setText("1 item selected");
        } else {
            labelText.setText(num + " items selected");
        }
    }

    public boolean checkValidation() {
        labelText.setText("");
        labelText.setStyle("-fx-font-size: 22");
        if (todoListView.getSelectionModel().getSelectedItems().size() > 0) {
            return false;
        } else {
            labelText.setTextFill(Color.RED);
            labelText.setText("No item was selected!");
            return true;
        }
    }
}
