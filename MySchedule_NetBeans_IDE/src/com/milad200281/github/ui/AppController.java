package com.milad200281.github.ui;

import com.milad200281.github.commen.Option;
import com.milad200281.github.commen.TodoData;
import com.milad200281.github.commen.TodoItem;
import com.sun.javafx.css.Style;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javax.swing.text.StyledEditorKit;

public class AppController {

    private List<TodoItem> todoItems;
    @FXML
    private ListView<TodoItem> todoListView;
    @FXML
    private TextArea itemDetailsTextArea;
    @FXML
    private Label deadlineLabel;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private ContextMenu listContextMenu;
    @FXML
    private ToggleButton filterToggleButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button editButton;

    private FilteredList<TodoItem> filteredList;
    SortedList<TodoItem> sortedList;

    private Predicate<TodoItem> wantAllItems;
    private Predicate<TodoItem> wantTodaysItems;

    public void initialize() {
        listContextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete");
        MenuItem editMenuItem = new MenuItem("Edit");
        MenuItem exportMenuItem = new MenuItem("Export");
        deleteMenuItem.setOnAction((event) -> {
            TodoItem item = todoListView.getSelectionModel().getSelectedItem();
            deleteItem(item);
        });

        editMenuItem.setOnAction((event) -> {
            TodoItem item = todoListView.getSelectionModel().getSelectedItem();
            editItem(item);
        });
        exportMenuItem.setOnAction((event) -> {
            TodoItem item = todoListView.getSelectionModel().getSelectedItem();
            Path savePath;
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Export " + item.getShortDescription());
            chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("MSF", "*.msf"));
            File file = chooser.showSaveDialog(mainBorderPane.getScene().getWindow());
            if (file != null) {
                savePath = file.toPath();
                System.out.println(savePath);
                new Thread(() -> {
                    try {
                        List<TodoItem> items = new ArrayList<TodoItem>();
                        items.add(item);
                        TodoData.getInstance().exportTodoItemsMSF(savePath, items);
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                }).start();
            } else {
                System.out.println("Chooser was cancelled");
            }

        });

        editButton.setDisable(true);
        deleteButton.setDisable(true);

        listContextMenu.getItems().addAll(editMenuItem, exportMenuItem, deleteMenuItem);

        todoListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TodoItem>() {
            @Override
            public void changed(ObservableValue<? extends TodoItem> observable, TodoItem oldValue, TodoItem newValue) {
                if (newValue != null) {
                    editButton.setDisable(false);
                    deleteButton.setDisable(false);

                    TodoItem item = todoListView.getSelectionModel().getSelectedItem();
                    itemDetailsTextArea.setText(item.getDetails());
                    itemDetailsTextArea.setStyle(Option.getInstance().getDetailSize());
                    todoListView.setStyle(Option.getInstance().getShortSize());
                    DateTimeFormatter df = DateTimeFormatter.ofPattern(Option.getInstance().getDateFormat());
                    deadlineLabel.setText(df.format(item.getDeadline()));
                } else {
                    editButton.setDisable(true);
                    deleteButton.setDisable(true);
                }
            }
        }
        );

        wantAllItems = new Predicate<TodoItem>() {
            @Override
            public boolean test(TodoItem todoItem) {
                return true;
            }
        };
        wantTodaysItems = new Predicate<TodoItem>() {
            @Override
            public boolean test(TodoItem todoItem) {
                return todoItem.getDeadline().equals(LocalDate.now());
            }
        };

        sorting();

        todoListView.getSelectionModel()
                .setSelectionMode(SelectionMode.SINGLE);
        todoListView.getSelectionModel()
                .selectFirst();

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
                                setStyle("");
                            } else if (item.getDeadline().isBefore(LocalDate.now().plusDays(1))) {
                                setTextFill(Color.valueOf(Option.getInstance().getColorToday()));
                                setStyle(Option.getInstance().getTodayBold());
                            } else if (item.getDeadline().equals(LocalDate.now().plusDays(1))) {
                                setTextFill(Color.valueOf(Option.getInstance().getColorTomorrow()));
                                setStyle("");
                            } else {
                                setTextFill(Color.valueOf(Option.getInstance().getColorFuture()));
                                setStyle("");

                            }
                        }
                    }
                };
                cell.emptyProperty().addListener(
                        (obs, wasEmpty, isNowEmpty) -> {
                            if (isNowEmpty) {
                                cell.setContextMenu(null);
                            } else {
                                cell.setContextMenu(listContextMenu);
                            }
                        }
                );
                return cell;
            }
        }
        );
    }

    public void sorting() {
        filteredList = new FilteredList<TodoItem>(TodoData.getInstance().getTodoItems(), wantAllItems);
        sortedList = new SortedList<TodoItem>(filteredList, (TodoItem o1, TodoItem o2) -> o1.getDeadline().compareTo(o2.getDeadline()));
        todoListView.setItems(sortedList);
    }

    @FXML
    public void handleKeyPressedItem(KeyEvent keyEvent) {
        TodoItem selectedItem = todoListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            if (keyEvent.getCode().equals(KeyCode.DELETE)) {
                System.out.println("Del");
                deleteItem(selectedItem);
            }
        }
        if (keyEvent.isControlDown() && (keyEvent.getCode() == KeyCode.R)) {
            System.out.println("Ctrl+R");
            editItem(selectedItem);
        }
    }

    @FXML
    public void handleKeyPressed(KeyEvent keyEvent) throws IOException {
        if (keyEvent.isControlDown() && (keyEvent.getCode() == KeyCode.E)) {
            System.out.println("Ctrl+E");
            handleExportMSF();
        }
        if (keyEvent.isControlDown() && (keyEvent.getCode() == KeyCode.I)) {
            System.out.println("Ctrl+I");
            handleImportMSF();
        }
        if (keyEvent.isControlDown() && (keyEvent.getCode() == KeyCode.M)) {
            System.out.println("Ctrl+M");
            handleMerge();

        }
        if (keyEvent.isControlDown() && (keyEvent.getCode() == KeyCode.N)) {
            System.out.println("Ctrl+N");
            showNewItemDialog();
        }
        if (keyEvent.isControlDown() && (keyEvent.getCode() == KeyCode.T)) {
            System.out.println("Ctrl+T");
            handleExportCSV();
        }
        if (keyEvent.getCode() == KeyCode.ESCAPE) {
            System.out.println("esp");
            handleExit();
        }
        if (keyEvent.isControlDown() && (keyEvent.getCode() == KeyCode.P)) {
            System.out.println("Ctrl+P");
            handlePreference();
        }
        if (keyEvent.isControlDown() && (keyEvent.getCode() == KeyCode.R)) {
            System.out.println("Ctrl+R");
            handleExportMultiple();
        }
    }

    @FXML
    public void handleClickListView() {
        TodoItem item = todoListView.getSelectionModel().getSelectedItem();
        itemDetailsTextArea.setText(item.getDetails());
        deadlineLabel.setText(item.getDeadline().toString());
    }

    public static void saveAll() {
        new Thread(() -> {
            try {
                TodoData.getInstance().storeTodoItems();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }).start();
    }

    public void deleteItem(TodoItem item) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Todo Item");
        alert.setHeaderText("Delete item: " + item.getShortDescription());
        alert.setContentText("Are you sure? Press OK to confirm, or cancel to back out.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            TodoData.getInstance().deleteTodoItem(item);
            saveAll();
        }
    }

    public void editItem(TodoItem oldItem) {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());

        dialog.setTitle("Edit Todo Item");
        dialog.setHeaderText("Use this dialog to edit a todo item");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("todoItemDialog.fxml"));

        try {

            dialog.getDialogPane().setContent(fxmlLoader.load());

        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.APPLY);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        final TodoItemDialogController controller = fxmlLoader.getController();
        controller.proccessEdit(oldItem);

        final Button btApply = (Button) dialog.getDialogPane().lookupButton(ButtonType.APPLY);
        btApply.addEventFilter(
                ActionEvent.ACTION,
                event -> {
                    if (controller.validation()) {
                        event.consume();
                    }
                }
        );

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.APPLY) {
            TodoData.getInstance().deleteTodoItem(oldItem);
            todoListView.getSelectionModel().select(controller.proccessResults());

            System.out.println("Apply pressed");
        } else {
            System.out.println("Cancel pressed");
        }

    }

    @FXML
    public void showNewItemDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());

        dialog.setTitle("Add New Todo Item");
        dialog.setHeaderText("Use this dialog to create a new todo item");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("todoItemDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        final TodoItemDialogController controller = fxmlLoader.getController();
        final Button btOk = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        btOk.addEventFilter(
                ActionEvent.ACTION,
                event -> {
                    if (controller.validation()) {
                        event.consume();
                    }
                }
        );

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            TodoItem newItem = controller.proccessResults();
            todoListView.getSelectionModel().select(newItem);
            System.out.println("OK pressed");

        } else {
            System.out.println("Cancel pressed");
        }

    }

    @FXML
    public void handleEditButton() {
        TodoItem item = todoListView.getSelectionModel().getSelectedItem();
        editItem(item);
    }

    public void handleDeleteButton() {
        TodoItem item = todoListView.getSelectionModel().getSelectedItem();
        deleteItem(item);
    }

    @FXML
    public void handleFilterButton() {
        TodoItem selectedItem = todoListView.getSelectionModel().getSelectedItem();

        if (filterToggleButton.isSelected()) {
            filteredList.setPredicate(wantTodaysItems);
            if (filteredList.isEmpty()) {
                itemDetailsTextArea.clear();
                deadlineLabel.setText("");
            } else if (filteredList.contains(selectedItem)) {
                todoListView.getSelectionModel().select(selectedItem);
            } else {
                todoListView.getSelectionModel().selectFirst();
            }
        } else {
            filteredList.setPredicate(wantAllItems);
            todoListView.getSelectionModel().select(selectedItem);
        }
    }

    @FXML
    public void handleExportMSF() throws IOException {
        Path savePath;
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Export File as MSF");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("MSF", "*.msf"));
        File file = chooser.showSaveDialog(mainBorderPane.getScene().getWindow());
        if (file != null) {
            savePath = file.toPath();
            System.out.println(savePath);
            new Thread(() -> {
                try {
                    TodoData.getInstance().exportTodoItemsMSF(savePath, TodoData.getInstance().getTodoItems());
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }).start();
        } else {
            System.out.println("Chooser was cancelled");
        }

    }

    @FXML
    public void handleExportCSV() throws IOException {
        Path savePath;
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Export File As CSV");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        File file = chooser.showSaveDialog(mainBorderPane.getScene().getWindow());
        if (file != null) {
            savePath = file.toPath();
            System.out.println(savePath);
            new Thread(() -> {
                try {
                    TodoData.getInstance().exportTodoItemsCSV(savePath);
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }).start();
        } else {
            System.out.println("Chooser was cancelled");
        }

    }

    @FXML
    public void handleImportMSF() throws IOException {
        Path loadPath;
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Import MSF file");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("MSF", "*.msf"));
        File file = chooser.showOpenDialog(mainBorderPane.getScene().getWindow());
        if (file != null) {
            loadPath = file.toPath();
            System.out.println(loadPath);
            TodoData.getInstance().importTodoItemsMSF(loadPath);
            sorting();
            saveAll();

        } else {
            System.out.println("Chooser was cancelled");
        }

    }

    @FXML
    public void handleMerge() throws IOException {
        Path loadPath;
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Merge MSF file");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("MSF", "*.msf"));
        File file = chooser.showOpenDialog(mainBorderPane.getScene().getWindow());
        if (file != null) {
            loadPath = file.toPath();
            System.out.println(loadPath);
            TodoData.getInstance().MergeTodoItemsMSF(loadPath);
            saveAll();
        } else {
            System.out.println("Chooser was cancelled");
        }

    }

    @FXML
    public void handleExportMultiple() {

        Dialog<ButtonType> Dialog = new Dialog<>();
        Dialog.initOwner(mainBorderPane.getScene().getWindow());

        Dialog.setTitle("Multiple Item export");
        Dialog.setHeaderText("Select items to export.");

        FXMLLoader fxmlOption = new FXMLLoader();
        fxmlOption.setLocation(getClass().getResource("selectiveExport.fxml"));

        try {
            Dialog.getDialogPane().setContent(fxmlOption.load());

        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }
        Dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Dialog.getDialogPane().getButtonTypes().add(ButtonType.NEXT);

        final SelectiveExportController controller = fxmlOption.getController();
        Optional<ButtonType> result = Dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.NEXT) {
            controller.getSelectedItems();
            Path savePath;
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Export selected items as MSF");
            chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("MSF", "*.msf"));
            File file = chooser.showSaveDialog(mainBorderPane.getScene().getWindow());
            if (file != null) {
                savePath = file.toPath();
                System.out.println(savePath);
                new Thread(() -> {
                    try {
                        TodoData.getInstance().exportTodoItemsMSF(savePath, controller.getSelectedItems());
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                }).start();
            } else {
                System.out.println("Chooser was cancelled");
            }

            System.out.println("Apply pressed");
        } else {
            System.out.println("Cancel pressed");
        }

    }

    @FXML
    public void handlePreference() {

        Dialog<ButtonType> OptionDialog = new Dialog<>();
        OptionDialog.initOwner(mainBorderPane.getScene().getWindow());

        OptionDialog.setTitle("Preferences");

        FXMLLoader fxmlOption = new FXMLLoader();
        fxmlOption.setLocation(getClass().getResource("preference.fxml"));

        try {
            OptionDialog.getDialogPane().setContent(fxmlOption.load());

        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }
        OptionDialog.getDialogPane().getButtonTypes().add(ButtonType.APPLY);
        OptionDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        final PreferenceController Optioncontroller = fxmlOption.getController();
        Optional<ButtonType> result = OptionDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.APPLY) {
            Optioncontroller.setValues();
            System.out.println("Apply pressed");
        } else {
            System.out.println("Cancel pressed");
        }

    }

    @FXML
    public void handleAbout() {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("About MySchdule");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("about.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Optional<ButtonType> result = dialog.showAndWait();

    }

    @FXML
    public void handleSupport() {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Help And Support");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("support.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Optional<ButtonType> result = dialog.showAndWait();

    }

    @FXML
    public void handleExit() {
        Platform.exit();
    }
}
