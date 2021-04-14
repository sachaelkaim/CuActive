package com.soen357.cuactive;

import android.annotation.SuppressLint;
import android.content.Intent;
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

        Button topButton = findViewById(R.id.topButton);
        Button midButton = findViewById(R.id.midButton);
        Button botButton = findViewById(R.id.botButton);
        TextView points = findViewById(R.id.points);

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String mString = mPrefs.getString("points", "0");
        points.setText("Pts : " + mString);

        topButton.setOnClickListener(v -> moveToActivityScreen( "Exercise", mPrefs));

        midButton.setOnClickListener(v -> moveToActivityScreen( "Chores", mPrefs));

        botButton.setOnClickListener(v -> moveToActivityScreen( "Cooking", mPrefs));

    }

    private void moveToActivityScreen(String activity, SharedPreferences mPrefs)
    {
        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putString("activity", activity).apply();

        Intent intent = new Intent(this, ActivityScreen.class);
        startActivity(intent);
    }
}