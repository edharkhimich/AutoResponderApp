package kdevgroup.com.autoresponderapp.main;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;

import kdevgroup.com.autoresponderapp.R;
import kdevgroup.com.autoresponderapp.common.Constants;

public class MainActivity extends MvpAppCompatActivity implements MainView {

    @InjectPresenter
    MainPresenter presenter;

    private Intent mServiceIntent;
    private MainCallService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        service = new MainCallService(getApplicationContext());
        mServiceIntent = new Intent(getApplicationContext(), service.getClass());
        if (!isMyServiceRunning(service.getClass())) {
            startService(mServiceIntent);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i(Constants.TAG, true + "");
                return true;
            }
        }
        Log.i(Constants.TAG, false + "");
        return false;
    }


    @Override
    protected void onDestroy() {
        stopService(mServiceIntent);
        Log.i(Constants.TAG, "onDestroy!");
        super.onDestroy();

    }
}
