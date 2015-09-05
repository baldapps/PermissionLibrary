package com.balda.permissionlibrary.permissions;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.StringRes;
import android.support.v4.app.NotificationCompat;

import com.balda.permissionlibrary.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class PermissionManager {

    private Activity activity;
    private static final int NOTIFICATION_ID = 1;

    public PermissionManager(Activity a) {
        this.activity = a;
    }

    /**
     * Internal callback
     * @param permissions Permissions list
     * @param request A request code
     */
    void onReasonAccepted(ArrayList<String> permissions, int request) {
        activity.requestPermissions(permissions.toArray(new String[permissions.size()]), request);
    }

    /**
     * Check if permissions are granted
     * @param perms Permissions list
     * @return True if all the permissions are granted, false otherwise
     */
    public boolean checkPermissions(String[] perms) {
        boolean res = true;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        for (String p : perms) {
            if (PackageManager.PERMISSION_GRANTED != activity.checkSelfPermission(p)) {
                res = false;
            }
        }
        return res;
    }

    /**
     * Request permissions
     * @param permissionMap A permission map
     * @param requestCode A request code
     * @return True if all the permissions are granted, false otherwise
     */
    public boolean requestPermissions(Map<String, Integer> permissionMap, int requestCode) {
        boolean res = true;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || permissionMap == null || permissionMap.size() == 0) {
            return true;
        }

        ArrayList<String> permToBeRequest = new ArrayList<>();
        ArrayList<Integer> reasonsToBeViewed = new ArrayList<>();

        for (String p : permissionMap.keySet()) {
            if (PackageManager.PERMISSION_GRANTED != activity.checkSelfPermission(p)) {
                res = false;
                permToBeRequest.add(p);
                if (activity.shouldShowRequestPermissionRationale(p) && permissionMap.get(p) != 0) {
                    reasonsToBeViewed.add(permissionMap.get(p));
                }
            }
        }
        if (!res) {
            if (reasonsToBeViewed.size() > 0) {
                PermissionDialog newFragment = PermissionDialog.newInstance(reasonsToBeViewed, permToBeRequest,
                        requestCode);
                newFragment.show(activity.getFragmentManager(), PermissionDialog.TAG);
            } else {
                onReasonAccepted(permToBeRequest, requestCode);
            }
        }
        return res;
    }

    /**
     * Helper method to build a permission map with no "reasons"
     * @param perms Permissions list
     * @return A map where the resources have always id 0
     */
    public static Map<String, Integer> generatePermissionMap(String[] perms) {
        HashMap<String, Integer> map = new HashMap<>();

        if (perms.length == 0)
            throw new IllegalArgumentException();

        for (String s : perms) {
            map.put(s, 0);
        }
        return map;
    }

    /**
     * Request permission from service or broadcast receiver
     * @param context A context
     * @param perms Permissions list
     * @param text Notification text
     * @return True if the permission is granted, false otherwise
     */
    public static boolean requestPermissions(Context context, String[] perms, @StringRes int text) {
        boolean res = true;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        for (String p : perms) {
            if (PackageManager.PERMISSION_GRANTED != context.checkSelfPermission(p)) {
                res = false;
                NotificationManager manager = (NotificationManager) context.getSystemService(Context
                        .NOTIFICATION_SERVICE);
                PendingIntent pi = PendingIntent.getBroadcast(context, 0, new Intent(context, PermissionReceiver.class),
                        PendingIntent.FLAG_ONE_SHOT);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                        .setAutoCancel(true)
                        .setColor(Color.RED)
                        .setSmallIcon(R.drawable.ic_notification_icon)
                        .setContentTitle(context.getString(R.string.notification_title))
                        .setTicker(context.getString(text))
                        .setContentIntent(pi)
                        .setContentText(context.getString(text));
                manager.notify(NOTIFICATION_ID, mBuilder.build());
                break;
            }
        }
        return res;
    }
}
