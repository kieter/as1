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
    //
    protected String habitName;
    protected String creationDate;
//    protected SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
    protected ArrayList<String> frequency;

    // Constructor
    public Habit(String name, String date, ArrayList<String> daysSelected) {
        this.habitName = name;
        this.creationDate = date;
        this.frequency = daysSelected;
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

    public Boolean isToday() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE", Locale.CANADA);
        Date now = new Date();
        String nowDay = dateFormat.format(now);
        return this.frequency.contains(nowDay);
    }

    public String toString() {
        return this.habitName;
    }


}
