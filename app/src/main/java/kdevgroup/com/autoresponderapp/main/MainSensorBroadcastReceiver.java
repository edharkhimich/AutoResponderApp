package kdevgroup.com.autoresponderapp.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static kdevgroup.com.autoresponderapp.common.Constants.TAG;

public class MainSensorBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Service Stops! Oooooooooooooppppssssss!!!!");
        context.startService(new Intent(context, MainCallService.class));;
    }

}
