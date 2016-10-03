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

public class MainActivity extends AppCompatActivity implements AddHabitDialogFragment.HabitInputDialogListener {

    private static final String FILENAME = "data.sav";
    public static ArrayList<Habit> listOfHabits;
//    private HabitList habitList = new;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadFromFile();
        for (Habit habit : listOfHabits) {
            HabitListController.addHabit(habit);
        }
        super.onCreate(savedInstanceState);

        //Toast.makeText(this, HabitListController.getHabitList().printHabit(), Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        HabitListController habitListController = new HabitListController();

        ListView listView = (ListView) findViewById(R.id.listOfHabits);
//        Collection<Habit> habits = HabitListController.getHabitList().getHabits();

        //final Collection<Habit> habits = HabitListController.getHabitList().getHabits();
        //final ArrayList<Habit> list = new ArrayList<Habit>(habits);
        final ArrayList<Habit> list = listOfHabits;
        final ArrayAdapter<Habit> habitAdapter = new ArrayAdapter<Habit>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(habitAdapter);


        HabitListController.getHabitList().clearListeners();
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
            // TODO long clicking also clicks?
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
                        HabitListController.removeHabit(habitToRemove);
                        saveInFile();
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                final int finalClickPosition = position;
                HabitListController.giveSelectedHabit(list.get(finalClickPosition));
                Toast.makeText(MainActivity.this, HabitListController.getSelectedHabit().getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, HabitActivity.class);
                startActivity(intent);
                saveInFile();
            }
        });
    }

    public void addHabitOnClick(View view) {
        android.app.FragmentManager manager = getFragmentManager();
        AddHabitDialogFragment addHabitDialog = new AddHabitDialogFragment();
        addHabitDialog.show(manager, "addHabitDialog");

    }

    private void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            Type listType = new TypeToken<ArrayList<Habit>>(){}.getType();
            listOfHabits = gson.fromJson(in, listType);

            //Type listType = new TypeToken<HabitList>(){}.getType();

            //Toast.makeText(this, "From load file: " + HabitListController.getHabitList().printHabit(), Toast.LENGTH_SHORT).show();
            //Toast.makeText(this, "Size of loaded: " + Integer.toString(HabitListController.getHabitList().size()), Toast.LENGTH_SHORT).show();
            //HabitList test = gson.fromJson(in, listType);

            //HabitListController.giveHabitList((HabitList) gson.fromJson(in, listType));
            //HabitListController.giveHabitList(test);
           // Toast.makeText(this, "Size of loaded: " + Integer.toString(HabitListController.getHabitList().size()), Toast.LENGTH_SHORT).show();
           // Toast.makeText(this, "Stuff loaded: " + HabitListController.getHabitList().printHabit(), Toast.LENGTH_SHORT).show();


        }
        catch (FileNotFoundException e) {
            //HabitListController habitListController = new HabitListController() ;
        }
    }

    private void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME, 0);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            ArrayList<Habit> temp = HabitListController.getHabitList().getHabits();

            Toast.makeText(this, "Size of saved: " + Integer.toString(HabitListController.getHabitList().size()), Toast.LENGTH_SHORT);
            Toast.makeText(this, "Stuff saved: " + HabitListController.getHabitList().printHabit(), Toast.LENGTH_SHORT).show();

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
    }

    public void onFinishInputEvent(String name, String date, ArrayList<String> selectedDays) {
        String listString = "";
        for (Object s : selectedDays) {
            listString += s + ",";
        }

        try {
            Habit inputtedHabit = new Habit(name, date, selectedDays);
            //Toast.makeText(MainActivity.this, inputtedHabit.getName(), Toast.LENGTH_SHORT).show();
            HabitListController.addHabit(inputtedHabit);
            //Toast.makeText(MainActivity.this, listString, Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(R.id.AddHabitFAB), "Habit added!", Snackbar.LENGTH_SHORT).show();
//                    .setAction("Action", null).show();
//            Toast.makeText(MainActivity.this, "Name:" + name + "Date:" + date, Toast.LENGTH_SHORT).show();
            //Toast.makeText(MainActivity.this, "Size of save: " + Integer.toString(HabitListController.getHabitList().size()), Toast.LENGTH_SHORT).show();
            saveInFile();
        }
        catch (HabitInvalidException badHabit) {
            Toast.makeText(MainActivity.this, "Habit name or date can't be empty.", Toast.LENGTH_SHORT).show();
        }

    }

    public String habitSubText(Habit habit) {
        String subText = "";
        List<String> daysOfWeek = new ArrayList<String>(asList( "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"));
        for (String day : daysOfWeek) {
            if (habit.getFrequency().contains(day)) {
                subText += day.charAt(0);
            }
        }
        return subText;
    }




//
//    public void onClickSwitchActivity(View view) {
//
//        Intent intent = new Intent(this, HabitActivity.class);
//        startActivity(intent);
//
//    }


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
