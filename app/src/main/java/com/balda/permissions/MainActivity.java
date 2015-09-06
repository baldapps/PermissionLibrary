package com.balda.permissions;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.balda.permissionlibrary.permissions.PermissionChecker;
import com.balda.permissionlibrary.permissions.PermissionManager;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, PermissionChecker {

    private PermissionManager permissionManager;
    public static final int REQUEST = 1;

    public MainActivity() {
        permissionManager = new PermissionManager(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b = (Button) findViewById(R.id.button);
        b.setOnClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != REQUEST || grantResults.length == 0)
            return;
        for (int i : grantResults) {
            if (i != PackageManager.PERMISSION_GRANTED)
                return;
        }
        businessLogic();
    }

    @Override
    public void onClick(View view) {
        Map<String, Integer> map = new HashMap<>();
        map.put(Manifest.permission.ACCESS_FINE_LOCATION, R.string.reason);
        if (permissionManager.requestPermissions(map, REQUEST)) {
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            businessLogic();
        } else {
            //we don't call businessLogic() here we wait for the permission callback
            Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public PermissionManager getManager() {
        return permissionManager;
    }

    private void businessLogic() {
        //do something here
    }
}
