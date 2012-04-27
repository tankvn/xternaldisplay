
package com.googamaphone.xternaldisplay;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

public class XternalDisplay extends Activity {
    private static final String KEY_FIRST_RUN = "first_run";

    private static final int DIALOG_FIRST_RUN = 1;

    @Override
    public void onResume() {
        super.onResume();

        final SharedPreferences prefs = getPreferences(MODE_PRIVATE);

        if (prefs.getBoolean(KEY_FIRST_RUN, true)) {
            prefs.edit().putBoolean(KEY_FIRST_RUN, false).commit();
            showDialog(DIALOG_FIRST_RUN);
        } else {
            Toast.makeText(this, R.string.toggling, Toast.LENGTH_SHORT).show();
            toggleService();
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_FIRST_RUN:
                return new AlertDialog.Builder(this).setTitle(R.string.first_run_title)
                        .setMessage(R.string.first_run_message)
                        .setPositiveButton(android.R.string.ok, mDialogClickListener).create();
            default:
                return super.onCreateDialog(id);
        }
    }

    private void toggleService() {
        startService(new Intent(ConnectionService.ACTION));
        finish();
    }

    private final DialogInterface.OnClickListener mDialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    toggleService();
                    break;
            }
        }
    };
}
