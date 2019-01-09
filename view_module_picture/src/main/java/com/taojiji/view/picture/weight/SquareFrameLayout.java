package com.taojiji.view.picture.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Doc  宽高一样的FrameLayout
 *
 * @author ym.li
 * @version 2.9.0
 * @since 2018/11/2/002
 */
public class SquareFrameLayout extends FrameLayout {
    public SquareFrameLayout(Context context) {
        this(context, null);
    }

    public SquareFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquareFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
                getDefaultSize(0, heightMeasureSpec));
        int childWidthSize = getMeasuredWidth();
        // 高度和宽度一样
        heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                childWidthSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
