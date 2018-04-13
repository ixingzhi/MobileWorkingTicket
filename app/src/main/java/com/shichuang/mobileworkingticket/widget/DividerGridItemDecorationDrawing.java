package com.shichuang.mobileworkingticket.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.State;

import com.shichuang.mobileworkingticket.MwtApplication;


public class DividerGridItemDecorationDrawing extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private Drawable mDivider;

    public DividerGridItemDecorationDrawing(Context context) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, State state) {
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition,
                               RecyclerView parent) {
        int margin = dp2px(MwtApplication.getInstance(), 12);
        // 如果是中间部分，只绘制间距的一半
        if (itemPosition % 2 == 0) {  // 左侧
            outRect.set(margin, margin, margin / 2, 0);
        } else if (itemPosition % 2 == 1) {  // 右侧
            outRect.set(margin / 2, margin, margin, 0);
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dp2px(Context context, float dpValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5f);
    }
}
