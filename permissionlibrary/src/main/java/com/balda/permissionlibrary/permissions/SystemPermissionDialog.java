package com.balda.permissionlibrary.permissions;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.balda.permissionlibrary.R;

@SuppressWarnings("unused")
public class SystemPermissionDialog extends DialogFragment {

    public static final String TAG = "SystemPermissionDialog";

    public static SystemPermissionDialog newInstance(int text) {
        SystemPermissionDialog frag = new SystemPermissionDialog();
        Bundle args = new Bundle();
        args.putInt("text", text);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int text = getArguments().getInt("text");

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.system_dialog_title)
                .setCancelable(false)
                .setMessage(text)
                .setCancelable(false)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(R.string.settings,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                PermissionReceiver.openSystemDialog(getActivity());
                            }
                        }
                ).create();
    }
}
