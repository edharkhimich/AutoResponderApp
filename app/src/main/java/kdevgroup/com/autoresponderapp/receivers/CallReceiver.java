package kdevgroup.com.autoresponderapp.receivers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Date;

import kdevgroup.com.autoresponderapp.main.MainCallService;

import static kdevgroup.com.autoresponderapp.common.Constants.NUMBER_KEY;
import static kdevgroup.com.autoresponderapp.common.Constants.TAG;

public class CallReceiver extends PhoneCallReceiver {

    @Override
    protected void onIncomingCallStarted(Context ctx, String number, Date start) {
        Log.d(TAG, "onIncomingCallStarted: " + number);
        Intent intent = new Intent(ctx, MainCallService.class);
        intent.putExtra(NUMBER_KEY, number);
        ctx.startService(intent);
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {
    }

}
