package com.eahm.drinkwaterapp.Class;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.eahm.drinkwaterapp.R;

public class DialogCustomClass extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        builder.setView(layoutInflater.inflate(R.layout.layout_select_glasses, null));

        /*
        builder.setPositiveButton("POSITIVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("NEGATIVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        })
        */

        //USO:
        //DialogCustomClass dialogCustomClass = new DialogCustomClass();
        //dialogCustomClass.show(getSupportFragmentManager(), "DIALOG");


        //return super.onCreateDialog(savedInstanceState);
        return builder.create();
    }
}
