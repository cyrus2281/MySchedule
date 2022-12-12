/**
 *MySchedule
 *Author: Cyrus Mobini
 *Last Modified: 2021/3
 * GitHub: https://github.com/cyrus2281/MySchedule
 * License available at legal folder
 */
package com.cyrus2281.github.data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * This class is to create to-do items
 *
 * @author Cyrus Mobini
 */
public class TodoItem implements Serializable {

    private final long SerialVersionUID = 123456789L;

    private String shortDescription;
    private String details;

    private LocalDate deadline;

    /**
     * @param shortDescription a short description of the item
     * @param details the complete details of the item
     * @param deadline the due date of the item
     */
    public TodoItem(String shortDescription, String details, LocalDate deadline) {
        this.shortDescription = shortDescription;
        this.details = details;
        this.deadline = deadline;
    }

    /**
     * @return the short description of the item
     */
    public String getShortDescription() {
        return shortDescription;
    }

    /**
     * @param shortDescription a short description for the item
     */
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    /**
     *
     * @return the complete details of the item
     */
    public String getDetails() {
        return details;
    }

    /**
     *
     * @param details a complete details for the item
     */
    public void setDetails(String details) {
        this.details = details;
    }

    /**
     *
     * @return the due date of the item
     */
    public LocalDate getDeadline() {
        return deadline;
    }

    /**
     *
     * @param deadline a due date for the item
     */
    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    /**
     * Override default, for compare
     *
     * @return the hash of the item
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.shortDescription);
        hash = 47 * hash + Objects.hashCode(this.details);
        hash = 47 * hash + Objects.hashCode(this.deadline);
        return hash;
    }

    /**
     * test two objects if they have the same value
     *
     * @param obj object to be tested equal
     * @return true if equal, false if not
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TodoItem other = (TodoItem) obj;
        if (!Objects.equals(this.shortDescription, other.shortDescription)) {
            return false;
        }
        if (!Objects.equals(this.details, other.details)) {
            return false;
        }
        if (!Objects.equals(this.deadline, other.deadline)) {
            return false;
        }
        return true;
    }
}
