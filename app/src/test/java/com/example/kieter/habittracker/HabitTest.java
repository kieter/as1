package com.example.kieter.habittracker;

/**
 * Created by kiete on 9/24/2016.
 */

import junit.framework.TestCase;
import com.example.kieter.habittracker.Habit;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class HabitTest extends TestCase{
    public void testHabit () throws HabitInvalidException {
        String date = "";
        ArrayList list = new ArrayList();

        String habitName = "Hang out with Bennett";
        Habit habit = new Habit(habitName, date, list);
        assertTrue("Habit name is not equal", habitName.equals(habit.getName()));
        assertTrue("Habit name is not equal", habitName.toString().equals(habit.getName()));
    }

    public void testGetCreationDate() throws HabitInvalidException {
        String habitName = "Google dank memes";
        String date = "1997/01/21";
        ArrayList list = new ArrayList();

        Habit testHabit = new Habit(habitName, date, list);
        assertTrue("Date is not equal", testHabit.getCreationDate().equals(date));
    }

    public void testGetFrequency() throws HabitInvalidException {
        String habitName = "Program this assignment";
        String date = "2016/09/01";
        ArrayList<String> list1 = new ArrayList<String>(Arrays.asList("Saturday", "Sunday"));
        ArrayList<String> list2 = new ArrayList<String>(Arrays.asList("Sunday", "Saturday"));

        Habit testHabit1 = new Habit(habitName, date, list1);
        Habit testHabit2 = new Habit(habitName, date, list2);

        assertTrue("list1 and testHabit1 frequency aren't the same", testHabit1.getFrequency().equals(list1));
        assertTrue("Lists are not equal", testHabit1.getFrequency().equals(testHabit2.getFrequency()));


    }
    public void testIsToday() throws HabitInvalidException {
        String habitName = "Wear pink";
        // "On October 3rd, he asked me what day it was. It's October 3rd."
        String date = "2004/10/03";
        ArrayList list = new ArrayList();
        list.add("Wednesday");

        Habit testHabit = new Habit(habitName, date, list);

        if (new SimpleDateFormat("EEE", Locale.CANADA).format(new Date()) == "Wednesday") {
            assertTrue("Do you have anything pink?", testHabit.isToday());
        }
        else {
            assertFalse("On Wednesdays we wear pink!", testHabit.isToday());
        }

    }





}

