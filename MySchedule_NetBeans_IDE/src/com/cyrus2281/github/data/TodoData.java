/**
 *MySchedule
 *Author: Cyrus Mobini
 *Last Modified: 2021/3
 * GitHub: https://github.com/cyrus2281/MySchedule
 * License available at legal folder
 */
package com.cyrus2281.github.data;

import com.cyrus2281.github.data.TodoItem;
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

/**
 * This class handle the main data used in the application
 *
 * @author Cyrus Mobini
 */
public class TodoData {

    private static TodoData instance = new TodoData();
    private static String filename = "MyScheduleDataItems.msf";

    private ObservableList<TodoItem> todoItems;
    private DateTimeFormatter formatter;
    private TodoItem firstTodayItem;

    /**
     * @return an instance of the class variable
     */
    public static TodoData getInstance() {
        return instance;
    }

    /**
     * default constructor setting the date format
     */
    private TodoData() {
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    /**
     * @return the items list
     */
    public ObservableList<TodoItem> getTodoItems() {
        return todoItems;
    }

    /**
     * adds the item to the list if it is not already there
     *
     * @param item item that want to be added to the list
     */
    public void addTodoItem(TodoItem item) {

        boolean notContain = true;
        for (TodoItem todo : todoItems) {
            if (item.equals(todo)) {
                notContain = false;
                break;
            }
        }
        if (notContain) {
            System.out.println(item.getShortDescription() + ": was added to the list");
            todoItems.add(item);
        } else {
            System.out.println(item.getShortDescription() + ": already exists");
        }
    }

    /**
     * This method will read the list from the file
     *
     * @return true on succuss, false on failure
     * @throws IOException
     */
    public boolean loadTodoItems() throws IOException {

        todoItems = FXCollections.observableArrayList();
        try (ObjectInputStream locFile = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
            boolean eof = false;
            int todayNum = 0;
            int tmwNum = 0;

            while (!eof) {
                try {

                    TodoItem item = (TodoItem) locFile.readObject();
                    todoItems.add(item);
                    if (item.getDeadline().isEqual(LocalDate.now())) {
                        todayNum++;
                        if (firstTodayItem == null) {
                            firstTodayItem = item;
                        }
                    } else if (item.getDeadline().equals(LocalDate.now().plusDays(1))) {
                        tmwNum++;
                    }
                    Option.getInstance().setTodayItems(todayNum);
                    Option.getInstance().setTomorrowItems(tmwNum);
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

    /**
     * This method will write the list to a file
     *
     * @throws IOException
     */
    public void storeTodoItems() throws IOException {
        synchronized (this) {
            try (ObjectOutputStream locFile = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)))) {
                for (TodoItem item : todoItems) {
                    locFile.writeObject(item);
                }
            }
        }
    }

    /**
     * This method will write all the list in a CSV format
     *
     * @param path the path to where it's going to be exported
     * @throws IOException
     */
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

    /**
     * This method will export a list of items to a file
     *
     * @param path path to where it's going to be exported
     * @param items The list of the item that are going to be exported
     * @throws IOException
     */
    public void exportTodoItemsMSF(Path path, List<TodoItem> items) throws IOException {
        synchronized (this) {
            try (ObjectOutputStream locFile = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(path.toString())))) {
                for (TodoItem item : items) {
                    locFile.writeObject(item);
                }
            }
        }
    }

    /**
     * This method will import items from a file by over-writing the existing
     * list
     *
     * @param path path of the existing file
     */
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

    /**
     * This method will merge items from a file by adding them to the existing
     * list
     *
     * @param path path of the existing file
     */
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

    /**
     * Delete an item from the list
     *
     * @param item item to be deleted
     */
    public void deleteTodoItem(TodoItem item) {
        todoItems.remove(item);
    }

    /**
     * delete all items from the list
     */
    public void deleteAll() {
        todoItems.clear();
    }

    /**
     * returns, if any, the first item that is due to today
     *
     * @return item due to today
     */
    public TodoItem getFirstTodayItem() {
        return firstTodayItem;
    }
}
