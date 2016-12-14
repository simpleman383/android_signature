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
    public static final String MODE = "MODE";


    private User user;
    private String result = "";
    private String oppositeResult = "";
    private String data = "";
    private String mode = "";

    private String result_bit = ""; //result as number: true=1/false=0
    private String opposite_result_bit = "";


    public static ResultFragment newInstance(String user, String result, String data, String mode) {
        Bundle args = new Bundle();

        args.putString(CURRENT_USER, user);
        args.putString(DATA, data);
        args.putString(RESULT, result);
        args.putString(MODE, mode);

        ResultFragment fragment = new ResultFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle bundle)
    {
        String userName =  (String)getArguments().getString(CURRENT_USER);
        user = new User(userName, getContext());
        result = (String) getArguments().getString(RESULT);
        data = (String) getArguments().getString(DATA);
        mode = (String) getArguments().getString(MODE);

        if (result.isEmpty())
        {
            result = "false";
            result_bit = "0";
        }




        if (result.equals("true")) {
            result_bit = "1";
            opposite_result_bit = "0";
            oppositeResult = "false";
        }
        else {
            oppositeResult = "true";
            result_bit = "0";
            opposite_result_bit = "1";
        }


        if (!mode.equals("4-Layer Perceptron Network")) {
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
        else
        {
            return new AlertDialog.Builder(getActivity())
                    .setTitle(result.toUpperCase())
                    .setMessage("Is it correct?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SignatureUtils.WriteFile(data +  result_bit, getContext(), user.getCORPUS_2_FILE());
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SignatureUtils.WriteFile(data + "," + opposite_result_bit, getContext(), user.getCORPUS_2_FILE());
                            FourLayerPerceptronNetwork nw = new FourLayerPerceptronNetwork(user, getContext());
                        }
                    })
                    .create();
        }

    }

}
