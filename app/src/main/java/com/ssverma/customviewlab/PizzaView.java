package com.ssverma.customviewlab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

public class PizzaView extends View {

    private Paint paint;
    private int totalWedges = 5;

    public PizzaView(Context context) {
        super(context);
        init(context, null);
    }

    public PizzaView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PizzaView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PizzaView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int width = getWidth() - getPaddingLeft() - getPaddingRight();
        final int height = getHeight() - getPaddingTop() - getPaddingBottom();
        final int cx = width / 2;
        final int cy = height / 2;
        final float diameter = Math.min(width, height) - paint.getStrokeWidth();
        final float radius = diameter / 2;

        canvas.drawCircle(cx, cy, radius, paint);
        drawPizzaCuts(canvas, cx, cy, radius);
    }

    private void drawPizzaCuts(Canvas canvas, int cx, int cy, float radius) {
        final float degrees = 360f / totalWedges;
        canvas.save();
        for (int i = 0; i < totalWedges; i++) {
            canvas.rotate(degrees, cx, cy);
            canvas.drawLine(cx, cy, cx, cy - radius, paint);
        }
        canvas.restore();
    }

    private void init(Context context, AttributeSet attrs) {
        int strokeWidth = 4;
        int color = Color.RED;
        if (attrs != null) {
            TypedArray array =  context.obtainStyledAttributes(attrs, R.styleable.PizzaView);
            strokeWidth = array.getDimensionPixelSize(R.styleable.PizzaView_pv_stroke_width, strokeWidth);
            color = array.getColor(R.styleable.PizzaView_pv_color, color);
            totalWedges = array.getInt(R.styleable.PizzaView_pv_total_wedges, totalWedges);
            array.recycle();
        }
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(color);
    }

}
