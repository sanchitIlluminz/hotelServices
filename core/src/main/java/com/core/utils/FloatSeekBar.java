package com.core.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.core.R;


public class FloatSeekBar extends androidx.appcompat.widget.AppCompatSeekBar {
    private float max = 1.0f;
    private float min = 0.0f;
    private boolean isIndicator = false;

    public FloatSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyAttrs(attrs);
    }

    public FloatSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyAttrs(attrs);
    }

    public FloatSeekBar(Context context) {
        super(context);
    }

    public float getValue() {
        return (max - min) * ((float) getProgress() / (float) getMax()) + min;
    }

    public void setValue(float value) {
        setProgress((int) ((value - min) / (max - min) * getMax()));
    }

    private void applyAttrs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FloatSeekBar);
        isIndicator = a.getBoolean(R.styleable.FloatSeekBar_isIndicator, false);
        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.FloatSeekBar_floatMax) {
                this.max = a.getFloat(attr, 1.0f);
            } else if (attr == R.styleable.FloatSeekBar_floatMin) {
                this.min = a.getFloat(attr, 0.0f);
            }
        }
        a.recycle();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //consume
        return !isIndicator;
    }
}