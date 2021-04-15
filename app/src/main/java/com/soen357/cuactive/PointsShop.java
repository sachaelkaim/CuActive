package com.soen357.cuactive;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.TextView;

public class PointsShop extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_shop);

        Button buy5 = findViewById(R.id.buy5Points);
        Button buy10 = findViewById(R.id.buy10Points);
        Button buy20 = findViewById(R.id.buy20Points);
        Button buy40 = findViewById(R.id.buy40Points);
        Button buy50 = findViewById(R.id.buy50Points);
        Button buy100 = findViewById(R.id.buy100Points);
        Button back = findViewById(R.id.backButton2);
        TextView points = findViewById(R.id.points2);

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String mString = mPrefs.getString("points", "0");
        points.setText("Pts : " + mString);

        buy5.setOnClickListener(v -> usePoints(5, mPrefs));
        buy10.setOnClickListener(v -> usePoints(10, mPrefs));
        buy20.setOnClickListener(v -> usePoints(20, mPrefs));
        buy40.setOnClickListener(v -> usePoints(40, mPrefs));
        buy50.setOnClickListener(v -> usePoints(50, mPrefs));
        buy100.setOnClickListener(v -> usePoints(100, mPrefs));
        back.setOnClickListener(v -> moveToMainScreen());
    }

    @SuppressLint("SetTextI18n")
    private void usePoints(int value, SharedPreferences mPrefs)
    {
        String mString = mPrefs.getString("points", "0");
        int currentPoints = Integer.parseInt(mString);

        if (currentPoints < value)
        {
            AlertDialog alertDialog = new AlertDialog.Builder(PointsShop.this).create();
            alertDialog.setMessage("You do not have enough points");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing
                }
            });
            alertDialog.show();
        }
        else
        {
            int newValue = currentPoints - value;
            SharedPreferences.Editor mEditor = mPrefs.edit();
            mEditor.putString("points", String.valueOf(newValue)).apply();

            TextView points = findViewById(R.id.points2);
            points.setText("Pts : " + newValue);

            AlertDialog alertDialog = new AlertDialog.Builder(PointsShop.this).create();
            alertDialog.setMessage("You have used " + value + " points");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing
                }
            });
            alertDialog.show();
        }
    }

    private void moveToMainScreen()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}