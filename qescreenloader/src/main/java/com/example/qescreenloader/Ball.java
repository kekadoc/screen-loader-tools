package com.example.qescreenloader;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.qegame.qeutil.androids.QeAndroid;

public class Ball extends View {
    private static final String TAG = "Ball-TAG";

    private int defaultSize;

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
        this.paint.setColor(QeAndroid.getThemeColor(context, QeAndroid.ThemeColor.ACCENT));
        this.defaultSize = (int) context.getResources().getDimension(R.dimen.ball_size_default);
        setElevation(20);
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

        canvas.drawCircle(getWidth() / 2, getHeight() / 2, Math.min(getWidth(), getHeight()) / 2, paint);
    }
}
