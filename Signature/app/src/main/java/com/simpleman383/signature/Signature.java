package com.simpleman383.signature;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 29.11.2016.
 */

public class Signature
{
    private static final int Compression = 30;

    private List<PointF> mSignatureControlPoints = null;
    private List<PointF> mSignatureActionUpPoints = null;
    private ArrayList<Long> timesOfGettingPoints = null;

    private Bitmap signatureBitmap;
    private int touches;
    private List <Long> timeOnTouch;

    private long maxTimeOnTouch;
    private long minTimeOnTouch;
    private long averageTimeOnTouch;
    private long totalTimeOnTouch;

    private double maxSpeed;
    private double minSpeedNotNull;

    private double maxVelocityProjectionX;
    private double minVelocityProjectionX;
    private double maxVelocityProjectionY;
    private double minVelocityProjectionY;


    public Signature(Bitmap signatureBitmap, int touches, List<Long> timeOnTouch)
    {
        this.signatureBitmap = Bitmap.createScaledBitmap(signatureBitmap, signatureBitmap.getWidth()/Compression ,signatureBitmap.getHeight()/Compression, false);
        this.touches = touches;
        this.timeOnTouch = timeOnTouch;
        this.setTimeOnTouchValues();
    }


    public Signature(Bitmap signatureBitmap, int touches, List<Long> timeOnTouch,  List<PointF> mSignatureControlPoints, List<PointF> mSignatureActionUpPoints, ArrayList<Long> timesOfGettingPoints)
    {
        this.signatureBitmap = Bitmap.createScaledBitmap(signatureBitmap, signatureBitmap.getWidth()/Compression ,signatureBitmap.getHeight()/Compression, false);
        this.touches = touches;
        this.timeOnTouch = timeOnTouch;
        this.mSignatureActionUpPoints = mSignatureActionUpPoints;
        this.mSignatureControlPoints = mSignatureControlPoints;
        this.timesOfGettingPoints = timesOfGettingPoints;
        this.setTimeOnTouchValues();
        this.setSpeedCharValues();
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

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public double getMinSpeedNotNull() {
        return minSpeedNotNull;
    }

    public double getMaxVelocityProjectionX() {
        return maxVelocityProjectionX;
    }

    public double getMinVelocityProjectionX() {
        return minVelocityProjectionX;
    }

    public double getMaxVelocityProjectionY() {
        return maxVelocityProjectionY;
    }

    public double getMinVelocityProjectionY() {
        return minVelocityProjectionY;
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

    private void setSpeedCharValues()
    {
        PointF prev_point = null;

        ArrayList<Double> speedModule = new ArrayList<>();
        ArrayList<Double> velocityProjectionsX = new ArrayList<>();
        ArrayList<Double> velocityProjectionsY = new ArrayList<>();

        for (PointF point : mSignatureControlPoints)
        {
            if (prev_point != null)
            {
                if (!mSignatureActionUpPoints.contains(prev_point))
                {
                    double path_length = Math.sqrt(Math.abs((point.x - prev_point.x) * (point.x - prev_point.x)) + Math.abs((point.y - prev_point.y) * (point.y - prev_point.y)));
                    long time_interval = timesOfGettingPoints.get(mSignatureControlPoints.indexOf(point)) - timesOfGettingPoints.get(mSignatureControlPoints.indexOf(prev_point));

                    double path_projectionX = point.x - prev_point.x;
                    double path_projectionY = point.y - prev_point.y;

                    double speed = path_length / time_interval;
                    double velocityProjectionX = path_projectionX / time_interval;
                    double velocityProjectionY = path_projectionY / time_interval;

                    velocityProjectionsX.add(velocityProjectionX);
                    velocityProjectionsY.add(velocityProjectionY);
                    speedModule.add(speed);
                }
            }

            prev_point = point;
        }

        Log.i("Speed: ", speedModule.toString());



        maxSpeed = 0;           //initialization
        if (speedModule.isEmpty() || speedModule == null)
        {
            minSpeedNotNull = 0;
        }
        else
        {
            minSpeedNotNull = speedModule.get(0);
        }
        maxVelocityProjectionX = 0;
        if (velocityProjectionsX.isEmpty() || velocityProjectionsX == null)
        {
            minVelocityProjectionX = 0;
        }
        else
        {
            minVelocityProjectionX = velocityProjectionsX.get(0);
        }
        maxVelocityProjectionY = 0;
        if (velocityProjectionsY.isEmpty() || velocityProjectionsY == null)
        {
            minVelocityProjectionY = 0;
        }
        else
        {
            minSpeedNotNull = velocityProjectionsY.get(0);
        }




        for (double speed: speedModule)
        {
            if (speed > maxSpeed)
                maxSpeed = speed;

            if (speed < minSpeedNotNull)
                minSpeedNotNull = speed;
        }



        for (double velocity: velocityProjectionsX)
        {
            if (velocity > maxVelocityProjectionX)
                maxVelocityProjectionX = velocity;

            if (velocity < minVelocityProjectionX)
                maxVelocityProjectionX = velocity;
        }




        for (double velocity: velocityProjectionsY)
        {
            if (velocity > maxVelocityProjectionY)
                maxVelocityProjectionY = velocity;

            if (velocity < minVelocityProjectionY)
                maxVelocityProjectionY = velocity;
        }


        return;
    }

}

