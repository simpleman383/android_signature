package com.simpleman383.signature;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.EventListener;

public class DrawActivity extends SingleFragmentActivity{

    public static final String CURRENT_USER = "CURRENT_USER";
    public static final String SELECTED_CLASSIFIER = "SELECTED_CLASSIFIER";

    public static Intent newIntent(Context packageContext, User user, String selected_classifier)
    {
        Intent intent = new Intent(packageContext, DrawActivity.class);
        intent.putExtra(CURRENT_USER, user.getUserName());
        intent.putExtra(SELECTED_CLASSIFIER, selected_classifier);
        return intent;
    }


    @Override
    public Fragment createFragment()
    {
        return DrawControlFragment.newInstance();
    }

}
