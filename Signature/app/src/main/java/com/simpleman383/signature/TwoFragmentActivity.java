package com.simpleman383.signature;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Alex on 29.11.2016.
 */

public abstract class TwoFragmentActivity extends AppCompatActivity{
    protected abstract Fragment createFragment_1();
    protected abstract Fragment createFragment_2();


    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_few_fragment);

        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment_1 = manager.findFragmentById(R.id.fragmentContainer1);
        Fragment fragment_2 = manager.findFragmentById(R.id.fragmentContainer2);

        if (fragment_1 == null)
        {
            fragment_1 = createFragment_1();
            manager.beginTransaction().add(R.id.fragmentContainer1, fragment_1).commit();
        }

        if (fragment_2 == null)
        {
            fragment_2 = createFragment_2();
            manager.beginTransaction().add(R.id.fragmentContainer2, fragment_2).commit();
        }


    }
}
