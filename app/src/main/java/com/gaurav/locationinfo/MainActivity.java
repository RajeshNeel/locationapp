package com.gaurav.locationinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.gaurav.locationinfo.services.MyService;

public class MainActivity extends AppCompatActivity {

    Location location;
    TextView textView;
    private Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textView = findViewById(R.id.locationDetailsTextView);

        Intent serviceIntent = new Intent(MainActivity.this, MyService.class);

        ContextCompat.startForegroundService(MainActivity.this, serviceIntent);

        Toast.makeText(this,"Service Started",Toast.LENGTH_SHORT).show();


        location = new MyService().getLocationDetails(MainActivity.this);

        try {
            if(location!=null){

                textView.setText("Location latitude: "+location.getLatitude()+" longitude :"+location.getLongitude());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}