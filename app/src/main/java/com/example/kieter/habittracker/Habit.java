/*
Habit Tracker - Tracks Daily Habits
Copyright (C) 2016 Kieter Philip Balisnomo, Abram Hindle

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.example.kieter.habittracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

/*
Habit contains necessary information for a habit, like it's name, the creation date (as a string),
how often it is during the week (stored as an array list of strings containing days "Monday" etc),
and completions (an array list of dates as strings).
Habit also contains listeners to update views.
 */
public class Habit {

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

    // Listener methods
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

    public void clearListeners() {
        listeners.clear();
    }

}
