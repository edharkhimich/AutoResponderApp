package kdevgroup.com.autoresponderapp.common;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import kdevgroup.com.autoresponderapp.R;
import kdevgroup.com.autoresponderapp.main.MainActivity;
import kdevgroup.com.autoresponderapp.receivers.CallReceiver;

import static kdevgroup.com.autoresponderapp.common.Constants.NOTIFICATION_CHANNEL_ID;
import static kdevgroup.com.autoresponderapp.common.Constants.TAG;

public class MyTaskService extends Service {

    private String phoneNr;


    public int counter = 0;

    private Timer timer;
    private TimerTask timerTask;

    private boolean contactNumberCalling() {
        boolean contactNumber = false;
        for (String number : getContactNumbers()) {
            if (number.equals(phoneNr)) {
                contactNumber = true;
                break;
            } else {
                Log.d(TAG, "Unknown Nubmer calling | number --> " + number);
            }
        }
        return contactNumber;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

//            phoneNr = intent.getStringExtra(NUMBER_KEY);
        Log.d(TAG, "onReceive: phone number " + phoneNr);

        startTimer();
        startCallReceiver();
        startForegroundService();



//        if (contactNumberCalling()) {
//            Log.d(TAG, "onReceive: Contact number calling");
//            //TODO Start recording message
//            startForegroundService();
//        } else {
//            Log.d(TAG, "onReceive: Calling unknown number");
//        }
//        }

        return START_NOT_STICKY;
    }

    private void startCallReceiver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PHONE_STATE");
        intentFilter.setPriority(100);
        CallReceiver callReceiver = new CallReceiver();
        registerReceiver(callReceiver, intentFilter);
    }

    private Set<String> getContactNumbers() {
        Set<String> contacts = new HashSet<>();

        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cursor.moveToFirst()) {
            String contactId =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

            Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
            while (phones.moveToNext()) {
                String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                Log.d(Constants.TAG, "getContactNames: " + number);
            }
            phones.close();
        }
        cursor.close();

        return contacts;
    }

    private void startForegroundService() {
        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .setContentTitle("Incoming number: " + phoneNr)
                .setContentText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
                .setContentInfo("Info")
                .build();

        startForeground(1, notification);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        initializeTimerTask();

        timer.schedule(timerTask, 1000, 1000); //
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Log.i(TAG, "in timer ++++  " + (counter++));
            }
        };
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stoptimertask();
    }
}
