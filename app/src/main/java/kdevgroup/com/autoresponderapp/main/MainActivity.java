package kdevgroup.com.autoresponderapp.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import kdevgroup.com.autoresponderapp.R;
import kdevgroup.com.autoresponderapp.common.MyTaskService;
import rebus.permissionutils.AskAgainCallback;
import rebus.permissionutils.PermissionEnum;
import rebus.permissionutils.PermissionManager;
import rebus.permissionutils.PermissionUtils;

import static kdevgroup.com.autoresponderapp.common.Constants.ACTION_NOTIFICATION_LISTENER_SETTINGS;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkAutoAnswerPermission();
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
                                getString(R.string.permission_denied_warning),
                                Toast.LENGTH_LONG).show();
                        PermissionUtils.openApplicationSettings(MainActivity.this, R.class.getPackage().getName());
                    }
                })
                .ask(this);
    }

    public void checkAutoAnswerPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ANSWER_PHONE_CALLS) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.MODIFY_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ANSWER_PHONE_CALLS}, 200);
            }
        } else {
            Intent intent = new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS);
            startActivity(intent);
        }
    }

    private void showDialog(final AskAgainCallback.UserResponse response) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(getString(R.string.permission_needed))
                .setMessage(getString(R.string.permission_realy_need_permission))
                .setPositiveButton(getString(R.string.answer_ok), (dialogInterface, i) -> response.result(true))
                .setNegativeButton(getString(R.string.not_info), (dialogInterface, i) -> response.result(false))
                .setCancelable(false)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handleResult(this, requestCode, permissions, grantResults);
    }
}
