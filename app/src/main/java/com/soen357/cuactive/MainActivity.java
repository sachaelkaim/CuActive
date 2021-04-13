package com.soen357.cuactive;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button topButton = (Button) findViewById(R.id.topButton);
        Button midButton = (Button) findViewById(R.id.midButton);
        Button botButton = (Button) findViewById(R.id.botButton);
        TextView points = (TextView) findViewById(R.id.points);

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String mString = mPrefs.getString("points", "0");
        points.setText("Pts : " + mString);

        topButton.setOnClickListener(v -> points.setText(updatePoints( 1, mPrefs)));

        midButton.setOnClickListener(v -> points.setText(updatePoints( 2, mPrefs)));

        botButton.setOnClickListener(v -> points.setText(updatePoints( 3, mPrefs)));

    }

    private String updatePoints(int value, SharedPreferences mPrefs)
    {
        String mString = mPrefs.getString("points", "0");
        int currentPoints = Integer.parseInt(mString);
        int newValue = currentPoints + value;

        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putString("points", String.valueOf(newValue)).apply();

        return "Pts : " + newValue;
    }
}