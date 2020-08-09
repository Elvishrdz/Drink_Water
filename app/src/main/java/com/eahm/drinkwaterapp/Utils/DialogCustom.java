package com.eahm.drinkwaterapp.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.eahm.drinkwaterapp.Interfaces.Callbacks;

public class DialogCustom {

    public AlertDialog.Builder getBuilder() {
        return builder;
    }

    AlertDialog.Builder builder;
    DialogInterface.OnClickListener dialogClickListener;

    public void displayAlertDialogYesNo(Activity activity, final Callbacks callback, final String CODE, String message, String yesText, String noText, boolean isCancelable){

        if(builder != null) return;

        builder = new AlertDialog.Builder(activity);
        dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        callback.onYesResponse(CODE,"");
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        callback.onNoResponse(CODE, "");
                        break;
                }
            }
        };

        builder.setMessage(message)
                .setPositiveButton(yesText, dialogClickListener)
                .setNegativeButton(noText, dialogClickListener)
                .setCancelable(isCancelable)
                .show();
    }



}
