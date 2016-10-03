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

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by kieter on 9/23/2016.
 */

/*
AddHabitDialog fragment is a custom dialog fragment that allows the user to input a new habit's
name, frequency (SMTWRFS),  and date created (using a calendar, automatically filed) and passes
inputted information back to MainActivity to create an actual Habit.
*/
    //TODO: put all the stack overflow things in a readme
//https://developer.android.com/guide/topics/ui/dialogs.html
//http://stackoverflow.com/questions/10903754/input-text-dialog-android
public class AddHabitDialogFragment extends DialogFragment {

    public AddHabitDialogFragment() {
        // required empty constructor
    }
    //http://android-developers.blogspot.ca/2012/05/using-dialogfragments.html
    /*
    Calls the method that creates an instance of the habit using the inputted information.
     */
    public interface HabitInputDialogListener {
        void onFinishInputEvent(String name, String date, ArrayList<String> selectedDays);
    }

    private HabitInputDialogListener listener;

    @Override
    /*
    onCreateDialog will set up all the views of the dialog for editting
     */
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        // create dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // set dialog title
        builder.setTitle("Add new habit");

        // set layout
        final LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        // set input
        final EditText input = new EditText(getActivity());
        input.setHint("Enter habit name");
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setLines(1);
        layout.addView(input);
        input.setOnKeyListener(new View.OnKeyListener() {
            //http://stackoverflow.com/questions/7940765/how-to-hide-the-soft-keyboard-from-inside-a-fragment
            @Override
            /*
            When the user hits enter the keyboard is brought down.
             */
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                    return true;
                }
                return false;
            } // end of onKey
        }); // end of onKeyListener


        // set Calendar and date input
        final Calendar habitCalendar = Calendar.getInstance();
        final EditText dateInput = new EditText(getActivity());
        dateInput.setHint("Start date");
        dateInput.setLines(1);
        layout.addView(dateInput);
        updateCalendarLabel(dateInput, habitCalendar);

        // set multichoice list
        final CharSequence[] daysOfWeek = new CharSequence[] {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        final ArrayList<String> selectedDaysofWeek = new ArrayList();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            /*
            onDateSet opens a calendar that allows the user to pick a date of creation.
             */
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                habitCalendar.set(Calendar.YEAR, year);
                habitCalendar.set(Calendar.MONTH, monthOfYear);
                habitCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateCalendarLabel(dateInput, habitCalendar);
            }
        };

        //http://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
        dateInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            /*
            When the user touches the edit text date a calendar pops up.
             */
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    DatePickerDialog habitDatePicker = new DatePickerDialog(getActivity(), date, habitCalendar
                            .get(Calendar.YEAR), habitCalendar.get(Calendar.MONTH),
                            habitCalendar.get(Calendar.DAY_OF_MONTH));
                    habitDatePicker.show();
                }
                return true;
            }
        });

        // Multichoice item input
        //https://developer.android.com/guide/topics/ui/dialogs.html
        builder.setMultiChoiceItems(daysOfWeek, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            /*
            The user is able to select the weekly frequency of the Habit
             */
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    selectedDaysofWeek.add(daysOfWeek[which].toString());
                }
                else if (selectedDaysofWeek.contains(daysOfWeek[which])) {
                    selectedDaysofWeek.remove(daysOfWeek[which]);
                }
            }

        });

        // okay button
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            /*
            Affirmative button saves the user's input and returns to the MainActivity
             */
            public void onClick(DialogInterface dialog, int which) {
                HabitInputDialogListener listener = (HabitInputDialogListener) getActivity();
                listener.onFinishInputEvent(input.getText().toString(), dateInput.getText().toString(), selectedDaysofWeek);

                dialog.dismiss();
            }
        });

        // cancel button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            /*
            Cancels the dialog
             */
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setView(layout);
        return builder.create();
    } // end of onCreate

    /*
    updateCalendarLable will update the text automatically entered into the EditText for inputting
    date
     */
    private void updateCalendarLabel(EditText dateInput, Calendar habitCalendar) {
        String dateFormat = "yyyy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.CANADA);
        dateInput.setText(sdf.format(habitCalendar.getTime()));
    }


} // end of AddHabitDialogFragment
