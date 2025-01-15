package com.metagards.metagames.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

public class CircularProgressBar extends View {
    private static final int MAX_PROGRESS = 720; // Two full rotations (360 degrees each)
    private static final int ROTATION_DURATION = 19000; // 15 seconds for each rotation

    private Paint bluePaint;
    private Paint redPaint;
    private RectF rectF;
    private int currentProgress;
    private boolean colorChanged = false;
    private Handler handler;

    public CircularProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        bluePaint = new Paint();
        bluePaint.setColor(getResources().getColor(android.R.color.holo_blue_light));
        bluePaint.setStyle(Paint.Style.STROKE);
        bluePaint.setStrokeWidth(10);
        bluePaint.setAntiAlias(true);

        redPaint = new Paint();
        redPaint.setColor(getResources().getColor(android.R.color.holo_red_light));
        redPaint.setStyle(Paint.Style.STROKE);
        redPaint.setStrokeWidth(10);
        redPaint.setAntiAlias(true);

        rectF = new RectF();

        currentProgress = 0;
        handler = new Handler();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(centerX, centerY) - 10;

        rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

        if (!colorChanged && currentProgress >= 360) {
            // After the first 180 degrees (15 seconds), switch to red color
            canvas.drawArc(rectF, -90, currentProgress - 360, false, redPaint);
        } else {
            // Use blue color for the first 15 seconds
            canvas.drawArc(rectF, -90, currentProgress, false, bluePaint);
        }
    }

    public void setProgress(int progress) {
        currentProgress = progress;
        invalidate();
    }

    public void startProgress() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                currentProgress += 1; // Increase progress by 1 degree
                if (currentProgress <= MAX_PROGRESS) {
                    setProgress(currentProgress);
                    handler.postDelayed(this, ROTATION_DURATION / MAX_PROGRESS);
                }
            }
        }, ROTATION_DURATION / MAX_PROGRESS);
    }
}



