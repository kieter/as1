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
/*
HabitListController is a singleton of habitList, it manages any modification to the habit list
including adds and deletes.
Once the user has added a habit, and selects it from the list (or multiple habits and selects one)
the controller is passed the selected habit so the user can see it in the habit activity.
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

    // a setter that sets the habitList
    public static void giveHabitList(HabitList givenHabitList) {
        habitList = givenHabitList;
    }

    // a setter that sets the selected habit
    public static void giveSelectedHabit(Habit habit) {
        selectedHabit = habit;
    }

    // a getter that returns the selected habit
    public static Habit getSelectedHabit() {
        return selectedHabit;
    }

}
