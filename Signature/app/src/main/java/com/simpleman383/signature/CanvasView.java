package com.simpleman383.signature;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 28.11.2016.
 */

public class CanvasView extends View {

    private Canvas mCanvas;     //for drawing
    private PointF mCurrentPoint;
    private Paint mSignaturePaint;
    private Paint mBackgroundPaint;
    private List<PointF> mSignatureControlPoints = new ArrayList<>();
    private List<PointF> mSignatureActionUpPoints = new ArrayList<>();

    private Bitmap mSignature;
    private Bitmap centeredSignature = null;

    private int mTouchCounter;

    private ArrayList<Long> TimePeriodOnTouch = new ArrayList<>(); //on touch time measure
    private long timeStart;
    private long periodOnTouch;

    private ArrayList<Long> timesOfGettingPoints = new ArrayList<>();

    private ArrayList<Double> WritingVelocityProjectionsX = new ArrayList<>();
    private ArrayList<Double> WritingVelocityProjectionsY = new ArrayList<>();


    public CanvasView(Context context)
    {
        this(context, null);
    }


    public CanvasView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mSignaturePaint = new Paint();
        mBackgroundPaint = new Paint();

        mSignaturePaint.setColor(Color.BLACK);
        mSignaturePaint.setStrokeWidth(10);
        mBackgroundPaint.setColor(Color.WHITE);

        mTouchCounter = 0;
    }


    public Bitmap getBitmap(){
        return mSignature;
    }

    public int getTouchCounter(){
        return mTouchCounter;
    }

    public ArrayList<Long> getTimePeriodOnTouch(){
        return TimePeriodOnTouch;
    }

    public ArrayList<Long> getTimesOfGettingPoints(){
        return timesOfGettingPoints;
    }

    public List<PointF> getSignatureControlPoints() {
        return mSignatureControlPoints;
    }

    public List<PointF> getSignatureActionUpPoints() {
        return mSignatureActionUpPoints;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        PointF curPoint = new PointF(event.getX(), event.getY());
        String action = "";

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            {
                timeStart = System.currentTimeMillis();

                action = "ACTION_DOWN";

                mTouchCounter += 1;

                mCurrentPoint = curPoint;

                mSignatureControlPoints.add(mCurrentPoint);
                timesOfGettingPoints.add(System.currentTimeMillis());

                if (mCurrentPoint != null)
                {
                    invalidate();
                }

                break;
            }

            case MotionEvent.ACTION_UP:
            {
                periodOnTouch = System.currentTimeMillis() - timeStart;
                timeStart = 0;
                TimePeriodOnTouch.add(periodOnTouch);

                action = "ACTION_UP";
                mSignatureActionUpPoints.add(curPoint);
                mCurrentPoint = null;
                Log.i("ON_TOUCH: ", String.valueOf(periodOnTouch));
                break;
            }

            case MotionEvent.ACTION_MOVE:
            {
                mCurrentPoint = curPoint;

                mSignatureControlPoints.add(mCurrentPoint);
                timesOfGettingPoints.add(System.currentTimeMillis());

                if (mCurrentPoint != null)
                {
                    invalidate();
                }
                break;
            }

            case MotionEvent.ACTION_CANCEL:
            {
                periodOnTouch = System.currentTimeMillis() - timeStart;
                timeStart = 0;
                TimePeriodOnTouch.add(periodOnTouch);
                action = "ACTION_CANCEL";
                mCurrentPoint = null;
                break;
            }
        }



        Log.i("CANVAS_VIEW: ", action + " at x=" + curPoint.x + ", y=" + curPoint.y);
        Log.i("ON_TOUCH_MASSIV: ", TimePeriodOnTouch.toString());
        return true;
    }






    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        mSignature = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mSignature);
        mCanvas.drawBitmap(mSignature, 0, 0, mSignaturePaint);
    }



    @Override
    protected void onDraw(Canvas canvas)
    {
        if (centeredSignature == null)
        {
            mCanvas.drawPaint(mBackgroundPaint);
            canvas.drawPaint(mBackgroundPaint);
            PointF prev = null;

            for (PointF p : mSignatureControlPoints)
            {
                mCanvas.drawCircle(p.x, p.y, 5, mSignaturePaint);
                canvas.drawCircle(p.x, p.y, 5, mSignaturePaint);
                if (prev != null) {
                    mCanvas.drawLine(prev.x, prev.y, p.x, p.y, mSignaturePaint);
                    canvas.drawLine(prev.x, prev.y, p.x, p.y, mSignaturePaint);
                }
                prev = p;
                if (mSignatureActionUpPoints.contains(prev))
                    prev = null;
            }
        }
        else
        {
            mSignature = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mSignature);

            canvas.drawPaint(mBackgroundPaint);
            mCanvas.drawPaint(mBackgroundPaint);

            canvas.drawBitmap(centeredSignature, this.getWidth()/2 - centeredSignature.getWidth()/2 ,this.getHeight()/2 - centeredSignature.getHeight()/2, mSignaturePaint);
            mCanvas.drawBitmap(centeredSignature, this.getWidth()/2 - centeredSignature.getWidth()/2 ,this.getHeight()/2 - centeredSignature.getHeight()/2, mSignaturePaint);
        }
    }



    public void CenterSignature()
    {
        CenterImage object = new CenterImage(mSignature);
        Bitmap cutSignature = object.center();
        double k = (double)cutSignature.getHeight()/cutSignature.getWidth();

        int width = this.getWidth();
        long height = (int)Math.round(k*width);


        if (height > this.getHeight())
        {
            height = this.getHeight();
            width = (int)Math.round(height/k);

            centeredSignature = Bitmap.createScaledBitmap(cutSignature, width, (int)height, false);
            invalidate();
        }
        else
        {
            centeredSignature = Bitmap.createScaledBitmap(cutSignature, width, (int)height, false);
            invalidate();
        }



    }

}
