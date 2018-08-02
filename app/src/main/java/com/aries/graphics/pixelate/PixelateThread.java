package com.aries.graphics.pixelate;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

/**
 * Created by JiangYiDong on 2018/6/19.
 */
class PixelateThread extends Thread {

    private Paint paint;
    private boolean isRendering = false;

    /* OnPixelateListener returning the pixelated bitmap */
    private OnPixelateListener onPixelateListener;
    private Bitmap bitmap;

    /* Bitmap properties */
    private int bmpWidth;
    private int bmpHeight;
    /* Columns based on density */
    private double cols;
    /* Properties for drawing a certain area on the canvas */
    private int touchedX = -1;
    private int touchedY = -1;
    private double touchedSize;
    private double touchedHeight;

    public PixelateThread() {
        this.paint = new Paint();
    }

    public void setPixelateListener(final OnPixelateListener listener) {
        this.onPixelateListener = listener;
    }

    public void setBitmap(@NonNull Bitmap bitmap) {
        this.bitmap = bitmap;
        // Get width and height of the bitmap
        this.bmpWidth = this.bitmap.getWidth();
        this.bmpHeight = this.bitmap.getHeight();
    }

    boolean isRendering() {
        return isRendering;
    }

    @Override
    public synchronized void start() {
        super.start();
    }
    public void setArea(int x, int y, int size, int height) {
        touchedX = x;
        touchedY = y;
        touchedSize = size;
        touchedHeight = height;
    }

    public void pixelate(int density) {
        this.cols = density > 1 ? density : 1;
    }

    @Override
    public void run() {
        if(!isRendering()) {
            isRendering = true;
            Canvas canvas = new Canvas(this.bitmap);
            draw(canvas);
        }
    }

    private void draw(Canvas canvas) {

        double startX = 0;
        double startY = 0;
        double blockSize;
        double rows;

        // Draw certain area
        if(touchedX > 0 && touchedY > 0) {
            blockSize = touchedSize / cols;
            rows = Math.ceil(touchedHeight / blockSize);
            // Find the column and row by coordinates of an equal sized rectangle
            double startCol = Math.floor(touchedX/blockSize);
            double startRow = Math.floor(touchedY/blockSize);

            // Adjust the coordinates to the grid
            startY = (int)startRow * blockSize - (touchedSize / 2);
            startX = (int)startCol * blockSize - (touchedSize / 2);
        } else {
            blockSize = bmpWidth / cols;
            touchedHeight = blockSize;
            rows = Math.ceil((double) bmpHeight / blockSize);
        }

        for (int row = 0; row < rows; row++ ) {

            for (int col = 0; col < cols; col++ ) {

                double pixelCoordY = blockSize * row + startY;
                double pixelCoordX = blockSize * col + startX;

                double midY = pixelCoordY + (blockSize / 2);
                double midX = pixelCoordX + (blockSize / 2);

                if(midX >= bmpWidth || midX < 0) continue;
                if(midY >= bmpHeight || midY < 0) continue;

                paint.setColor(bitmap.getPixel((int)midX, (int)midY));

                canvas.drawRect((float)pixelCoordX, (float)pixelCoordY,
                        (float)(pixelCoordX + blockSize), (float)(pixelCoordY + blockSize), paint);
            }
        }
        isRendering = false;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override public void run() {
                onPixelateListener.onPixelated(bitmap, (int)cols);
            }
        });
    }
}
