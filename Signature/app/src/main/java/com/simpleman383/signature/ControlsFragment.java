package com.simpleman383.signature;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * Created by Alex on 29.11.2016.
 */

public class ControlsFragment extends Fragment {

    public static ControlsFragment newInstance() {
        ControlsFragment control_fragment = new ControlsFragment();
        return  control_fragment;

    }

    private static int TIMER = 0;

    private Button mOptions;
    private Button mDone;
    private Button mClear;


    private static String CLEAR_REQUEST = "CLEAR_REQUEST";




    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.controls_fragment, container, false);


        mOptions = (Button)v.findViewById(R.id.options_button);
        mOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), OptionsActivity.class);
                startActivity(intent);
            }
        });


        mClear = (Button)v.findViewById(R.id.clear_button);
        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                DrawFragment drawFragment = DrawFragment.newInstance();
                manager.beginTransaction().replace(R.id.fragmentContainer1, drawFragment).commit();
            }
        });


        mDone = (Button)v.findViewById(R.id.done_button);
        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();

                DrawFragment draw_frag = DrawFragment.newInstance();

                draw_frag.setTargetFragment(ControlsFragment.this, TIMER);
                manager.beginTransaction().replace(R.id.fragmentContainer1, draw_frag);
            }
        });


        return v;
    }




}
