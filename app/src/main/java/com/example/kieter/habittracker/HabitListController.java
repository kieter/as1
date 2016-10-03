package com.example.kieter.habittracker;

import android.widget.Toast;

/**
 * Created by kiete on 9/25/2016.
 */

public class HabitListController {
    private static HabitList habitList = null;
    private static Habit selectedHabit;

    static public HabitList getHabitList() {
        if (habitList == null) {
            habitList = new HabitList();
//            throw new RuntimeException("SHOULD NOT GO HERE");

        }
        return habitList;
    }

    public static void addHabit(Habit habit) {
        getHabitList().addHabit(habit);
    }

    public static void removeHabit(Habit habit) {
        getHabitList().removeHabit(habit);
    }

    public static int size() {
        return getHabitList().size();
    }

    public static void giveHabitList(HabitList givenHabitList) {
        habitList = givenHabitList;
    }

    public static void giveSelectedHabit(Habit habit) {
        selectedHabit = habit;
    }

    public static Habit getSelectedHabit() {
        return selectedHabit;
    }

}
