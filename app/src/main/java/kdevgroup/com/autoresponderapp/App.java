package kdevgroup.com.autoresponderapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import kdevgroup.com.autoresponderapp.common.Constants;

import static kdevgroup.com.autoresponderapp.common.Constants.NOTIFICATION_CHANNEL_ID;
import static kdevgroup.com.autoresponderapp.common.Constants.NOTIFICATION_CHANNEL_NAME;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
    }


}
