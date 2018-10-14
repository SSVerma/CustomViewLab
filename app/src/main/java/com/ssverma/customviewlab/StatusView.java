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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = 0;
        int desiredHeight = 0;

        int specWidth = MeasureSpec.getSize(widthMeasureSpec);//Overall width available, width constraints
        int useAbleWidth = specWidth - getPaddingLeft() - getPaddingRight();
        float perItemWidth = shapeSize + spacing;
        int totalStatsIconCanFit = (int) (useAbleWidth / perItemWidth);
        int maxHorizontalStatusIcon = Math.min(totalStatsIconCanFit, statusDone.length);

        desiredWidth = (int) (perItemWidth * maxHorizontalStatusIcon - spacing/*Initial rectangle spacing*/);
        desiredWidth = desiredWidth + getPaddingLeft() + getPaddingRight();

        int rowsRequired = statusDone.length / maxHorizontalStatusIcon + 1;
        desiredHeight = (int)((shapeSize + spacing/*Bottom spacing*/) * rowsRequired - spacing/*last row bottom spacing*/);
        desiredHeight = desiredHeight + getPaddingTop() + getPaddingBottom();

        int width = resolveSizeAndState(desiredWidth, widthMeasureSpec, 0);
        int height = resolveSizeAndState(desiredHeight, heightMeasureSpec, 0);

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        setUpStatusRectangle(w);
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
        int totalStatus = 9;
        boolean[] previewModeStatusDone = new boolean[totalStatus];
        /*Lets make half of the status done to true*/
        for (int i = 0; i < totalStatus / 2; i++) {
            previewModeStatusDone[i] = true;
        }
        setStatusDone(previewModeStatusDone);
    }

    private void setUpStatusRectangle(int newWidth) {
        int useAbleWidth = newWidth - getPaddingLeft() - getPaddingRight();
        int statusIconCanFit = (int) (useAbleWidth / (shapeSize + spacing));
        int maxHorizontalIcon = Math.min(statusIconCanFit, statusDone.length);

        statusRects = new Rect[statusDone.length];

        for (int index = 0; index < statusRects.length; index++) {
            int rowIndex = index / maxHorizontalIcon;
            int colIndex = index % maxHorizontalIcon;

            int left = (int) (colIndex * (shapeSize + spacing)) + getPaddingLeft();
            int top = (int) (rowIndex * (shapeSize + spacing)) + getPaddingTop();
            int right = (int) (left + shapeSize);
            int bottom = (int) (top + shapeSize);
            statusRects[index] = new Rect(left, top, right, bottom);
        }
    }

    public boolean[] getStatusDone() {
        return statusDone;
    }

    public void setStatusDone(boolean[] statusDone) {
        this.statusDone = statusDone;
    }
}
