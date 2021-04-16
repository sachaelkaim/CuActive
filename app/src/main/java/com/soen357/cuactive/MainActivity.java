package com.soen357.cuactive;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        onFirstRun(mPrefs);

        Button topButton = findViewById(R.id.topButton);
        Button midButton = findViewById(R.id.midButton);
        Button botButton = findViewById(R.id.botButton);
        Button shopButton = findViewById(R.id.shopButton);
        TextView points = findViewById(R.id.points);
        TextView minutes = findViewById(R.id.minutes);

        changeImage(mPrefs);

        int iValue = mPrefs.getInt("minutesSpent", 0);
        minutes.setText("Minutes : " + iValue);

        String mString = mPrefs.getString("points", "0");
        points.setText("Pts : " + mString);

        topButton.setOnClickListener(v -> moveToActivityScreen( "Exercise", mPrefs));
        midButton.setOnClickListener(v -> moveToActivityScreen( "Chores", mPrefs));
        botButton.setOnClickListener(v -> moveToActivityScreen( "Cooking", mPrefs));
        shopButton.setOnClickListener(v -> moveToPointsShop());
    }

    private void moveToActivityScreen(String activity, SharedPreferences mPrefs)
    {
        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putString("activity", activity).apply();

        Intent intent = new Intent(this, ActivityScreen.class);
        startActivity(intent);
    }

    private void moveToPointsShop()
    {
        Intent intent = new Intent(this, PointsShop.class);
        startActivity(intent);
    }

    // Taken from https://developer.android.com/training/notify-user/channels
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Default channel";
            String description = "Default channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("cuactive", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void setupNotificationReceiver()
    {
        Intent notificationIntent = new Intent(getApplicationContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (1000 * 60 * 30), 1000 * 60 * 30, pendingIntent);
    }

    private void setupMinuteReceiver()
    {
        Intent exerciseIntent = new Intent(getApplicationContext(), ExerciseReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, exerciseIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
    }

    private void onFirstRun(SharedPreferences mPrefs)
    {
        boolean isFirstRun = mPrefs.getBoolean("firstRun", true);
        if (isFirstRun)
        {
            createNotificationChannel();
            setupMinuteReceiver();

            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setMessage("Thank you for using the CuActive Rough Prototype application!");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //Do nothing
                }
            });
            alertDialog.show();

            SharedPreferences.Editor mEditor = mPrefs.edit();
            mEditor.putBoolean("firstRun", false).apply();
        }
    }

    private void changeImage(SharedPreferences mPrefs)
    {
        ImageView image = findViewById(R.id.imageView);
        int iValue = mPrefs.getInt("minutesSpent", 0);

        if (iValue < 65)
        {
            image.setImageResource(R.mipmap.sad);
        }
        else if (iValue < 130)
        {
            image.setImageResource(R.mipmap.neutral);
        }
        else
        {
            image.setImageResource(R.mipmap.happy);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        setupNotificationReceiver();
    }
}