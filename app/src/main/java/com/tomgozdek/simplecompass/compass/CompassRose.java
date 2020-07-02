package com.tomgozdek.simplecompass.compass;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.util.AttributeSet;
import android.view.View;

import com.tomgozdek.simplecompass.R;

public class CompassRose extends View {
    private Paint labelPaint;
    private Paint scalePaint;
    private int width;
    private int height;
    private String[] DIRECTIONS = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
    private float directionLabelSize = 80f;
    private VectorDrawableCompat compassNeedle;
    private float rotationAngle = 45;

    public CompassRose(Context context) {
        this(context, null);
    }

    public CompassRose(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        parseAttributes(attrs);
        createPaints();
        createCompassNeedle();
    }

    private void createCompassNeedle() {
        compassNeedle = VectorDrawableCompat.create(getResources(), R.drawable.compass_needle, null);
        if(compassNeedle != null) {
            compassNeedle.setBounds(0, 0, compassNeedle.getIntrinsicWidth(), compassNeedle.getIntrinsicHeight());
        }
    }

    private void parseAttributes(AttributeSet attrs) {
        if(attrs != null){
            TypedArray compassRoseAttributes = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.CompassRose, 0, 0);
            try {
                directionLabelSize = compassRoseAttributes.getDimension(R.styleable.CompassRose_directionLabelsSize, 0);
            } finally {
                compassRoseAttributes.recycle();
            }
        }
    }

    private void createPaints() {
        labelPaint = new Paint();
        labelPaint.setAntiAlias(true);
        labelPaint.setStyle(Style.FILL);
        labelPaint.setColor(Color.BLACK);
        labelPaint.setTypeface(Typeface.SANS_SERIF);
        labelPaint.setTextSize(directionLabelSize);

        scalePaint = new Paint();
        scalePaint.setAntiAlias(true);
        scalePaint.setColor(Color.LTGRAY);
        scalePaint.setStrokeWidth(3);
        scalePaint.setStyle(Style.STROKE);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawNeedle(canvas);

        float centerX = width/2f;
        float centerY = height/2f;
        canvas.rotate(rotationAngle, centerX, centerY);

        drawCircles(canvas);
        drawDirections(canvas);
    }

    private void drawCircles(Canvas canvas) {
        float centerX = width/2f;
        float centerY = height/2f;

        double radius = (width - directionLabelSize * 3)/2;
        double innerRadius = radius - directionLabelSize /2;
        double innerInnerRadius = innerRadius - directionLabelSize /2;
        double currentDegreeRadius;

        for(double angle = 0d; angle <= 360; angle+=5){

            float startX = (centerX + getCartesianX(radius, angle));
            float startY = (centerY + getCartesianY(radius, angle));
            currentDegreeRadius = angle % 10 == 0 ? innerRadius : innerInnerRadius;
            float endX = (centerX + getCartesianX(currentDegreeRadius, angle));
            float endY = (centerY + getCartesianY(currentDegreeRadius, angle));

            canvas.drawLine(startX, startY, endX, endY, scalePaint);
        }
    }

    private void drawNeedle(Canvas canvas) {
        float centerX = width/2f - compassNeedle.getIntrinsicWidth()/4f;
        float centerY = height/2f - compassNeedle.getIntrinsicHeight()/2f;
        canvas.translate(centerX, centerY);
        compassNeedle.draw(canvas);
        canvas.translate(-centerX, -centerY);
    }


    private void drawDirections(Canvas canvas) {
        float centerX = width/2f;
        float centerY = height/2f;

        double radius = (width- directionLabelSize)/2;

        double angle = 0d;
        int angleChangeStep = 360 / DIRECTIONS.length;
        for(String direction : DIRECTIONS){

            float labelPosX = (centerX + (getCartesianX(radius, angle) - labelPaint.measureText(direction)/2));
            float labelPosY = (centerY - getCartesianY(radius, angle)) + (labelPaint.getTextSize()-labelPaint.descent())/2;

            canvas.drawText(direction, labelPosX, labelPosY, labelPaint);
            angle+=angleChangeStep;
        }
    }

    private float getCartesianY(double radius, double angle){
        return (int) (radius * Math.cos(Math.toRadians(angle)));
    }

    private float getCartesianX(double radius, double angle){
        return (int) (radius * Math.sin(Math.toRadians(angle)));
    }

    public void setAngle(int angle){
        rotationAngle = (angle * -1);
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
        height = w;
        h = w;
        oldh = oldw;
        super.onSizeChanged(w, h, oldw, oldh);
    }
}

