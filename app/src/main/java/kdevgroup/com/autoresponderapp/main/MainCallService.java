package kdevgroup.com.autoresponderapp.main;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import kdevgroup.com.autoresponderapp.common.Constants;

import static kdevgroup.com.autoresponderapp.common.Constants.BROADCAST_ACTION;
import static kdevgroup.com.autoresponderapp.common.Constants.TAG;

public class MainCallService extends Service {

    public int counter = 0;

    private Timer timer;
    private TimerTask timerTask;
    private long oldTime = 0;

    public MainCallService(Context applicationContext) {
        super();
        Log.d(TAG, "CallService: constructor" );

    }

    public MainCallService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "ondestroy!");
        Intent broadcastIntent = new Intent(BROADCAST_ACTION);
        sendBroadcast(broadcastIntent);
        stoptimertask();
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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

