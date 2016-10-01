package com.example.kieter.habittracker;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by kieter on 9/24/2016.
 */

public class HabitList extends ArrayList {

    protected ArrayList<Habit> habitList;
    protected ArrayList<Listener> listeners;

    public HabitList() {
        habitList = new ArrayList<Habit>();
        listeners = new ArrayList<Listener>();
    }

    public Collection<Habit> getHabits() {
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
