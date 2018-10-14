package com.ssverma.customviewlab;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

public class StatusView extends View {

    private boolean[] statusDone;
    private float outlineWidth;
    private float shapeSize;
    private float spacing;
    private Rect[] statusRects;
    private int outlineColor;
    private Paint paintOutline;
    private int fillColor;
    private Paint paintFill;
    private float radius;

    public StatusView(Context context) {
        super(context);
        init(null);
    }

    public StatusView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public StatusView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public StatusView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int index = 0; index < statusRects.length; index++) {
            int cx = statusRects[index].centerX();
            int cy = statusRects[index].centerY();

            canvas.drawCircle(cx, cy, radius, paintOutline);
            if (statusDone[index]) {
                canvas.drawCircle(cx, cy, radius, paintFill);
            }
        }

    }

    private void init(AttributeSet attrs) {

        if (isInEditMode()) {
            /*Preview mode of layout, just for ui verification*/
            setUpPreviewModeValues();
        }

        outlineWidth = 4f;
        shapeSize = 144f;
        spacing = 32f;
        float diameter = shapeSize - outlineWidth;
        radius = diameter / 2;
        setUpStatusRectangle();

        /*Outline circle*/
        outlineColor = Color.GRAY;
        paintOutline = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintOutline.setStyle(Paint.Style.STROKE);
        paintOutline.setStrokeWidth(outlineWidth);
        paintOutline.setColor(outlineColor);

        /*Filled circle*/
        fillColor = Color.RED;
        paintFill = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintFill.setStyle(Paint.Style.FILL);
        paintFill.setColor(fillColor);

    }

    private void setUpPreviewModeValues() {
        int totalStatus = 5;
        boolean[] previewModeStatusDone = new boolean[totalStatus];
        /*Lets make half of the status done to true*/
        for (int i = 0; i < totalStatus / 2; i++) {
            previewModeStatusDone[i] = true;
        }
        setStatusDone(previewModeStatusDone);
    }

    private void setUpStatusRectangle() {
        statusRects = new Rect[statusDone.length];

        for (int index = 0; index < statusRects.length; index++) {
            int left = (int) (index * (shapeSize + spacing));
            int top = 0;
            statusRects[index] = new Rect(left, top, (int) (left + shapeSize), (int) (top + shapeSize));
        }

    }

    public boolean[] getStatusDone() {
        return statusDone;
    }

    public void setStatusDone(boolean[] statusDone) {
        this.statusDone = statusDone;
    }
}
