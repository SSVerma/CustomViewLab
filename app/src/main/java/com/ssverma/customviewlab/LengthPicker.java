package com.ssverma.customviewlab;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

public class LengthPicker extends LinearLayout {

    private static final String KEY_SUPER_STATE = "key_super_state";
    private static final String KEY_TOTAL_INCHES = "key_total_inches";
    private int totalInches;
    private Button btnMinus;
    private Button btnPlus;
    private TextView tvLength;

    public LengthPicker(Context context) {
        super(context);
        init();
    }

    public LengthPicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LengthPicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LengthPicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_SUPER_STATE, super.onSaveInstanceState());
        bundle.putInt(KEY_TOTAL_INCHES, totalInches);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            totalInches = bundle.getInt(KEY_TOTAL_INCHES);
            super.onRestoreInstanceState(bundle.getParcelable(KEY_SUPER_STATE));
        } else {
            super.onRestoreInstanceState(state);
        }
        updateControls();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.length_picker, this);
        setOrientation(LinearLayout.HORIZONTAL);

        btnMinus = findViewById(R.id.btn_minus);
        btnPlus = findViewById(R.id.btn_plus);
        tvLength = findViewById(R.id.tv_length);

        btnMinus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                totalInches--;
                updateControls();
            }
        });

        btnPlus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                totalInches++;
                updateControls();
            }
        });

        updateControls();
    }

    private void updateControls() {
        int feet = totalInches / 12;
        int inches = totalInches % 12;

        String text = String.format(Locale.getDefault(), "%d' %d\"", feet, inches);
        if (feet == 0) {
            text = String.format(Locale.getDefault(), "%d\"", inches);
        } else if (inches == 0) {
            text = String.format(Locale.getDefault(), "%d'", feet);
        }

        tvLength.setText(text);
        btnMinus.setEnabled(totalInches > 0);
    }

}
