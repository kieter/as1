package com.example.kieter.habittracker;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by kieter on 9/23/2016.
 */

//https://developer.android.com/guide/topics/ui/dialogs.html
//http://stackoverflow.com/questions/10903754/input-text-dialog-android
    //TODO transfer all the input info into classes
public class AddHabitDialogFragment extends DialogFragment {

    public AddHabitDialogFragment() {
        // required empty constructor
    }
    //http://android-developers.blogspot.ca/2012/05/using-dialogfragments.html
    public interface HabitInputDialogListener {
        void onFinishInputEvent(String name, String date, ArrayList<String> selectedDays);
    }

    private HabitInputDialogListener listener;

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        // Create dialog
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
        layout.addView(input);

        //set Calendar and date input
        final Calendar habitCalendar = Calendar.getInstance();
        final EditText dateInput = new EditText(getActivity());
        dateInput.setHint("Start date");
        layout.addView(dateInput);
        updateCalendarLabel(dateInput, habitCalendar);

        //set multichoice list
        final CharSequence[] daysOfWeek = new CharSequence[] {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        final ArrayList<String> selectedDaysofWeek = new ArrayList();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
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
            public void onClick(DialogInterface dialog, int which) {

                // TODO probably throw an error if name or date are empty

                HabitInputDialogListener listener = (HabitInputDialogListener) getActivity();
                listener.onFinishInputEvent(input.getText().toString(), dateInput.getText().toString(), selectedDaysofWeek);

                dialog.dismiss();

            }
        });

        // cancel button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setView(layout);

        return builder.create();
    }

    private void updateCalendarLabel(EditText dateInput, Calendar habitCalendar) {
        String dateFormat = "yyyy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.CANADA);
        dateInput.setText(sdf.format(habitCalendar.getTime()));
    }


}
