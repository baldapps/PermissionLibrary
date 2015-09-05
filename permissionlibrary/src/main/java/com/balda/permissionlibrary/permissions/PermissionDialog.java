package com.balda.permissionlibrary.permissions;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.balda.permissionlibrary.R;

import java.util.ArrayList;

public class PermissionDialog extends DialogFragment {

    public static final String TAG = "PermissionDialog";
    private ArrayList<String> permissions;
    private int request;
    private PermissionChecker checker;

    public static PermissionDialog newInstance(ArrayList<Integer> text, ArrayList<String> perms, int req) {
        PermissionDialog frag = new PermissionDialog();
        Bundle args = new Bundle();
        args.putIntegerArrayList("texts", text);
        args.putInt("req", req);
        args.putStringArrayList("perms", perms);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            checker = (PermissionChecker) getActivity();
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Activity doesn't implement PermissionChecker");
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            checker = (PermissionChecker) activity;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Activity doesn't implement PermissionChecker");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        checker = null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String message = "";
        ArrayList<Integer> texts = getArguments().getIntegerArrayList("texts");
        permissions = getArguments().getStringArrayList("perms");
        request = getArguments().getInt("req");

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.dialog_title)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if (checker != null && checker.getManager() != null) {
                                    checker.getManager().onReasonAccepted(permissions, request);
                                }
                            }
                        }
                );

        if (texts == null)
            throw new IllegalArgumentException();

        for (int i = 0; i < texts.size(); ++i) {
            if (i == texts.size() - 1)
                message += getString(texts.get(i));
            else
                message += getString(texts.get(i)) + "\n";
        }
        b.setMessage(message);
        return b.create();
    }
}
