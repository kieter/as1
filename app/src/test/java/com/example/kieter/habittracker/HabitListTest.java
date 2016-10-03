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

/**
 * Created by kiete on 9/24/2016.
 */

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Collection;

public class HabitListTest extends TestCase{
    public void testEmptyHabitList() {
        HabitList habitList = new HabitList();
        assertTrue("Empty habit list", habitList.size() == 0);
    }

    public void testAddHabit() throws HabitInvalidException {
        String date = "";
        ArrayList list = new ArrayList();

        HabitList habitList = new HabitList();
        String habitName = "Netflix and chill with the bae";
        Habit testHabit = new Habit(habitName, date, list);

        habitList.addHabit(testHabit);
//        Collection<Habit> habits = habitList.getHabits();

        assertTrue("Students list size", habitList.size() == 1);
        assertTrue("Test habit not contained", habitList.contains(testHabit));
    }

    public void testGetHabit() throws HabitInvalidException {
        String date = "";
        ArrayList list = new ArrayList();

        HabitList habitList = new HabitList();
        String habitName = "Netflix and chill with the bae";
        Habit testHabit = new Habit(habitName, date, list);

        habitList.addHabit(testHabit);
        Collection<Habit> habits = habitList.getHabits();

        assertTrue("Students list size", habits.size() == 1);
        assertTrue("Test habit not contained", habits.contains(testHabit));
    }

    public void testRemoveHabit() throws HabitInvalidException {
        String date = "";
        ArrayList list = new ArrayList();

        HabitList habitList = new HabitList();
        String habitName = "Eat with the bae";
        Habit testHabit = new Habit(habitName, date, list);

        habitList.addHabit(testHabit);
        Collection<Habit> habits = habitList.getHabits();

        assertTrue("Habit list size", habitList.size() == 1);
        assertTrue("Test habit not contained", habitList.contains(testHabit));

        habitList.removeHabit(testHabit);

        assertTrue("Habit list size", habitList.size() == 0);
        assertFalse("Test habit still theree?", habitList.contains(testHabit));
    }

    boolean updated = false;
    public void testNotifyListeners() throws HabitInvalidException {
        HabitList habitList = new HabitList();
        String habitName = "";
        String date = "";
        ArrayList list = new ArrayList();

        updated = false;
        Listener l = new Listener() {
            public void update() {
                HabitListTest.this.updated = true;
            }
        };
        habitList.addListener(l);
        Habit testHabit = new Habit("Say hi to everyone", date, list);
        habitList.addHabit(testHabit);
        habitList.addHabit(new Habit(habitName, date, list));
        assertTrue("HabitList didn't fire an update off", this.updated);

        updated = false;
        habitList.removeHabit(testHabit);
        assertTrue("HabitList didn't fire an update", this.updated);
    }

    public void testRemoveListeners() throws HabitInvalidException {
        HabitList habitList = new HabitList();
        String habitName = "";
        String date = "";
        ArrayList list = new ArrayList();

        updated = false;
        Listener l = new Listener() {
            public void update() {
                HabitListTest.this.updated = true;
            }
        };
        habitList.addListener(l);
        habitList.removeListener(l);
        habitList.addHabit(new Habit(habitName, date, list));
        assertFalse("StudentList didn't fire an update off", this.updated);
    }
}
