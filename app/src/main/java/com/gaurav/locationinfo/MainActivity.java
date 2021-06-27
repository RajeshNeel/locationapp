package com.gaurav.locationinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.gaurav.locationinfo.services.MyService;

public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Intent serviceIntent = new Intent(MainActivity.this, MyService.class);

        ContextCompat.startForegroundService(MainActivity.this, serviceIntent);

        Toast.makeText(this,"Service Started",Toast.LENGTH_SHORT).show();



    }
}