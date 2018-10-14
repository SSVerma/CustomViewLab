package com.ssverma.customviewlab;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class PhotoSpiralView extends ViewGroup {
    public PhotoSpiralView(Context context) {
        super(context);
    }

    public PhotoSpiralView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PhotoSpiralView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PhotoSpiralView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getChildCount() == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        measureChildren(widthMeasureSpec, heightMeasureSpec);
        View first = getChildAt(0);
        int size = first.getMeasuredWidth() + first.getMeasuredHeight();
        int width = ViewGroup.resolveSize(size, widthMeasureSpec);
        int height = ViewGroup.resolveSize(size, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (getChildCount() == 0) {
            return;
        }
        View firstView = getChildAt(0);
        int childWidth = firstView.getMeasuredWidth();
        int childHeight = firstView.getMeasuredHeight();

        for (int i=0;i<getChildCount();i++) {
            View child = getChildAt(i);
            int l = 0;
            int t = 0;
            switch (i) {
                case 0:
                    l = 0;
                    t = 0;
                    break;

                case 1:
                    l = childWidth;
                    t = 0;
                    break;

                case 2:
                    l = childHeight;
                    t = childWidth;
                    break;

                case 3:
                    l = 0;
                    t = childHeight;
                    break;
            }
            child.layout(l, t, l + child.getMeasuredWidth(), t + child.getMeasuredHeight());
        }

    }
}
