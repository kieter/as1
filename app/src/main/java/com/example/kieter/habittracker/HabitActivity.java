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

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/*
HabitActivity will set up all the XML in activity_habit, it is the activity in which users are
able to view more information about the habit that they clicked in MainActivity like the days
the habit is active, any completions, and how many completions that the habit has.
Main activity also handles persistence using GSON/JSON. It saves after any edit.
 */
public class HabitActivity extends AppCompatActivity {

    // for saving data in internal storage
    private static final String FILENAME = "data.sav";
    ArrayList<Habit> listOfHabits2 = MainActivity.listOfHabits;

    @Override
    /*
    onCreate will load all the information of the selected habit form MainActivity (passed by the
    habit list controller) and display it with views. The user is also able to complete the habit
    from this screen and it will increase their count.
    The user may also delete habit completions from this activity.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Habit selectedHabit = HabitListController.getSelectedHabit();
        // Initialize views
        setContentView(R.layout.activity_habit);
        // Display habit name
        TextView habitTextView = (TextView) findViewById(R.id.habitTextView);
        habitTextView.setText(selectedHabit.getName());
        // Set up array adapter
        final ListView completionsListView = (ListView) findViewById(R.id.completionsListView);
        final TextView counterTextView = (TextView)findViewById(R.id.completionCountTextView);
        final ArrayList<String> selectedHabitDates = selectedHabit.getCompletions();
        final ArrayAdapter<String> dateAdapter = new ArrayAdapter<String>(HabitActivity.this, android.R.layout.simple_list_item_1, selectedHabitDates);
        // Set up more views
        completionsListView.setAdapter(dateAdapter);
        counterTextView.setText(Integer.toString(selectedHabit.getCompletions().size()));
        ArrayList<String> daysActive = new ArrayList<String>(selectedHabit.getFrequency());
        if (daysActive.contains("Monday")) {
            TextView monTextView = (TextView)findViewById(R.id.monTextView);
            monTextView.setTextColor(getResources().getColor(R.color.colorAccent));
        }
        if (daysActive.contains("Tuesday")) {
            TextView tueTextView = (TextView)findViewById(R.id.tueTextView);
            tueTextView.setTextColor(getResources().getColor(R.color.colorAccent));
        }
        if (daysActive.contains("Wednesday")) {
            TextView wedTextView= (TextView)findViewById(R.id.wedTextView);
            wedTextView.setTextColor(getResources().getColor(R.color.colorAccent));
        }
        if (daysActive.contains("Thursday")) {
            TextView thursTextView = (TextView)findViewById(R.id.thursTextView);
            thursTextView.setTextColor(getResources().getColor(R.color.colorAccent));
        }
        if (daysActive.contains("Friday")) {
            TextView friTextView= (TextView)findViewById(R.id.friTextView);
            friTextView.setTextColor(getResources().getColor(R.color.colorAccent));
        }
        if (daysActive.contains("Saturday")) {
            TextView satTextView = (TextView)findViewById(R.id.satTextView);
            satTextView.setTextColor(getResources().getColor(R.color.colorAccent));
        }
        if (daysActive.contains("Sunday")) {
            TextView sunTextView = (TextView)findViewById(R.id.sunTextView);
            sunTextView.setTextColor(getResources().getColor(R.color.colorAccent));
        }

        // Added an observer after clearing observers.
        selectedHabit.clearListeners();
        selectedHabit.addListener(new Listener() {
            @Override
            // update keeps the datset persistent with user changes
            public void update() {
                dateAdapter.notifyDataSetChanged();
            }
        });


        FloatingActionButton completeFAB = (FloatingActionButton) findViewById(R.id.completeFAB);
        completeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            /*
            When the user clicks the floating action button with a checkmark it will add a
            completion to the list view with a date accurate to the second. It will also increment
            the completions counter in the bottom right corner and save the file to internal
            storage.
             */
            public void onClick(View v) {
                Toast.makeText(HabitActivity.this, "Habit completed!", Toast.LENGTH_SHORT).show();

                Date date = new Date();
                String dateFormat = "yyyy/MM/dd hh:mm:ss";
                SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.CANADA);
                String format = sdf.format(date);

                counterTextView.setText(Integer.toString(selectedHabit.getCompletions().size() + 1) );
                selectedHabit.addCompletion(format);

                saveInFile();
            }
        });

        ImageButton backImageButton = (ImageButton) findViewById(R.id.backImageButton);
        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            // When the user clicks the back button it will finish the activity and go back
            public void onClick(View v) {
                HabitActivity.this.finish();
            }
        });
        completionsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            /*
            Should the user long click a completion they are given a prompt about whether or not
            to delete that completion.
             */
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                final int finalPosition = position;
                AlertDialog.Builder builder = new AlertDialog.Builder(HabitActivity.this);
                builder.setCancelable(true);
                builder.setMessage("Are you sure you want to delete this completion?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    /*
                    Should the user click the positive button, it will remove the habit completion
                    from the list and delete it from the static list and save in file.
                     */
                    public void onClick(DialogInterface dialog, int which) {
                        selectedHabitDates.remove(finalPosition);
                        dateAdapter.notifyDataSetChanged();
                        saveInFile();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    /*
                    Should the user cancel it will cancel the dialog
                     */
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing.
                    }
                });
                builder.show();
                return false;
            } // end of onItemLongClick
        });
    } // end of on Create

    /*
    Load from file will open the persistent file from internal storage and store the list of habits
    stored into a static variable listOfHabits that persists between activities so that the proper
    information can be loaded and stored upon any edits to habits.
     */
    private void loadFromFile() {
        ArrayList<Habit> listOfHabits;
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            Type listType = new TypeToken<ArrayList<Habit>>(){}.getType();
            listOfHabits = gson.fromJson(in, listType);
        }
        catch (FileNotFoundException e) {
        }
    } // end of loadFromFile

    /*
    Save from file alters the persistent file in internal storage to keep consistency with what's
    going on with user edits in the app.
    */
    private void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME, 0);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            ArrayList<Habit> temp = HabitListController.getHabitList().getHabits();

            gson.toJson(temp, out);

            out.flush();
            fos.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    } // end of saveInFile
} // end of HabitActivity

