package kdevgroup.com.autoresponderapp.main;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

public class MainActivity extends MvpAppCompatActivity implements MainView {

    @InjectPresenter
    MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(Constants.TAG, "onCreate: ");

        checkPermissions();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handleResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        Log.i(Constants.TAG, "Main Activity onDestroy!");
        super.onDestroy();
    }
}
