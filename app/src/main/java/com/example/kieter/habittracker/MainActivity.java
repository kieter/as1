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
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;

/*
Main activity will set up all the XML in activity_main, it is the activity in which users are able
to add and view habits, tapping on habits will show more information, long pressing will permit
the user to delete the habit.
Adding a habit uses a custom dialog, MainActivity receives information from that dialogue via a
function call.
Main activity also handles persistence using GSON/JSON. It saves after any edit.
 */
public class MainActivity extends AppCompatActivity implements AddHabitDialogFragment.HabitInputDialogListener {

    // File name for saving/loading
    private static final String FILENAME = "data.sav";
    // For saving/loading data across activities
    public static ArrayList<Habit> listOfHabits;

    @Override
    /*
    On create will initialize views, array adapters, and all click listeners
     */
    protected void onCreate(Bundle savedInstanceState) {
        // Load saved JSON, if there is one, otherwise create null.
        loadFromFile();
        for (Habit habit : listOfHabits) {
            HabitListController.addHabit(habit);
        }
        super.onCreate(savedInstanceState);

        // Initialize views
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ListView listView = (ListView) findViewById(R.id.listOfHabits);

        // Initialize and set ArrayAdapter
        final ArrayList<Habit> list = listOfHabits;
        final ArrayAdapter<Habit> habitAdapter = new ArrayAdapter<Habit>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(habitAdapter);

        // Clear observers
        HabitListController.getHabitList().clearListeners();
        // Added an observer!
        HabitListController.getHabitList().addListener(new Listener() {
            @Override
            // Update refreshes the data held in the instance of the habit list.
            public void update() {
                list.clear();
                Collection<Habit> habits = HabitListController.getHabitList().getHabits();
                list.addAll(habits);
                habitAdapter.notifyDataSetChanged();
            }
        });

        // Set up array onItemClickListener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            /*
            Once the user clicks an element in the ListView, it will create a new intent to go
            to an activity that displays extra information about that habit.
             */
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                final int finalClickPosition = position;
                HabitListController.giveSelectedHabit(list.get(finalClickPosition));
                Intent intent = new Intent(MainActivity.this, HabitActivity.class);
                startActivity(intent);
                saveInFile();
            } // end of onItem Click
        }); // end of onItemClickListener

        // Set up array onItemLongClickListener
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            /*
            Should the user long click an item in the array list they will receive a prompt of
            whether to delete or cancel the long clicked item in the ListView.
             */
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                final int finalPosition = position;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setMessage("Are you sure you want to delete the habit " + list.get(finalPosition).toString() + "?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    // On click the positive option, confirm deletion in the model and save
                    public void onClick(DialogInterface dialog, int which) {
                        Habit habitToRemove = list.get(finalPosition);
                        HabitListController.removeHabit(habitToRemove);
                        saveInFile();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    // On click the negative button, cancel the dialog.
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing.
                    }
                });
                builder.show();
                return true;
            } // end of onItemLongClick
        }); // end of onItemLongClickListener
    } // end of onCreate

    /*
    addHabitOnClick creates an instance of the custom dialog addHabitDialog that the user uses
    to input information about a new habit.
     */
    public void addHabitOnClick(View view) {
        android.app.FragmentManager manager = getFragmentManager();
        AddHabitDialogFragment addHabitDialog = new AddHabitDialogFragment();
        addHabitDialog.show(manager, "addHabitDialog");
    } // end of addHabitOnClick

    /*
    onFinishInputEvent will be called by the addHabitDialog once the user confirms their input of
    a new habit, this method will turn the user's input into an instance of a Habit and append that
    habit to the static habit list and it will save the changes to internal storage.
     */
    public void onFinishInputEvent(String name, String date, ArrayList<String> selectedDays) {
        String listString = "";
        for (Object s : selectedDays) {
            listString += s + ",";
        }

        try {
            Habit inputtedHabit = new Habit(name, date, selectedDays);
            HabitListController.addHabit(inputtedHabit);
            Snackbar.make(findViewById(R.id.AddHabitFAB), "Habit added!", Snackbar.LENGTH_SHORT).show();
            saveInFile();
        }
        // If the user leaves the habit name or date blank inputting will be cancelled and the user
        // will receive a cancel Toast.
        catch (HabitInvalidException badHabit) {
            Toast.makeText(MainActivity.this, "Habit name or date can't be empty.", Toast.LENGTH_SHORT).show();
        }

    } // end of onFinishInputEvent

    /*
    Load from file will open the persistent file from internal storage and store the list of habits
    stored into a static variable listOfHabits that persists between activities so that the proper
    information can be loaded and stored upon any edits to habits.
     */
    private void loadFromFile() {
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
} // end of MainActivity
