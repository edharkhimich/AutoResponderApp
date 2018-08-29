package kdevgroup.com.autoresponderapp.common;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import kdevgroup.com.autoresponderapp.R;

import static kdevgroup.com.autoresponderapp.common.Constants.TAG;

@SuppressLint("OverrideAbstract")
public class NLService extends NotificationListenerService {

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        try {
            if (sbn.getNotification().actions != null) {
                for (Notification.Action action : sbn.getNotification().actions) {
                    if (action.title.toString().equalsIgnoreCase(getString(R.string.answer_ok))) {
                        PendingIntent intent = action.actionIntent;

                        try {
                            intent.send();
                        } catch (PendingIntent.CanceledException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {

    }

}
