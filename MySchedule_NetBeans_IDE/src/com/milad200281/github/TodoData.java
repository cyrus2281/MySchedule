package com.milad200281.github;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import javafx.collections.FXCollections;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;

public class TodoData {

    private static TodoData instance = new TodoData();
    private static String filename = "TodoListItems.msv1";

    private ObservableList<TodoItem> todoItems;
    private DateTimeFormatter formatter;

    public static TodoData getInstance() {
        return instance;
    }

    private TodoData() {
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    public ObservableList<TodoItem> getTodoItems() {
        return todoItems;
    }

    public void addTodoItem(TodoItem item) {
        todoItems.add(item);
    }

    public void loadTodoItems() throws IOException {

        todoItems = FXCollections.observableArrayList();
        try (ObjectInputStream locFile = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
            boolean eof = false;
            while (!eof) {
                try {
                    TodoItem item = (TodoItem) locFile.readObject();

                    String shortDescription = item.getShortDescription();
                    String details = item.getDetails();
                    LocalDate date = item.getDeadline();
                    TodoItem todoItem = new TodoItem(shortDescription, details, date);
                    todoItems.add(todoItem);
                } catch (EOFException e) {
                    eof = true;
                }
            }
        } catch (IOException io) {
            System.out.println("IO Exception" + io.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException " + e.getMessage());
        }
    }

    public void storeTodoItems() throws IOException {
        try (ObjectOutputStream locFile = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)))) {
            for (TodoItem item : todoItems) {
                locFile.writeObject(item);
            }
        }
    }

    public void exportTodoItemsCSV() {

    }

    /*
            //folder
        /*
    DirectoryChooser chooser = new DirectoryChooser();
    File file = chooser.showDialog(gridPane.getScene().getWindow());
    if(file != null){
        System.out.println(file.getPath());
}else{
        System.out.println("Chooser was cancelled");
    }
     *//*
        //file
        FileChooser chooser = new FileChooser();
//    chooser.showOpenDialog(gridPane.getScene().getWindow()); //this prevent the user from doing anything else
//        chooser.showOpenMultipleDialog(gridPane.getScene().getWindow()); // to open multiple files, returns an array (file.size();)
        //to save file
        chooser.setTitle("Save App File");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text", "*.txt"), //to add extension filter
                new FileChooser.ExtensionFilter("PDF", "*.pdf"),
                new FileChooser.ExtensionFilter("My Type", "*.mbn", "*.mld", "*.png"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));

        File file = chooser.showSaveDialog(gridPane.getScene().getWindow());
        if (file != null) {
            System.out.println(file.getPath());
        } else {
            System.out.println("Chooser was cancelled");
        }
    
     */
    public void exportTodoItemsMSV1(Path path) throws IOException {

        try (ObjectOutputStream locFile = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(path.toString())))) {
            for (TodoItem item : todoItems) {
                locFile.writeObject(item);
            }
        }
    }

    public void importTodoItemsMSV1(Path path) {
        todoItems.clear();
        todoItems = FXCollections.observableArrayList();
        try (ObjectInputStream locFile = new ObjectInputStream(new BufferedInputStream(new FileInputStream(path.toString())))) {
            boolean eof = false;
            while (!eof) {
                try {
                    TodoItem item = (TodoItem) locFile.readObject();

                    String shortDescription = item.getShortDescription();
                    String details = item.getDetails();
                    LocalDate date = item.getDeadline();
                    TodoItem todoItem = new TodoItem(shortDescription, details, date);
                    todoItems.add(todoItem);
                } catch (EOFException e) {
                    eof = true;
                }
            }
        } catch (IOException io) {
            System.out.println("IO Exception" + io.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException " + e.getMessage());
        }

    }

    public void MergeTodoItemsMSV1(Path path) {
        try (ObjectInputStream locFile = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
            boolean eof = false;
            ArrayList<TodoItem> items = new ArrayList<TodoItem>();
            while (!eof) {
                try {
                    TodoItem item = (TodoItem) locFile.readObject();
                    String shortDescription = item.getShortDescription();
                    String details = item.getDetails();
                    LocalDate date = item.getDeadline();
                    TodoItem todoItem = new TodoItem(shortDescription, details, date);
                    items.add(todoItem);
                } catch (EOFException e) {
                    eof = true;
                }
            }
            for (TodoItem item : items) {
                if (todoItems.containsAll(items)) {
                    System.out.println("it does contain");
                } else {
                    System.out.println("It does not");
                }
            }
        } catch (IOException io) {
            System.out.println("IO Exception" + io.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException " + e.getMessage());

        }
    }

    public void deleteTodoItem(TodoItem item) {
        todoItems.remove(item);
    }

    public void editItem(TodoItem oldItem, TodoItem newItem) {
        oldItem.setShortDescription(newItem.getShortDescription());
        oldItem.setDetails(newItem.getDetails());
        oldItem.setDeadline(newItem.getDeadline());
    }
}
