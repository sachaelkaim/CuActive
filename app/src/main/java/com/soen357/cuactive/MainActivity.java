package com.soen357.cuactive;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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

        createNotificationChannel();

        Button topButton = findViewById(R.id.topButton);
        Button midButton = findViewById(R.id.midButton);
        Button botButton = findViewById(R.id.botButton);
        Button shopButton = findViewById(R.id.shopButton);
        TextView points = findViewById(R.id.points);

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
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

    private void setupReceiver()
    {
        Intent notificationIntent = new Intent(getApplicationContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (1000 * 60 * 30), 1000 * 60 * 30, pendingIntent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        setupReceiver();
    }
}