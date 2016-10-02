package com.example.kieter.habittracker;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import static android.R.id.list;
import static com.example.kieter.habittracker.R.id.completeFAB;
import static com.example.kieter.habittracker.R.id.wedTextView;

public class HabitActivity extends AppCompatActivity {
    private static final String FILENAME = "dataHabitTracker.sav";
    private Collection<Habit> habitList = new ArrayList<Habit>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);
        TextView habitTextView = (TextView) findViewById(R.id.habitTextView);
        final ListView completionsListView = (ListView) findViewById(R.id.completionsListView);


        Collection<Habit> habits = loadFromFile();
        // final ArrayList<Habit> list = new ArrayList<Habit>(habits);
        final Habit selectedHabit = HabitListController.getSelectedHabit();
        final ArrayList<String> selectedHabitDates = selectedHabit.getCompletions();
        habitTextView.setText(selectedHabit.getName());
        final ArrayAdapter<String> dateAdapter = new ArrayAdapter<String>(HabitActivity.this, android.R.layout.simple_list_item_1, selectedHabitDates);
        completionsListView.setAdapter(dateAdapter);

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

        // Added an observer!
        selectedHabit.addListener(new Listener() {
            @Override
            public void update() {
//                selectedHabitDates.clear();
//                selectedHabitDates.addAll(selectedHabit.getCompletions());
                dateAdapter.notifyDataSetChanged();
            }
        });

        FloatingActionButton completeFAB = (FloatingActionButton) findViewById(R.id.completeFAB);
        completeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HabitActivity.this, "Habit completed!", Toast.LENGTH_SHORT).show();
                Toast.makeText(HabitActivity.this, selectedHabit.getCreationDate(), Toast.LENGTH_SHORT).show();
                Date date = new Date();
                String dateFormat = "yyyy/MM/dd hh:mm:ss";
                SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.CANADA);
                String format = sdf.format(date);
                selectedHabit.addCompletion(format);
                saveInFile(HabitListController.getHabitList().getHabits());
            }
        });

        ImageButton backImageButton = (ImageButton) findViewById(R.id.backImageButton);
        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HabitActivity.this.finish();
            }
        });

        completionsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                final int finalPosition = position;
                Toast.makeText(HabitActivity.this, Integer.toString(finalPosition), Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(HabitActivity.this);
                builder.setCancelable(true);
                builder.setMessage("Are you sure you want to delete this completion?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedHabitDates.remove(finalPosition);
//                        String testString = "";
//                        for (String s: selectedHabit.getCompletions()) {
//                            testString += s + ",";
//                        }
//                        Toast.makeText(HabitActivity.this, testString, Toast.LENGTH_SHORT).show();
                        dateAdapter.notifyDataSetChanged();
                        saveInFile(HabitListController.getHabitList().getHabits());
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

    private Collection<Habit> loadFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            Type listType = new TypeToken<ArrayList<Habit>>(){}.getType();

            habitList = gson.fromJson(in, listType);

        }
        catch (FileNotFoundException e) {
            habitList = new ArrayList<Habit>();
        }
        catch (IOException e) {
            throw new RuntimeException();
        }
        return habitList;
    }

    private void saveInFile(Collection<Habit> habits) {
        try {
            FileOutputStream fos = openFileOutput(FILENAME, 0);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();

            gson.toJson(habits, out);
            out.flush();
            fos.close();
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException();
        }
        catch (IOException e) {
            throw new RuntimeException();
        }
    }
}

