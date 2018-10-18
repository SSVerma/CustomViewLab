package com.ssverma.customviewlab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

public class StatusView extends View {

    private static final int SHAPE_CIRCLE = 0;
    private static final int SHAPE_SQUARE = 1;
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
    private int shape;
    private float DEFAULT_OUTLINE_WIDTH = 4;

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
        desiredHeight = (int) ((shapeSize + spacing/*Bottom spacing*/) * rowsRequired - spacing/*last row bottom spacing*/);
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
            if (shape == SHAPE_CIRCLE) {
                int cx = statusRects[index].centerX();
                int cy = statusRects[index].centerY();

                canvas.drawCircle(cx, cy, radius, paintOutline);
                if (statusDone[index]) {
                    canvas.drawCircle(cx, cy, radius, paintFill);
                }

            } else if (shape == SHAPE_SQUARE) {
                drawSquare(canvas, index);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int statusIconIndex = findItemAtTouchPoint(event.getX(), event.getY());
                toggleStatusIcon(statusIconIndex);
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void drawSquare(Canvas canvas, int statusIconIndex) {
        Rect rect = statusRects[statusIconIndex];

        if (statusDone[statusIconIndex]) {
            canvas.drawRect(rect, paintFill);
        }

        canvas.drawRect(rect.left + outlineWidth / 2,
                rect.top + outlineWidth / 2,
                rect.right - outlineWidth / 2,
                rect.bottom - outlineWidth / 2,
                paintOutline);
    }

    private void toggleStatusIcon(int statusIconIndex) {
        if (statusIconIndex == -1) {
            return;
        }
        statusDone[statusIconIndex] = !statusDone[statusIconIndex];
        invalidate();//Redraw -> onDraw() is called
    }

    private int findItemAtTouchPoint(float x, float y) {
        for (int index = 0; index < statusRects.length; index++) {
            if (statusRects[index].contains((int) x, (int) y)) {
                return index;
            }
        }
        return -1;//Invalid index
    }

    private void init(AttributeSet attrs) {
        if (isInEditMode()) {
            /*Preview mode of layout, just for ui verification*/
            setUpPreviewModeValues();
        }

        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        float displayDensity = displayMetrics.density;
        float defaultOutlineWidthPx = displayDensity * DEFAULT_OUTLINE_WIDTH;

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.StatusView);
        outlineColor = typedArray.getColor(R.styleable.StatusView_outlineColor, Color.GRAY);
        fillColor = typedArray.getColor(R.styleable.StatusView_fillColor, Color.RED);
        outlineWidth = typedArray.getDimension(R.styleable.StatusView_outlineWidth, defaultOutlineWidthPx);
        shape = typedArray.getInt(R.styleable.StatusView_shape, SHAPE_CIRCLE/*Circle default value*/);

        typedArray.recycle();

        shapeSize = 144f;
        spacing = 32f;
        float diameter = shapeSize - outlineWidth;
        radius = diameter / 2;

        /*Outline circle*/
        paintOutline = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintOutline.setStyle(Paint.Style.STROKE);
        paintOutline.setStrokeWidth(outlineWidth);
        paintOutline.setColor(outlineColor);

        /*Filled circle*/
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
