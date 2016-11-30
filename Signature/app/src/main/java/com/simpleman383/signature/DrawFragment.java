package com.simpleman383.signature;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Alex on 28.11.2016.
 */

public class DrawFragment extends Fragment {

    private static String TIMER = "TIMER";


    public static DrawFragment newInstance() {
        return new DrawFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.draw_fragment, container, false);
        return v;
    }




}
