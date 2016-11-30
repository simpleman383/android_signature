package com.simpleman383.signature;

import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.LocaleDisplayNames;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.BitmapCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 29.11.2016.
 */

public class DrawControlFragment extends Fragment {

    public static DrawControlFragment newInstance() {
        return new DrawControlFragment();
    }

    private static int TIMER = 0;

    private Button mOptions;
    private Button mDone;
    private Button mClear;
    private CanvasView mCanvasView;

    private static String CLEAR_REQUEST = "CLEAR_REQUEST";

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.draw_fragment, container, false);


        mOptions = (Button)v.findViewById(R.id.options_button);
        mOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), OptionsActivity.class);
                startActivity(intent);
            }
        });

        mCanvasView = (CanvasView)v.findViewById(R.id.canvas_view);

        mClear = (Button)v.findViewById(R.id.clear_button);
        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                DrawControlFragment drawFragment = DrawControlFragment.newInstance();
                manager.beginTransaction().replace(R.id.fragmentContainer, drawFragment).commit();
            }
        });


        mDone = (Button)v.findViewById(R.id.done_button);
        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetSignatureParams();
            }
        });


        return v;
    }




    public void GetSignatureParams()
    {
        Bitmap signatureBitmap = mCanvasView.getBitmap();
        int touches = mCanvasView.getTouchCounter();
        List <Long> timeOnTouch = mCanvasView.getTimePeriodOnTouch();

       String tt = "";

        for (int x=0; x<signatureBitmap.getWidth(); x+=7)
        {
            for (int y=0; y<signatureBitmap.getHeight(); y+=7) {
                tt = tt + String.valueOf(signatureBitmap.getPixel(x, y)) + ", ";
            }
        }

        Log.i("DATA_SET: ", tt);
       /// SignatureUtils.WriteFile(tt ,getContext());


    }













}
