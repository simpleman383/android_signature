package com.simpleman383.signature;

/**
 * Created by Sergey on 01.12.2016.
 */
import android.graphics.*;
import android.util.Log;
import java.math.*;

public class CenterImage {
    private int xLeftBorder, xRightBorder, yTopBorder, yBottomBorder;
    private Bitmap source;

    public CenterImage(Bitmap source){
        this.source = source;
    }

    public Bitmap center(){
        detectXLeftBorder();
        detectXRightBorder();
        detectYTopBorder();
        detectYBottomBorder();
        if ((this.xLeftBorder == -1) || (this.xRightBorder == -1) || (this.yTopBorder == -1) || (this.yBottomBorder == -1)){
            Log.i("ERROR: ", "bad signature");
            return this.source; // в случае ошибки возврат неизмененного bitmap
        }
        else {
            int newWidth = Math.abs(xRightBorder - xLeftBorder);
            int newHeight = Math.abs(yBottomBorder - yTopBorder);
            return Bitmap.createBitmap(this.source, xLeftBorder, yTopBorder, newWidth, newHeight);
        }
    }

    public int getXLeftBorder(){
        detectXLeftBorder();
        return xLeftBorder;
    }

    public int getXRightBorder(){
        detectXRightBorder();
        return xRightBorder;
    }

    public int getYTopBorder(){
        detectYTopBorder();
        return yTopBorder;
    }

    public int getYBottomBorder(){
        detectYBottomBorder();
        return yBottomBorder;
    }

    private void detectXLeftBorder(){
        int minX = -1; // если черный пиксель не найдется - код ошибки
        int currentPixel;
        boolean foundBlackPixel = false;
        for (int x = 0; x < this.source.getWidth(); x++){
            if (foundBlackPixel) {
                break;
            }
            for (int y = 0; y < this.source.getHeight(); y++){
                currentPixel = this.source.getPixel(x, y);
                if (currentPixel == Color.BLACK){
                    foundBlackPixel = true;
                    minX = x;
                    break;
                }
            }
        }
        this.xLeftBorder = minX;
    }

    private void detectXRightBorder(){
        int minX = -1; // если черный пиксель не найдется - код ошибки
        int currentPixel;
        boolean foundBlackPixel = false;
        for (int x = this.source.getWidth() - 1; x >= 0; x--){
            if (foundBlackPixel) {
                break;
            }
            for (int y = this.source.getHeight() - 1; y >= 0; y--){
                currentPixel = this.source.getPixel(x, y);
                if (currentPixel == Color.BLACK){
                    foundBlackPixel = true;
                    minX = x;
                    break;
                }
            }
        }
        this.xRightBorder = minX;
    }

    private void detectYTopBorder(){
        int minY = -1; // если черный пиксель не найдется - код ошибки
        int currentPixel;
        boolean foundBlackPixel = false;
        for (int y = 0; y < this.source.getHeight(); y++){
            if (foundBlackPixel) {
                break;
            }
            for (int x = 0; x < this.source.getWidth(); x++){
                currentPixel = this.source.getPixel(x, y);
                if (currentPixel == Color.BLACK){
                    foundBlackPixel = true;
                    minY = y;
                    break;
                }
            }
        }
        this.yTopBorder = minY;
    }

    private void detectYBottomBorder(){
        int minY = -1; // если черный пиксель не найдется - код ошибки
        int currentPixel;
        boolean foundBlackPixel = false;
        for (int y = this.source.getHeight() - 1; y >= 0; y--){
            if (foundBlackPixel) {
                break;
            }
            for (int x = this.source.getWidth() - 1; x >= 0; x--){
                currentPixel = this.source.getPixel(x, y);
                if (currentPixel == Color.BLACK){
                    foundBlackPixel = true;
                    minY = y;
                    break;
                }
            }
        }
        this.yBottomBorder = minY;
    }
}
