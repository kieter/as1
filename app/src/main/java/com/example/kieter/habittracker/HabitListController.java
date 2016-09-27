package com.example.kieter.habittracker;

/**
 * Created by kiete on 9/25/2016.
 */

public class HabitListController {
    private static HabitList habitList = null;

    static public HabitList getHabitList() {
        if (habitList == null) {
            habitList = new HabitList();
        }
        return habitList;
    }

    public void addHabit(Habit habit) {
        getHabitList().addHabit(habit);
    }

    public int size() {
        return getHabitList().size();
    }

}
