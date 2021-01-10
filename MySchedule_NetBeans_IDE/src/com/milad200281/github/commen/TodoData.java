package com.milad200281.github.commen;

import com.milad200281.github.commen.TodoItem;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import javafx.collections.FXCollections;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
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
import java.util.Optional;
import java.util.function.Predicate;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

public class TodoData {

    private static TodoData instance = new TodoData();
    private static String filename = "MyScheduleDataItems.msf";

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
         
        boolean notContain = true;
        for (TodoItem todo : todoItems) {
            if (item.equals(todo)) {
                notContain = false;
                break;
            }
        }
        if (notContain) {
            System.out.println(item.getShortDescription()+": was added to the list");
            todoItems.add(item);
        } else {
            System.out.println(item.getShortDescription()+": already exists");
        }
    }

    public boolean loadTodoItems() throws IOException {

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
            return true;
        } catch (IOException io) {
            System.out.println("File Corrupted\nIO Exception" + io.getMessage());
            return false;
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException " + e.getMessage());
            return false;

        }
    }

    public void storeTodoItems() throws IOException {
        synchronized (this) {
            try (ObjectOutputStream locFile = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)))) {
                for (TodoItem item : todoItems) {
                    locFile.writeObject(item);
                }
            }
        }
    }

    public void exportTodoItemsCSV(Path path) throws IOException {
        synchronized (this) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("MMMM d, yyyy");
            String append = "\"Short Description\",\"Details\",\"DeadLine\"\n";
            try (FileWriter locFile = new FileWriter(path.toString())) {
                locFile.write(append);
                for (TodoItem item : todoItems) {
                    append = "\"";
                    append += item.getShortDescription();
                    append += "\",\"";
                    append += item.getDetails();
                    append += "\",\"";
                    append += df.format(item.getDeadline());
                    append += "\"\n";
                    locFile.write(append);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

    }
    public void exportTodoItemsMSF(Path path, List<TodoItem> items) throws IOException {
        synchronized (this) {
            try (ObjectOutputStream locFile = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(path.toString())))) {
                for (TodoItem item : items) {
                    locFile.writeObject(item);
                }
            }
        }
    }

    public void importTodoItemsMSF(Path path) {
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

    public void MergeTodoItemsMSF(Path path) {
        try (ObjectInputStream locFile = new ObjectInputStream(new BufferedInputStream(new FileInputStream(path.toString())))) {
            boolean eof = false;
            while (!eof) {
                try {
                    TodoItem item = (TodoItem) locFile.readObject();
                    addTodoItem(item);
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

    public void deleteTodoItem(TodoItem item) {
        todoItems.remove(item);
    }

    public void editItem(TodoItem oldItem, TodoItem newItem) {
        oldItem.setShortDescription(newItem.getShortDescription());
        oldItem.setDetails(newItem.getDetails());
        oldItem.setDeadline(newItem.getDeadline());
    }
}
