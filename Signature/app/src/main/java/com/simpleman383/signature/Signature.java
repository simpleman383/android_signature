package com.simpleman383.signature;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 29.11.2016.
 */

public class Signature
{
    private static final int Compression = 10;

    private Bitmap signatureBitmap;
    private int touches;
    private List <Long> timeOnTouch;

    private long maxTimeOnTouch;
    private long minTimeOnTouch;
    private long averageTimeOnTouch;
    private long totalTimeOnTouch;


    public Signature(Bitmap signatureBitmap, int touches, List<Long> timeOnTouch)
    {
        this.signatureBitmap = Bitmap.createScaledBitmap(signatureBitmap, signatureBitmap.getWidth()/Compression ,signatureBitmap.getHeight()/Compression, false);
        this.touches = touches;
        this.timeOnTouch = timeOnTouch;
        this.setTimeOnTouchValues();

    }

    public Bitmap getSignatureBitmap()
    {
        return this.signatureBitmap;
    }

    public int getTouches() {
        return this.touches;
    }


    public long getAverageTimeOnTouch() {
        return averageTimeOnTouch;
    }

    public long getTotalTimeOnTouch() {
        return totalTimeOnTouch;
    }

    public long getMinTimeOnTouch() {
        return minTimeOnTouch;
    }

    public long getMaxTimeOnTouch() {
        return maxTimeOnTouch;
    }

    private void setTimeOnTouchValues()
    {
        if  (timeOnTouch == null || timeOnTouch.isEmpty())
        {
            this.maxTimeOnTouch = 0;
            this.minTimeOnTouch = 0;
            this.averageTimeOnTouch = 0;
            this.totalTimeOnTouch = 0;
            return;
        }
        else
        {
            this.maxTimeOnTouch = this.timeOnTouch.get(0);
            this.minTimeOnTouch = this.timeOnTouch.get(0);

            this.totalTimeOnTouch = 0;

            for (Long interval : timeOnTouch) {
                if (interval > this.maxTimeOnTouch)
                    this.maxTimeOnTouch = interval;

                if (interval < this.minTimeOnTouch)
                    this.minTimeOnTouch = interval;

                this.totalTimeOnTouch += interval;
            }

            this.averageTimeOnTouch = this.totalTimeOnTouch / timeOnTouch.size();
        }
    }

}

