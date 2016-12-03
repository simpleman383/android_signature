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
    private int SampleCounter;

    public User(String name, Context context){
        mUserName = name;
        CORPUS_FILE = name + "_CORPUS.txt";
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


}


