package com.from.view.swipeback;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.view.View;


import java.util.List;

/**
 * 滑动返回帮助类
 *
 * @version 0.0.9
 * @since 2019-1
 */
public class SwipeBackHelper {
    private Activity mActivity;
    private Delegate mDelegate;
    private SwipeBackLayout mSwipeBackLayout;

    private SwipeBackHelper(){
    }

    /**
     * 必须在 Application 的 onCreate 方法中调用
     *
     * @param application          应用程序上下文
     * @param problemViewClassList 如果发现滑动返回后立即触摸界面时应用崩溃，
     *                             请把该界面里比较特殊的 View 的 class 添加到该集合中，
     *                             目前在库中已经添加了 WebView 和 SurfaceView
     * @param options             如果有些第三方库 Activity 不需要 swipeBack 可使用Option 配置
     */
    public static void init(Application application, List<Class<? extends View>> problemViewClassList,SwipeOptions options) {
        SwipeBackManager.getInstance().init(application, problemViewClassList,options);
    }
    public static void init(Application application) {
        SwipeBackManager.getInstance().init(application, null,null);
    }

    /**
     * @param activity
     */
    private SwipeBackHelper(Activity activity) {
        mActivity = activity;
        mDelegate = new SimpleSwipeBackDelegate(this);

        initSwipeBackFinish();
    }

    public static void bingOf(Activity activity) {
        new SwipeBackHelper(activity);
    }

    /**
     * 初始化滑动返回
     */
    private void initSwipeBackFinish() {
        if (mDelegate.isSupportSwipeBack()) {
            mSwipeBackLayout = new SwipeBackLayout(mActivity);
            mSwipeBackLayout.attachToActivity(mActivity);
            mSwipeBackLayout.setPanelSlideListener(new SwipeBackLayout.PanelSlideListener() {
                @Override
                public void onPanelSlide(View panel, float slideOffset) {
                    // 开始滑动返回时关闭软键盘
                    if (slideOffset < 0.03) {
                        SwipeBackUtil.closeKeyboard(mActivity);
                    }

                    mDelegate.onSwipeBackLayoutSlide(slideOffset);
                }

                @Override
                public void onPanelOpened(View panel) {
                    mDelegate.onSwipeBackLayoutExecuted();
                }

                @Override
                public void onPanelClosed(View panel) {
                    mDelegate.onSwipeBackLayoutCancel();
                }
            });
        }
    }

    /**
     * 设置滑动返回是否可用。默认值为 true
     *
     * @param swipeBackEnable
     * @return
     */
    public SwipeBackHelper setSwipeBackEnable(boolean swipeBackEnable) {
        if (mSwipeBackLayout != null) {
            mSwipeBackLayout.setSwipeBackEnable(swipeBackEnable);
        }
        return this;
    }

    /**
     * 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
     *
     * @param isOnlyTrackingLeftEdge
     * @return
     */
    public SwipeBackHelper setIsOnlyTrackingLeftEdge(boolean isOnlyTrackingLeftEdge) {
        if (mSwipeBackLayout != null) {
            mSwipeBackLayout.setIsOnlyTrackingLeftEdge(isOnlyTrackingLeftEdge);
        }
        return this;
    }

    /**
     * 设置是否是微信滑动返回样式。默认值为 true
     *
     * @param isWeChatStyle
     * @return
     */
    public SwipeBackHelper setIsWeChatStyle(boolean isWeChatStyle) {
        if (mSwipeBackLayout != null) {
            mSwipeBackLayout.setIsWeChatStyle(isWeChatStyle);
        }
        return this;
    }

    /**
     * 设置阴影资源 id。默认值为 R.drawable.maven_swipeback_shadow
     *
     * @param shadowResId
     * @return
     */
    public SwipeBackHelper setShadowResId(@DrawableRes int shadowResId) {
        if (mSwipeBackLayout != null) {
            mSwipeBackLayout.setShadowResId(shadowResId);
        }
        return this;
    }

    /**
     * 设置是否显示滑动返回的阴影效果。默认值为 true
     *
     * @param isNeedShowShadow
     * @return
     */
    public SwipeBackHelper setIsNeedShowShadow(boolean isNeedShowShadow) {
        if (mSwipeBackLayout != null) {
            mSwipeBackLayout.setIsNeedShowShadow(isNeedShowShadow);
        }
        return this;
    }

    /**
     * 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
     *
     * @param isShadowAlphaGradient
     * @return
     */
    public SwipeBackHelper setIsShadowAlphaGradient(boolean isShadowAlphaGradient) {
        if (mSwipeBackLayout != null) {
            mSwipeBackLayout.setIsShadowAlphaGradient(isShadowAlphaGradient);
        }
        return this;
    }

    /**
     * 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
     *
     * @param threshold
     */
    public SwipeBackHelper setSwipeBackThreshold(@FloatRange(from = 0.0f, to = 1.0f) float threshold) {
        if (mSwipeBackLayout != null) {
            mSwipeBackLayout.setSwipeBackThreshold(threshold);
        }
        return this;
    }

    /**
     * 设置底部导航条是否悬浮在内容上
     *
     * @param overlap
     */
    public SwipeBackHelper setIsNavigationBarOverlap(boolean overlap) {
        if (mSwipeBackLayout != null) {
            mSwipeBackLayout.setIsNavigationBarOverlap(overlap);
        }
        return this;
    }

    /**
     * 是否正在滑动
     *
     * @return
     */
    public boolean isSliding() {
        if (mSwipeBackLayout != null) {
            return mSwipeBackLayout.isSliding();
        }
        return false;
    }

    /**
     * 执行跳转到下一个 Activity 的动画
     */
    public void executeForwardAnim() {
        executeForwardAnim(mActivity);
    }

    /**
     * 执行回到到上一个 Activity 的动画
     */
    public void executeBackwardAnim() {
        executeBackwardAnim(mActivity);
    }

    /**
     * 执行滑动返回到到上一个 Activity 的动画
     */
    public void executeSwipeBackAnim() {
        executeSwipeBackAnim(mActivity);
    }

    /**
     * 执行跳转到下一个 Activity 的动画。这里弄成静态方法，方便在 Fragment 中调用
     */
    public static void executeForwardAnim(Activity activity) {
        activity.overridePendingTransition(R.anim.maven_swipeback_activity_forward_enter, R.anim.maven_swipeback_activity_forward_exit);
    }

    /**
     * 执行回到到上一个 Activity 的动画。这里弄成静态方法，方便在 Fragment 中调用
     */
    public static void executeBackwardAnim(Activity activity) {
        activity.overridePendingTransition(R.anim.maven_swipeback_activity_backward_enter, R.anim.maven_swipeback_activity_backward_exit);
    }

    /**
     * 执行滑动返回到到上一个 Activity 的动画。这里弄成静态方法，方便在 Fragment 中调用
     */
    public static void executeSwipeBackAnim(Activity activity) {
        activity.overridePendingTransition(R.anim.maven_swipeback_activity_swipeback_enter, R.anim.maven_swipeback_activity_swipeback_exit);
    }

    /**
     * 回到上一个 Activity，并销毁当前 Activity
     */
    public void backward() {
        SwipeBackUtil.closeKeyboard(mActivity);
        mActivity.finish();
        executeBackwardAnim();
    }

    /**
     * 滑动返回上一个 Activity，并销毁当前 Activity
     */
    public void swipeBackward() {
        SwipeBackUtil.closeKeyboard(mActivity);
        mActivity.finish();
        executeSwipeBackAnim();
    }

    public interface Delegate {
        /**
         * 是否支持滑动返回
         *
         * @return
         */
        boolean isSupportSwipeBack();

        /**
         * 正在滑动返回
         *
         * @param slideOffset 从 0 到 1
         */
        void onSwipeBackLayoutSlide(float slideOffset);

        /**
         * 没达到滑动返回的阈值，取消滑动返回动作，回到默认状态
         */
        void onSwipeBackLayoutCancel();

        /**
         * 滑动返回执行完毕，销毁当前 Activity
         */
        void onSwipeBackLayoutExecuted();
    }
}
