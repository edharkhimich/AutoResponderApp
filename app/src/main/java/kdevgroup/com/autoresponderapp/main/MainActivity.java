package kdevgroup.com.autoresponderapp.main;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;

import kdevgroup.com.autoresponderapp.R;
import kdevgroup.com.autoresponderapp.common.Constants;
import kdevgroup.com.autoresponderapp.common.MyTaskService;
import rebus.permissionutils.AskAgainCallback;
import rebus.permissionutils.PermissionEnum;
import rebus.permissionutils.PermissionManager;
import rebus.permissionutils.PermissionUtils;

import static kdevgroup.com.autoresponderapp.common.Constants.PERMISSIONS_REQUEST_READ_CONTACTS;

public class MainActivity extends MvpAppCompatActivity implements MainView {

    @InjectPresenter
    MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(Constants.TAG, "onCreate: ");

        checkPermissions();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("android.intent.action.PHONE_STATE");
//        intentFilter.setPriority(100);
//        CallReceiver callReceiver = new CallReceiver();
//        registerReceiver(callReceiver, intentFilter);
//        if(isPermissionsGranted()){
//            Intent serviceIntent = new Intent(this, MyTaskService.class);
//            ContextCompat.startForegroundService(this, serviceIntent);
//        }
    }

    private void checkPermissions() {
        PermissionManager.Builder()
                .permission(PermissionEnum.READ_PHONE_STATE, PermissionEnum.READ_CONTACTS)
                .askAgain(true)
                .askAgainCallback(this::showDialog)
                .callback((allPermissionsGranted, somePermissionsDeniedForever) -> {
                    if (allPermissionsGranted) {
                        Intent serviceIntent = new Intent(this, MyTaskService.class);
                        ContextCompat.startForegroundService(this, serviceIntent);
                    }
                    if (somePermissionsDeniedForever) {
                        Toast.makeText(getApplicationContext(),
                                "Until you grant the permission, the application willn't work correctly",
                                Toast.LENGTH_LONG).show();
                        PermissionUtils.openApplicationSettings(MainActivity.this, R.class.getPackage().getName());
                    }
                })
                .ask(this);

    }

    private void showDialog(final AskAgainCallback.UserResponse response) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Permission needed")
                .setMessage("This app realy need to use this permission, you wont to authorize it?")
                .setPositiveButton("OK", (dialogInterface, i) -> response.result(true))
                .setNegativeButton("NOT NOW", (dialogInterface, i) -> response.result(false))
                .setCancelable(false)
                .show();
    }

    private boolean isPermissionsGranted() {
        boolean permissionsGranted;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            Log.d(Constants.TAG, "loadContacts: ");
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            permissionsGranted = false;
        } else {
            Log.d("TAG", "isPermissionsGranted: ???");
            permissionsGranted = true;
        }

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                permissionsGranted = true;
            } else {
                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 2);
                permissionsGranted = false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            permissionsGranted = true;
        }
        return permissionsGranted;
    }

    private void is() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            Log.d(Constants.TAG, "loadContacts: ");
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            Intent serviceIntent = new Intent(this, MyTaskService.class);
            ContextCompat.startForegroundService(this, serviceIntent);
        }
    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case 2: {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
//                    //do ur specific task after read phone state granted
//                } else {
//                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
//                }
//                return;
//            }
//            case PERMISSIONS_REQUEST_READ_CONTACTS:
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                //TODO Register receiver programmaticaly
//                Log.d(Constants.TAG, "onRequestPermissionsResult: GRANTED" );
////                Intent serviceIntent = new Intent(this, MyTaskService.class);
////                ContextCompat.startForegroundService(this, serviceIntent);
//            } else {
//                //Todo Probably we need to start Toast in background
//                Toast.makeText(this, "Until you grant the permission, the application willn't work correctly", Toast.LENGTH_LONG).show();
//            }
//        }
//    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handleResult(this, requestCode, permissions, grantResults);
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
        Log.i(Constants.TAG, "Main Activity onDestroy!");
        super.onDestroy();
    }
}
