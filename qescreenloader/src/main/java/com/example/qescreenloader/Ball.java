package com.example.qescreenloader;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.qegame.qeutil.androids.QeAndroid;

public class Ball extends View implements Element {
    private static final String TAG = "Ball-TAG";

    private int defaultSize;

    @ColorInt
    private int color;

    private Paint paint;

    public Ball(Context context) {
        super(context);
        init(context, null);
    }
    public Ball(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    public Ball(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }
    public Ball(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        this.paint = new Paint();
        this.paint.setStrokeWidth(1);
        this.color = QeAndroid.getThemeColor(context, QeAndroid.ThemeColor.ACCENT);
        this.paint.setColor(color);
        this.defaultSize = (int) context.getResources().getDimension(R.dimen.ball_size_default);
        setElevation(20);
    }

    @Override
    public void setColor(int color) {
        this.color = color;
        this.paint.setColor(color);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = resolveSize(defaultSize, widthMeasureSpec);
        int height = resolveSize(defaultSize, heightMeasureSpec);

        setMeasuredDimension(width, height);
    }
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, Math.min(getWidth(), getHeight()) / 2.0f, paint);
    }
    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        if (params instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) params;
            int dp = (int) QeAndroid.dp(getContext(), 8);
            lp.setMargins(dp, dp, dp, dp);
        }
        super.setLayoutParams(params);
    }

}
