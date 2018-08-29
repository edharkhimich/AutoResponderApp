package kdevgroup.com.autoresponderapp.common;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.session.MediaController;
import android.media.session.MediaSessionManager;
import android.os.Build;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.telecom.TelecomManager;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.KeyEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import kdevgroup.com.autoresponderapp.R;
import kdevgroup.com.autoresponderapp.main.MainActivity;
import kdevgroup.com.autoresponderapp.receivers.CallReceiver;

import static kdevgroup.com.autoresponderapp.common.Constants.NOTIFICATION_CHANNEL_ID;
import static kdevgroup.com.autoresponderapp.common.Constants.NUMBER_KEY;
import static kdevgroup.com.autoresponderapp.common.Constants.PHONE_ACTION_STATE;
import static kdevgroup.com.autoresponderapp.common.Constants.TAG;
import static kdevgroup.com.autoresponderapp.common.Constants.TELECOM_PACKAGE_NAME;

public class MyTaskService extends Service {

    private String phoneNr;

    private CallReceiver callReceiver;

    private boolean contactNumberCalling() {
        boolean contactNumber = false;
        for (String number : getContactNumbers()) {
            if (PhoneNumberUtils.compare(getApplicationContext(), number, phoneNr)) {
                contactNumber = true;
                break;
            }
        }
        return contactNumber;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if (intent.getStringExtra(NUMBER_KEY) != null) {
            phoneNr = intent.getStringExtra(NUMBER_KEY);
            if (contactNumberCalling()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    answerAutoCall();
                } else {
                    sendHeadsetHookLollipop();
                }
                //TODO detect voice
            }
        }
        startCallReceiver();

        // ToDo Maybe add inc number to notification
        startForegroundService();

        return START_NOT_STICKY;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    void sendHeadsetHookLollipop() {
        MediaSessionManager mediaSessionManager = (MediaSessionManager) getApplicationContext().getSystemService(Context.MEDIA_SESSION_SERVICE);

        try {
            assert mediaSessionManager != null;
            List<MediaController> mediaControllerList = mediaSessionManager.getActiveSessions
                    (new ComponentName(getApplicationContext(), NotificationReceiverService.class));

            for (MediaController m : mediaControllerList) {
                if (TELECOM_PACKAGE_NAME.equals(m.getPackageName())) {
                    m.dispatchMediaButtonEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
                    break;
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void answerAutoCall() {
        TelecomManager tm = (TelecomManager) getSystemService(Context.TELECOM_SERVICE);
        if (tm == null) {
            // TODO Show Toast - Something goes wrong. Try again later
            throw new NullPointerException("tm == null");
        }
        tm.acceptRingingCall();
    }

    private void startCallReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        callReceiver = new CallReceiver();

        intentFilter.addAction(PHONE_ACTION_STATE);
        intentFilter.setPriority(100);

        registerReceiver(callReceiver, intentFilter);
    }

    private Set<String> getContactNumbers() {
        Set<String> contacts = new HashSet<>();

        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (Objects.requireNonNull(cursor).moveToFirst()) {
            String contactId =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

            Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
            assert phones != null;
            while (phones.moveToNext()) {
                String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contacts.add(number);
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
                .setContentTitle(getString(R.string.not_incoming_number) + phoneNr)
                .setContentText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
                .setContentInfo(getString(R.string.not_info))
                .build();

        startForeground(1, notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
