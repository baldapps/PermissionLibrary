package com.balda.permissions;

import android.Manifest;
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
    public void onClick(View view) {
        Map<String, Integer> map = new HashMap<>();
        map.put(Manifest.permission.ACCESS_FINE_LOCATION, R.string.reason);
        if (permissionManager.requestPermissions(map, REQUEST)) {
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public PermissionManager getManager() {
        return permissionManager;
    }
}
