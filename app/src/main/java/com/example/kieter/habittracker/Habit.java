package com.example.kieter.habittracker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by kieter on 9/24/2016.
 */

public class Habit {

    //TODO exception for duplicate habitNames
    protected String habitName;
    protected String creationDate;
    protected ArrayList<String> frequency;
    protected ArrayList<String> completions;
    protected ArrayList<Listener> listeners;

    // Constructor
    public Habit(String name, String date, ArrayList<String> daysSelected) throws HabitInvalidException {
        if (name.replaceAll("\\s+", "") == "" || date.replaceAll("\\s+", "") == "") {
            throw new HabitInvalidException();
        }
        else {
            this.habitName = name;
            this.creationDate = date;
            this.frequency = daysSelected;
            this.completions = new ArrayList<String>();
            this.listeners = new ArrayList<Listener>();
        }

    }
    // Methods
    public String getName() {
        return this.habitName;
    }

    public String getCreationDate() {
        return this.creationDate;
    }

    public ArrayList<String> getFrequency() {
        Collections.sort(this.frequency, String.CASE_INSENSITIVE_ORDER);
        return this.frequency;
    }

    // TODO: Test getCompletions
    public ArrayList<String> getCompletions() {
        return this.completions;
    }

    // TODO: addDate, removeDate and tests

    public void addCompletion(String dateString) {
        this.completions.add(dateString);
        notifyListeners();
    }

    public void removeCompletion(String dateString) {
        this.completions.remove(dateString);
        notifyListeners();
    }

    public void removeCompletion(Integer i) {
        this.completions.remove(i);
        notifyListeners();
    }

    public Boolean isToday() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE", Locale.CANADA);
        Date now = new Date();
        String nowDay = dateFormat.format(now);
        return this.frequency.contains(nowDay);
    }

    public String toString() {
        return this.habitName;
    }

    public void notifyListeners() {
        for (Listener listener: listeners) {
            listener.update();
        }
    }

    public void addListener(Listener l) {
        listeners.add(l);
    }

    public void removeListener(Listener l) {
        listeners.remove(l);
    }


}
