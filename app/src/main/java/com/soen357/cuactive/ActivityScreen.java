package com.soen357.cuactive;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.TextView;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Date;

public class ActivityScreen extends AppCompatActivity {

    Date start;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);

        TextView activitySelection = findViewById(R.id.activityText);
        Button startButton = findViewById(R.id.startButton);
        Button backButton = findViewById(R.id.backButton);

        startButton.setBackgroundColor(Color.GREEN);

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String mString = mPrefs.getString("activity", "None Chosen");
        activitySelection.setText(mString);

        startButton.setText("Start " + mString);

        backButton.setOnClickListener(v -> moveToMainScreen());

        startButton.setOnClickListener(v -> startExercise(mPrefs));


    }

    @SuppressLint("SetTextI18n")
    private void startExercise(SharedPreferences mPrefs)
    {
        boolean started = mPrefs.getBoolean("startedExercise", true);
        Button startButton = findViewById(R.id.startButton);
        if (started)
        {
            start = new Date();
            String mString = mPrefs.getString("activity", "None Chosen");
            startButton.setText("End " + mString);
            startButton.setBackgroundColor(Color.RED);

            SharedPreferences.Editor mEditor = mPrefs.edit();
            mEditor.putBoolean("startedExercise", false).apply();
        }
        else
        {
            Date end = new Date();
            long timeSpent = end.getTime() - start.getTime();
            int minutes = (int) Math.floor(timeSpent / 60000.0f);

            int minutesDone = mPrefs.getInt("minutesSpent", 0);
            SharedPreferences.Editor mEditor = mPrefs.edit();
            mEditor.putInt("minutesSpent", minutes + minutesDone).apply();

            int pointsEarned = (int) Math.floor(Math.random() * (100 - 20 + 1) + 20);
            updatePoints(pointsEarned, mPrefs);

            AlertDialog alertDialog = new AlertDialog.Builder(ActivityScreen.this).create();
            alertDialog.setMessage("You have earned " + pointsEarned + " points. \n" + "You have spent " + minutes + " minutes on this activity!");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    moveToMainScreen();
                }
            });
            alertDialog.show();

            mEditor.putBoolean("startedExercise", true).apply();
        }

    }

    private void updatePoints(int value, SharedPreferences mPrefs)
    {
        String mString = mPrefs.getString("points", "0");
        int currentPoints = Integer.parseInt(mString);
        int newValue = currentPoints + value;

        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putString("points", String.valueOf(newValue)).apply();
    }

    private void moveToMainScreen()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}