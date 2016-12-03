package com.simpleman383.signature;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by Alex on 01.12.2016.
 */

public class ResultFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle bundle)
    {
        return new AlertDialog.Builder(getActivity())
                .setTitle("RESULT\nIs it correct?")
                .setPositiveButton("Yes", null)
                .setNegativeButton("No", null)
                .create();
    }

}
