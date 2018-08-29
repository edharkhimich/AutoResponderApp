package kdevgroup.com.autoresponderapp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import java.util.Objects;

import kdevgroup.com.autoresponderapp.common.MyTaskService;

import static kdevgroup.com.autoresponderapp.common.Constants.NUMBER_KEY;
import static kdevgroup.com.autoresponderapp.common.Constants.PHONE_ACTION_STATE;

public class CallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Objects.requireNonNull(intent.getAction()).equals(PHONE_ACTION_STATE)) {

            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                sendIncomingNumberToService(context, number);
            }
        }
    }

    private void sendIncomingNumberToService(Context context, String number) {
        Intent serviceIntent = new Intent(context, MyTaskService.class);
        serviceIntent.putExtra(NUMBER_KEY, number);
        context.startService(serviceIntent);
    }
}


