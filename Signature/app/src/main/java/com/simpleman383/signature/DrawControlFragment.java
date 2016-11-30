package com.simpleman383.signature;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.LocaleDisplayNames;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.support.v4.graphics.BitmapCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.simpleman383.signature.DrawActivity.CURRENT_USER;

/**
 * Created by Alex on 29.11.2016.
 */

public class DrawControlFragment extends Fragment {

    public static DrawControlFragment newInstance() {
        return new DrawControlFragment();
    }


    private Button mOptions;
    private Button mDone;
    private Button mClear;
    private CanvasView mCanvasView;

    private User curUser;
    boolean NEWBYE_MODE;
    private int exampleRemain = 10;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.draw_fragment, container, false);


        if (this.getArguments() != null)
        {
            curUser = new User((String)this.getArguments().getSerializable(DrawActivity.CURRENT_USER), getContext());
        }
        else
        {
            String name = (String) getActivity().getIntent().getSerializableExtra(DrawActivity.CURRENT_USER); //decide whether the user is new
            curUser = new User(name,  getContext());
        }

        NEWBYE_MODE = IsCurrentUserNew(curUser, getContext());


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
                ResetFragment();
            }
        });


        mDone = (Button)v.findViewById(R.id.done_button);
        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (NEWBYE_MODE)
                {
                    //getData
                    //write data in User_Corpus_File and mark it true
                    //SignatureUtils.WriteFile("tratata",getContext(), curUser.getCORPUS_FILE());

                    exampleRemain--;
                    if (exampleRemain == 0)
                        NEWBYE_MODE = false;
                    Toast.makeText(getContext(), String.valueOf(exampleRemain) + " examples remain", Toast.LENGTH_SHORT).show();
                    if (exampleRemain == 0)
                        Toast.makeText(getContext(), "Enough. Now write again", Toast.LENGTH_SHORT).show();
                    ResetFragment();
                }
                else
                {
                    //getData
                    //write data in User_Corpus_File
                    //classify

                    //show a dialog window that asks whether the decision was correct
                }


            }
        });


        return v;
    }



    private boolean IsCurrentUserNew(User curUser, Context context)
    {
        List<String> existingCorpus = SignatureUtils.ReadFile(curUser.getCORPUS_FILE(), context);

        if (existingCorpus.size() < 10)
        {
            exampleRemain = 10 - existingCorpus.size();
            Toast.makeText(context, "Give an example of your signature "+String.valueOf(exampleRemain)+" times", Toast.LENGTH_SHORT).show();
            return true;
        }
        else
        {
            return false;
        }
    }


    private void ResetFragment()
    {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        DrawControlFragment drawFragment = DrawControlFragment.newInstance();

        Bundle args = new Bundle();
        args.putSerializable(CURRENT_USER, curUser.getUserName());
        drawFragment.setArguments(args);

        manager.beginTransaction().replace(R.id.fragmentContainer, drawFragment).commit();
        return;
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
