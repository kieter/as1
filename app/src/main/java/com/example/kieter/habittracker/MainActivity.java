package com.example.kieter.habittracker;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class MainActivity extends AppCompatActivity implements AddHabitDialogFragment.HabitInputDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView listView = (ListView)findViewById(R.id.listOfHabits);
        Collection<Habit> habits = HabitListController.getHabitList().getHabits();
        final ArrayList<Habit> list = new ArrayList<Habit>(habits);
        final ArrayAdapter<Habit> habitAdapter = new ArrayAdapter<Habit>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(habitAdapter);


        // Added an observer!
        HabitListController.getHabitList().addListener(new Listener() {
            @Override
            public void update() {
                list.clear();
                Collection<Habit> habits = HabitListController.getHabitList().getHabits();
                list.addAll(habits);
                habitAdapter.notifyDataSetChanged();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                //Toast.makeText(MainActivity.this, "Delete " + list.get(position).toString(), Toast.LENGTH_SHORT).show();
                final int finalPosition = position;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setMessage("Are you sure you want to delete the habit " + list.get(finalPosition).toString() + "?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Habit habitToRemove = list.get(finalPosition);
                        HabitListController.getHabitList().removeHabit(habitToRemove);

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing.
                    }
                });

                builder.show();

                return false;
            }
        });
    }

    public void addHabitOnClick(View view) {
        android.app.FragmentManager manager = getFragmentManager();
        AddHabitDialogFragment addHabitDialog = new AddHabitDialogFragment();
        addHabitDialog.show(manager, "addHabitDialog");

    }

    public void onFinishInputEvent(String name, String date, ArrayList<String> selectedDays) {
        HabitListController hl = new HabitListController();

        String listString = "";
        for (Object s : selectedDays) {
            listString += s + ",";
        }

        try {
            Habit inputtedHabit = new Habit(name, date, selectedDays);
            Toast.makeText(MainActivity.this, inputtedHabit.getName(), Toast.LENGTH_SHORT).show();
            HabitListController.getHabitList().addHabit(inputtedHabit);
            Toast.makeText(MainActivity.this, listString, Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(R.id.AddHabitFAB), "Habit added!", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            Toast.makeText(MainActivity.this, "Name:" + name + "Date:" + date, Toast.LENGTH_SHORT).show();
        }
        catch (HabitInvalidException badHabit) {
            Toast.makeText(MainActivity.this, "Habit name or date can't be empty.", Toast.LENGTH_SHORT).show();
        }

    }


// Settings, don't need them I think.
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
