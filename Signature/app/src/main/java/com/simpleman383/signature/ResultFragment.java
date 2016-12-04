package com.simpleman383.signature;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.io.File;


/**
 * Created by Alex on 01.12.2016.
 */

public class ResultFragment extends DialogFragment
{
    public static final String CURRENT_USER = "CURRENT_USER";
    public static final String RESULT = "RESULT";
    public static final String DATA = "DATA";


    private User user;
    private String result = "";
    private String oppositeResult = "";
    private String data = "";


    public static ResultFragment newInstance(String user, String result, String data) {
        Bundle args = new Bundle();
        args.putString(CURRENT_USER, user);
        args.putString(DATA, data);
        args.putString(RESULT, result);
        ResultFragment fragment = new ResultFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle bundle)
    {
        String userName =   (String)getArguments().getString(CURRENT_USER);
        user = new User(userName, getContext());
        result = (String) getArguments().getString(RESULT);
        data = (String) getArguments().getString(DATA);


        if (result.equals("true"))
            oppositeResult = "false";
        else
            oppositeResult = "true";


        return new AlertDialog.Builder(getActivity())
                .setTitle(result.toUpperCase())
                .setMessage("Is it correct?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SignatureUtils.WriteFile(data + "," + result, getContext(), user.getCORPUS_FILE());
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SignatureUtils.WriteFile(data + "," + oppositeResult, getContext(), user.getCORPUS_FILE());
                    }
                })
                .create();
    }

}
