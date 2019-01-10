package com.from.view.picture.weight;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 在使用photoView缩放时避免异常的出现
 * <p>
 * author: ym.li
 * since: 2018/11/3
 */

public class FixViewPager extends ViewPager {
    public FixViewPager(@NonNull Context context) {
        this(context, null);
    }

    public FixViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            //uncomment if you really want to see these errors
            //e.printStackTrace();
            return false;
        }
    }
}
