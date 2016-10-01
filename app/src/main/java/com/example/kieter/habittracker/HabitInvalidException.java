package com.example.kieter.habittracker;

/**
 * Created by kiete on 9/30/2016.
 */

public class HabitInvalidException extends Exception {

    public HabitInvalidException() {
    }

    public HabitInvalidException(String message) {
        super(message);
    }
}
