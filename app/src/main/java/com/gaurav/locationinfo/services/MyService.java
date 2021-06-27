package com.gaurav.locationinfo.services;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.gaurav.locationinfo.MainActivity;
import com.gaurav.locationinfo.R;
import com.gaurav.locationinfo.model.LocationDetails;
import com.gaurav.locationinfo.storage.LocationSharedPreference;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import java.util.Timer;
import java.util.TimerTask;

import static com.gaurav.locationinfo.ui.ApplicationClass.NOTIFICATION_CHANNEL_ID_SERVICE;

public class MyService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private static final int START_FOREGROUND_ID = 6;
    private final static int TIME_INTERVAL = 600000;
    private LocationDetails locationDetails;
    Handler handler;
    Timer timer = new Timer();
    TimerTask printLocationDetailsAfterHalfAnHour = new PrintLocationDetailsAfterHalfAnHour(MyService.this);



    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        buildGoogleApiClient();

        Log.v("MyService", "onCreate calling");

        timer.scheduleAtFixedRate(printLocationDetailsAfterHalfAnHour, 0, TIME_INTERVAL);


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //  return super.onStartCommand(intent, flags, startId);



        if (!googleApiClient.isConnected()){
            try {
                googleApiClient.connect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Log.v("MyService", "onStartCommand");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID_SERVICE)
                .setContentTitle("LocationInfo")
                .setContentText("Fetch location in background")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(START_FOREGROUND_ID, notification);

        if(intent!=null) {
            try {


            } catch (NullPointerException e2) {
                e2.getMessage();
                Log.v("MyService", "Null Pointer Exception Found:"+ e2.getMessage());
            }

        }
        return START_STICKY;
    }


    private void buildGoogleApiClient() {

        googleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

    }

    private void startLocationUpdate() {
        initLocationRequest();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)

                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    private void initLocationRequest() {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(TIME_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }



    @Override
    public void onLocationChanged(@NonNull Location location) {


        LocationDetails locationDetails = new LocationDetails(String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()),String.valueOf(location.getAccuracy()),
                String.valueOf(location.getBearing()),String.valueOf(location.getAltitude()),String.valueOf(location.getSpeed()),String.valueOf(location.getTime()));


        LocationSharedPreference.saveLocationObjectToSharedPreference(getApplicationContext(),"locationDetails",
                locationDetails);

     //   Toast.makeText(this, "location "+location.getLatitude()+" Longitude "+location.getLongitude(), Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onTaskRemoved(Intent rootIntent){
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);

        super.onTaskRemoved(rootIntent);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        startLocationUpdate();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        if(googleApiClient!=null){
            googleApiClient.connect();

        }
    }

    public Location getLocationDetails(Context mContext) {
        Location location = null;
        if (googleApiClient != null) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d("MyService","Location Permission Denied");
                return null;
            }else {
                location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            }
        }
        return location;
    }


    public class PrintLocationDetailsAfterHalfAnHour extends TimerTask {


        private Context context;
        private Handler mHandler = new Handler();

        public PrintLocationDetailsAfterHalfAnHour(Context con) {
            this.context = con;
        }



        @Override
        public void run() {
            new Thread(new Runnable() {

                public void run() {

                    mHandler.post(new Runnable() {
                        public void run() {

                            if(getLocationDetails(MyService.this)!=null){

                                LocationDetails locationDetails = LocationSharedPreference.getSavedLocationObjectFromPreference(getApplicationContext(),
                                        "locationDetails", LocationDetails.class);

                                Gson gson = new Gson();

                                String locationInJsonFormat = gson.toJson(locationDetails);

                                Toast.makeText(context, "Location Details "+locationInJsonFormat, Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
                }
            }).start();

        }

    }
}