package com.simpleman383.signature;

/**
 * Created by Alex on 29.11.2016.
 */
import android.graphics.*;
import java.util.*;

public class Signature {
    private Bitmap signatureBitmap;
    private int touches;
    private List <Long> timeOnTouch;


    public Signature(Bitmap signatureBitmap, int touches, List<Long> timeOnTouch){
        this.signatureBitmap = signatureBitmap;
        this.touches = touches;
        this.timeOnTouch = timeOnTouch;
    }

    public Bitmap getSignatureBitmap(){
        return this.signatureBitmap;
    }

    public int getTouches(){
        return this.touches;
    }

    public List <Long> getTimeOnTouch(){
        return this.timeOnTouch;
    }
}
