package com.shichuang.mobileworkingticket.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.shichuang.mobileworkingticket.R;

/**
 * Created by Administrator on 2018/3/16.
 */

public class HorizontalTextProgressBar extends ProgressBar {
    private Paint mPaintText;
    private int mWidth, mHeight;
    private float mTextWidth;
    private String mTextProcess = "0%";

    public HorizontalTextProgressBar(Context context) {
        super(context);
        init();
    }

    public HorizontalTextProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HorizontalTextProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaintText = new Paint();
        mPaintText.setTextAlign(Paint.Align.CENTER);
        mPaintText.setAntiAlias(true);
        mTextWidth = mPaintText.measureText(mTextProcess + "%");
        mPaintText.setTextSize(mTextWidth + 16);  // 设置字体的大小为整个View的大小一致
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 判断当前进度有没有超过 文字，设置不同
        int progressWidth = getProgress() * mWidth / 100;
        if (progressWidth > (mWidth / 2 - mTextWidth / 2)) {
            mPaintText.setColor(getResources().getColor(R.color.white));
        } else {
            mPaintText.setColor(getResources().getColor(R.color.colorPrimary));
        }
        Rect targetRect = new Rect(0, 0, mWidth, mHeight);
        Paint.FontMetricsInt fontMetrics = mPaintText.getFontMetricsInt();
        int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        canvas.drawText(mTextProcess, targetRect.centerX(), baseline, mPaintText);
    }

    public void setTextProcess(int process) {
        this.mTextProcess = process + "%";
        invalidate();
    }

    /**
     * convert px to its equivalent sp
     * <p>
     * 将px转换为sp
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * convert sp to its equivalent px
     * <p>
     * 将sp转换为px
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
