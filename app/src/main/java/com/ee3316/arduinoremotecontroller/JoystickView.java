package com.ee3316.arduinoremotecontroller;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class JoystickView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener
{
    private float centerX;
    private float centerY;
    private float baseRadius;
    private float hatRadius;
    private float posX;
    private float posY;

    private void setupDimension() {
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        baseRadius = Math.min(getWidth(), getHeight()) / 4;
        hatRadius = Math.min(getWidth(), getHeight()) / 6;
        posX = centerX / 2;
        posY = centerY;
    }

    private void drawJoystick(float newX, float newY) {
        if (getHolder().getSurface().isValid()) {
            Canvas myCanvas = this.getHolder().lockCanvas();
            Paint color = new Paint();
            myCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            color.setARGB(128,50,50,50);
            myCanvas.drawCircle(posX, posY, baseRadius, color);
            color.setARGB(255,0,0,0);
            myCanvas.drawLine(posX - baseRadius, posY, posX + baseRadius, posY, color);
            myCanvas.drawLine(posX,posY - baseRadius, posX, posY + baseRadius, color);
            color.setARGB(192,255,255,224);
            myCanvas.drawCircle(newX, newY, hatRadius, color);
            color.setARGB(192,30,144,255);
            myCanvas.drawCircle(newX, newY, hatRadius / 25, color);
            getHolder().unlockCanvasAndPost(myCanvas);
        }
    }

    public JoystickView(Context context) {
        super(context);
        getHolder().addCallback(this);
        setOnTouchListener(this);
    }

    public JoystickView(Context context, AttributeSet a, int style) {
        super(context, a, style);
        getHolder().addCallback(this);
        setOnTouchListener(this);
    }

    public JoystickView(Context context, AttributeSet a) {
        super(context, a);
        getHolder().addCallback(this);
        this.setBackgroundColor(Color.TRANSPARENT);
        this.setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSPARENT);
        setOnTouchListener(this);
    }

    public boolean onTouch(View v, MotionEvent e) {
        if(v.equals(this)) {
            float getX = e.getX();
            float getY = e.getY();
            float distanceFromCenter = (float) Math.sqrt(Math.pow(getX - posX, 2) + Math.pow(getY - posY, 2));
            if (e.getAction() != e.ACTION_UP)
            {
                if(distanceFromCenter > baseRadius)
                {
                    getX = posX + (getX - posY) * baseRadius / distanceFromCenter;
                    getY = posY + (getY - posY) * baseRadius / distanceFromCenter;
                }
                drawJoystick(getX, getY);
            }
            else
            {
                drawJoystick(posX, posY);
            }
        }
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setupDimension();
        drawJoystick(posX, posY);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
