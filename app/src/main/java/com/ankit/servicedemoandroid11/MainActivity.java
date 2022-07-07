package com.ankit.servicedemoandroid11;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

// https://stackoverflow.com/questions/34216348/best-way-to-run-a-never-ending-service-in-android

public class MainActivity extends AppCompatActivity {
    Intent mServiceIntent;
    private YourService mYourService;
    Button btnTap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTap = findViewById(R.id.btnTap);

        mYourService = new YourService();
        mServiceIntent = new Intent(this, mYourService.getClass());
        if (!isMyServiceRunning(mYourService.getClass())) {
            startService(mServiceIntent);
        }

        btnTap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("1", "1");
                sendBroadcast(new Intent("ABCD"));
            }
        });
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("Service status", "Running");
                return true;
            }
        }
        Log.i("Service status", "Not running");
        return false;
    }


    @Override
    protected void onDestroy() {
        //stopService(mServiceIntent);
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, RestarterBroadcastReceiver.class);
        this.sendBroadcast(broadcastIntent);
        super.onDestroy();
    }
}