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

import java.util.ArrayList;

/*
Habitlist contains habits.
One can add habits, remove habits, print habits, get size, add/remove listeners, notify listeners
clear listeners, and see if a habit is contained in the list.
 */
public class HabitList extends ArrayList {

    protected ArrayList<Habit> habitList;
    protected ArrayList<Listener> listeners;

    // Constructor
    public HabitList() {
        habitList = new ArrayList<Habit>();
        listeners = new ArrayList<Listener>();
    }

    public ArrayList<Habit> getHabits() {
        return habitList;
    }

    public void addHabit(Habit habit) {
        habitList.add(habit);
        // tell anyone that we've changed
        notifyListeners();
    }

    public void removeHabit(Habit habit) {
        habitList.remove(habit);
        notifyListeners();
    }

    public int size() {
        return habitList.size();
    }

    public boolean contains(Habit habit) {
        return habitList.contains(habit);
    }


    // Listeners
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
