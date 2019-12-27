package com.m.openthedoorapp.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.m.openthedoorapp.R;

import androidx.appcompat.app.AlertDialog;


/**
 * Created by Mahmoud on 10/16/17.
 */

public class DialogUtil {

    public DialogUtil() {
    }

    public static ProgressDialog showProgressDialog(Context context, String message, boolean cancelable) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(message);
        dialog.setCancelable(cancelable);
        dialog.show();
        return dialog;
    }
}
