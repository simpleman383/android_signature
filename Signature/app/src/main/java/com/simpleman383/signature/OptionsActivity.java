package com.simpleman383.signature;

import android.support.v4.app.Fragment;

/**
 * Created by Alex on 29.11.2016.
 */

public class OptionsActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment()
    {
        return OptionsFragment.newInstance();
    }

}
