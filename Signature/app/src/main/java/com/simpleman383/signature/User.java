package com.simpleman383.signature;

import android.content.Context;
import android.util.Log;
import android.graphics.*;
import java.util.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 29.11.2016.
 */

public class User
{
    private String mUserName;

    private String CORPUS_FILE;
    private String SIGNATURE_VECTOR_CHARS;
    private String PERCEPTRON_WEIGHTS;
    private String CORPUS_2_FILE;

    private int SampleCounter;

    public User(String name, Context context){
        mUserName = name;
        CORPUS_FILE = name + "_CORPUS.txt";
        SIGNATURE_VECTOR_CHARS = name + "_SIGNATURE_VECTOR_CHARS.txt";

        CORPUS_2_FILE = name + "_CORPUS_2.txt";
        PERCEPTRON_WEIGHTS = name + "_PERCEPTRON_WEIGHTS.txt";


        SampleCounter = countSampleAmount(context);
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getCORPUS_FILE() {
        return CORPUS_FILE;
    }

    public int getSampleCounter(){
        return SampleCounter;
    }

    private int countSampleAmount(Context context){
        List data = new ArrayList<String>();

        try {
            InputStream inputStream = context.openFileInput(CORPUS_FILE);

            if (inputStream != null)
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = reader.readLine()) != null)
                {
                    data.add(line);
                }

                inputStream.close();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return data.size();
    }

    public String getSIGNATURE_VECTOR_CHARS() {
        return SIGNATURE_VECTOR_CHARS;
    }


    public String getPERCEPTRON_WEIGHTS() {
        return PERCEPTRON_WEIGHTS;
    }

    public String getCORPUS_2_FILE() {
        return CORPUS_2_FILE;
    }
}


