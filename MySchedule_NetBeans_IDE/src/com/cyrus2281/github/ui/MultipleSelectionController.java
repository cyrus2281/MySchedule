/**
 *MySchedule
 *Author: Cyrus Mobini
 *Last Modified: 2021/3
 * GitHub: https://github.com/cyrus2281/MySchedule
 * License available at legal folder
 */
package com.cyrus2281.github.ui;

import com.cyrus2281.github.data.Option;
import com.cyrus2281.github.data.TodoData;
import com.cyrus2281.github.data.TodoItem;
import java.time.LocalDate;
import java.util.ArrayList;
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
 * This class is a controller for the Multiple selection page
 *
 * @author Cyrus Mobini
 */
public class MultipleSelectionController {

    @FXML
    private ListView<TodoItem> todoListView;
    @FXML
    private Label labelText;

    /**
     * initialize the page by printing the items to the view list in date order
     */
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

    /**
     * @return a list of all the selected items
     */
    public ArrayList<TodoItem> getSelectedItems() {
        ArrayList<TodoItem> lists = new ArrayList<>();
        for (TodoItem item : todoListView.getSelectionModel().getSelectedItems()) {
            lists.add(item);
        }
        return lists;
    }

    /**
     * Changes the label text based on the number of selected items
     */
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

    /**
     * validate if any item has been selected
     *
     * @return true if at least one item is selected, false if non is selected
     */
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
