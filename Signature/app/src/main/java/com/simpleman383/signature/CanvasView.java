package com.simpleman383.signature;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 28.11.2016.
 */

public class CanvasView extends View {

    private Canvas mCanvas;
    private PointF mCurrentPoint; //for drawing
    private Paint mSignaturePaint;
    private Paint mBackgroundPaint;
    private List<PointF> mSignatureControlPoints = new ArrayList<>();
    private List<PointF> mSignatureActionUpPoints = new ArrayList<>();

    private Bitmap mSignature;


    private int mTouchCounter;

    private ArrayList<Long> TimePeriodOnTouch = new ArrayList<>(); //on touch time measure
    private long timeStart;
    private long periodOnTouch;


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
        mSignaturePaint.setStrokeWidth(8);
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




    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        PointF curPoint = new PointF(event.getX(), event.getY());
        String action = "";

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                timeStart = System.currentTimeMillis();
                action = "ACTION_DOWN";
                mTouchCounter += 1;
                mCurrentPoint = curPoint;
                mSignatureControlPoints.add(mCurrentPoint);
                if (mCurrentPoint != null) {invalidate();}
                break;

            case MotionEvent.ACTION_UP:
                periodOnTouch = System.currentTimeMillis() - timeStart;
                timeStart = 0;
                TimePeriodOnTouch.add(periodOnTouch);
                action = "ACTION_UP";
                mSignatureActionUpPoints.add(curPoint);
                mCurrentPoint = null;
                Log.i("ON_TOUCH: ", String.valueOf(periodOnTouch));
                break;

            case MotionEvent.ACTION_MOVE:
                mCurrentPoint = curPoint;
                mSignatureControlPoints.add(mCurrentPoint);

                if (mCurrentPoint != null)
                {
                    invalidate();
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                periodOnTouch = System.currentTimeMillis() - timeStart;
                timeStart = 0;
                TimePeriodOnTouch.add(periodOnTouch);
                action = "ACTION_CANCEL";
                mCurrentPoint = null;
                break;
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
    }




    @Override
    protected void onDraw(Canvas canvas)
    {


        mCanvas.drawPaint(mBackgroundPaint);
        PointF prev = null;

        for (PointF p : mSignatureControlPoints) {
            mCanvas.drawCircle(p.x, p.y, 4, mSignaturePaint);
            if (prev != null)
            {
                mCanvas.drawLine(prev.x, prev.y, p.x, p.y, mSignaturePaint);
            }
            prev = p;
            if (mSignatureActionUpPoints.contains(prev))
                prev = null;
        }
        canvas.drawBitmap(mSignature,0,0, mSignaturePaint);


    }

    private Bitmap ConvertToBitmap(Canvas canvas, Paint signaturePaint, int width, int height)
    {

        Bitmap signatureBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);

        canvas.drawBitmap(signatureBitmap, 0, 0 , signaturePaint);

        return signatureBitmap;

    }

}
