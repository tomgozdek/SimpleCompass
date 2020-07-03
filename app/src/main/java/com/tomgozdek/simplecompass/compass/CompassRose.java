package com.tomgozdek.simplecompass.compass;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.util.AttributeSet;
import android.view.View;

import com.tomgozdek.simplecompass.R;

public class CompassRose extends View {
    private Paint labelPaint;
    private Paint scalePaint;
    private Paint markerPaint;
    private int width;
    private String[] DIRECTIONS = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
    private float directionLabelSize = 80f;
    private VectorDrawableCompat compassNeedle;
    private Bitmap bitmapMarker;
    private float rotationAngle = 45;
    private float destinationAngle = 45;
    private boolean showDestination = isInEditMode();

    float originX;
    float originY;

    public CompassRose(Context context) {
        this(context, null);
    }

    public CompassRose(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        parseAttributes(attrs);
        createPaints();
        createCompassNeedle();
        createDestinationMarker();
    }

    private void createDestinationMarker() {
        VectorDrawableCompat destinationMarker = VectorDrawableCompat.create(getResources(), R.drawable.destination_marker, null);
        if(destinationMarker != null){
            bitmapMarker = Bitmap.createBitmap(destinationMarker.getIntrinsicWidth(), destinationMarker.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmapMarker);
            destinationMarker.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            destinationMarker.draw(canvas);
            destinationMarker.setBounds(0,0,destinationMarker.getIntrinsicWidth(), destinationMarker.getIntrinsicHeight());
        }
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

        markerPaint = new Paint();
        markerPaint.setAntiAlias(true);
        markerPaint.setStyle(Style.FILL);
        markerPaint.setColor(Color.RED);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawNeedle(canvas);
        drawScale(canvas);
        if(showDestination){
            drawDestinationMarker(canvas);
        }
        drawDirections(canvas);
    }

    private void drawScale(Canvas canvas) {

        float radius = width/2f;

        canvas.save();
        int angleStep = 5;
        float startX = originX;
        float startY = originY + radius * 0.9f;
        float smallScaleEndY = originY + radius * 0.875f;
        float largeScaleEndY = originY + radius * 0.85f;

        for(double angle = 0d; angle <= 360; angle+=angleStep){
            if(angle % 10 == 0){
                canvas.drawLine(startX, startY, startX, largeScaleEndY, scalePaint);
            } else {
                canvas.drawLine(startX, startY, startX, smallScaleEndY, scalePaint);
            }
            canvas.rotate(angleStep, originX, originY);
        }
        canvas.restore();
    }

    private void drawNeedle(Canvas canvas) {
        float centerX = originX - compassNeedle.getIntrinsicWidth()/4f;
        float centerY = originY - compassNeedle.getIntrinsicHeight()/2f;
        canvas.translate(centerX, centerY);
        compassNeedle.draw(canvas);
        canvas.translate(-centerX, -centerY);
    }

    private void drawDestinationMarker(Canvas canvas) {
        float radius = originX * 0.8f;
        float posX = originX - bitmapMarker.getWidth()/4f;
        float posY = (originY - radius) + bitmapMarker.getHeight()/2f;
        canvas.save();
        canvas.rotate(destinationAngle + rotationAngle, originX, originY);
        canvas.drawBitmap(bitmapMarker, posX,posY, markerPaint);
        canvas.restore();
    }


    private void drawDirections(Canvas canvas) {

        float radius = (width - directionLabelSize)/2f;
        canvas.save();
        canvas.rotate(rotationAngle, originX, originY);

        int angleChangeStep = 360 / DIRECTIONS.length;
        float labelPosX = originX;
        float labelPosY = (originY - radius) + (labelPaint.getTextSize()-labelPaint.descent())/2;
        for(String direction : DIRECTIONS){
            canvas.drawText(direction, labelPosX - labelPaint.measureText(direction)/2, labelPosY, labelPaint);
            canvas.rotate(angleChangeStep, originX, originY);
        }
        canvas.restore();
    }

    @BindingAdapter({"setAngle"})
    public static void setAngle(CompassRose view, int angle){
        view.setRotationAngle(angle * -1);
        view.invalidate();
    }

    private void setRotationAngle(int angle) {
        rotationAngle = angle;
    }

    @BindingAdapter({"showDestinationBearing"})
    public static void showDestinationBearing(CompassRose view, boolean show){
        view.showDestinationMarker(show);
    }

    private void showDestinationMarker(boolean show) {
        showDestination = show;
    }

    public static void setDestinationMarkerAzimuth(CompassRose view, int bearing){
        view.setDestinationBearing(bearing);
        view.invalidate();
    }

    private void setDestinationBearing(int bearing) {
        destinationAngle = bearing;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
        originX = width/2f;
        originY = width/2f;
        h = w;
        oldh = oldw;
        super.onSizeChanged(w, h, oldw, oldh);
    }
}

