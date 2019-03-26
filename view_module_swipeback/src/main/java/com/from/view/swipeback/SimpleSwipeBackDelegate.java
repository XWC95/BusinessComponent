package com.from.view.swipeback;

import android.app.Activity;

/**
 * @author Vea
 * @version 1.0.2
 * @since 2019-03
 */
public class SimpleSwipeBackDelegate implements SwipeBackHelper.Delegate {
    /**
     * 正在滑动返回
     *
     * @param slideOffset 从 0 到 1
     */
    @Override
    public void onSwipeBackLayoutSlide(float slideOffset) {
    }

    /**
     * 没达到滑动返回的阈值，取消滑动返回动作，回到默认状态
     */
    @Override
    public void onSwipeBackLayoutCancel() {
    }

    /**
     * 滑动返回执行完毕，销毁当前 Activity
     *
     * @param activity 当前Activity
     */
    @Override
    public void onSwipeBackLayoutExecuted(Activity activity) {

    }
}
