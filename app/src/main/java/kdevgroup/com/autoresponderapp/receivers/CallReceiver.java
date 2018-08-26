package kdevgroup.com.autoresponderapp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import kdevgroup.com.autoresponderapp.common.MyTaskService;

import static kdevgroup.com.autoresponderapp.common.Constants.NUMBER_KEY;
import static kdevgroup.com.autoresponderapp.common.Constants.TAG;

public class CallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: start");
//
//        Intent intent = new Intent(context, MyTaskService.class);
//        intent.putExtra(NUMBER_KEY, number);
//        Log.d(TAG, "onIncomingCallStarted: " + number);
//
//        ContextCompat.startForegroundService(ctx, intent);
    }

//    @Override
//    protected void onIncomingCallStarted(Context ctx, String number, Date start) {
//        super.onIncomingCallStarted(ctx, number, start);
//
//        Intent intent = new Intent(ctx, MyTaskService.class);
//        intent.putExtra(NUMBER_KEY, number);
//        Log.d(TAG, "onIncomingCallStarted: " + number);
//
//        ContextCompat.startForegroundService(ctx, intent);
//
//    }
}

