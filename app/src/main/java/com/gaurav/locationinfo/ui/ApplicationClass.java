package com.gaurav.locationinfo.ui;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

/**
 * Created by Gaurav Sharma on 27-06-2021 on 23:54 .
 */
public class ApplicationClass extends Application {

    public static final String NOTIFICATION_CHANNEL_ID_SERVICE = "com.gaurav.locationinfo";

    public static final String CHANNEL_ID = "gaurav-channel";

    @Override
    public void onCreate() {
        super.onCreate();

        initChannel();

    }

    public void initChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.createNotificationChannel(new NotificationChannel(NOTIFICATION_CHANNEL_ID_SERVICE, "App Service", NotificationManager.IMPORTANCE_DEFAULT));
        }
    }

}
