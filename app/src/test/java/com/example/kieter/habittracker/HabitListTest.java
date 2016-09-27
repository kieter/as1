package com.example.kieter.habittracker;

/**
 * Created by kiete on 9/24/2016.
 */
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Collection;
import com.example.kieter.habittracker.Listener;

public class HabitListTest extends TestCase{
    public void testEmptyHabitList() {
        HabitList habitList = new HabitList();
        assertTrue("Empty habit list", habitList.size() == 0);
    }

    public void testAddHabit() {
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

    public void testGetHabit() {
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

    public void testRemoveHabit() {
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

    //TODO: 35:00 IN STUDENT PICKER PART 6, IMPLEMENT REMOVE LISTENER
    boolean updated = false;
    public void testNotifyListeners() {
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
        habitList.addHabit(new Habit(habitName, date, list));
        assertTrue("StudentList didn't fire an update off", this.updated);
    }

    public void testRemoveListeners() {
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
