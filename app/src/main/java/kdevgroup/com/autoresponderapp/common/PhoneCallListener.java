package kdevgroup.com.autoresponderapp.common;

import android.database.Cursor;
import android.os.Handler;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import static kdevgroup.com.autoresponderapp.common.Constants.TAG;

public class PhoneCallListener extends PhoneStateListener {

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {

        if (TelephonyManager.CALL_STATE_RINGING == state) {
            // phone ringing
            Log.i(TAG, "RINGING, number: " + incomingNumber);
        }
    }
}
