package com.from.view.swipeback;

import java.util.Collections;
import java.util.List;

/**
 * @version 0.0.9
 * @since 2019-01
 */
public class SwipeOptions {
    private List<String> mClassNameList;
    private boolean mIsOnlyTrackingLeftEdge;
    private boolean mIsWeChatStyle;
    private int mShadowResId;
    private boolean mIsNeedShowShadow;
    private boolean mIsShadowAlphaGradient;
    private boolean mIsNavigationBarOverlap;

    private SwipeOptions(Builder builder) {
        this.mClassNameList = builder.mClassNameList;
        this.mIsOnlyTrackingLeftEdge = builder.mIsOnlyTrackingLeftEdge;
        this.mIsWeChatStyle = builder.mIsWeChatStyle;
        this.mShadowResId = builder.mShadowResId;
        this.mIsNeedShowShadow = builder.mIsNeedShowShadow;
        this.mIsShadowAlphaGradient = builder.mIsShadowAlphaGradient;
        this.mIsNavigationBarOverlap = builder.mIsNavigationBarOverlap;
    }

    public List<String> getClassNameList() {
        return mClassNameList;
    }

    public boolean getIsOnlyTrackingLeftEdge() {
        return mIsOnlyTrackingLeftEdge;
    }

    public boolean getIsWeChatStyle() {
        return mIsWeChatStyle;
    }

    public int getShadowResId() {
        return mShadowResId;
    }

    public boolean getIsNeedShowShadow() {
        return mIsNeedShowShadow;
    }

    public boolean getIsShadowAlphaGradient() {
        return mIsShadowAlphaGradient;
    }

    public boolean getIsNavigationBarOverlap() {
        return mIsNavigationBarOverlap;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Builder() {
        }
        // 不需要侧滑的类
        private List<String> mClassNameList = Collections.emptyList();
        // 是否仅仅跟踪左侧边缘的滑动返回
        private boolean mIsOnlyTrackingLeftEdge = true;
        // 是否是微信滑动返回样式
        private boolean mIsWeChatStyle = true;
        // 阴影资源 id
        private int mShadowResId = R.drawable.maven_swipeback_shadow;
        // 否显示滑动返回的阴影效果
        private boolean mIsNeedShowShadow = true;
        // 阴影区域的透明度是否根据滑动的距离渐变
        private boolean mIsShadowAlphaGradient = true;
        // 底部导航条是否悬浮在内容上
        private boolean mIsNavigationBarOverlap = false;

        public Builder exclude(List<String> classNameList) {
            this.mClassNameList = classNameList;
            return this;
        }

        public Builder isTrackingLeftEdge(boolean isOnlyTrackingLeftEdge) {
            mIsOnlyTrackingLeftEdge = isOnlyTrackingLeftEdge;
            return this;
        }

        public Builder isWeChatStyle(boolean mIsWeChatStyle) {
            this.mIsWeChatStyle = mIsWeChatStyle;
            return this;
        }

        public Builder shadowResId(int mShadowResId) {
            this.mShadowResId = mShadowResId;
            return this;
        }

        public Builder isNeedShowShadow(boolean mIsNeedShowShadow) {
            this.mIsNeedShowShadow = mIsNeedShowShadow;
            return this;
        }

        public Builder isShadowAlphaGradient(boolean mIsShadowAlphaGradient) {
            this.mIsShadowAlphaGradient = mIsShadowAlphaGradient;
            return this;
        }

        public Builder isNavigationBarOverlap(boolean mIsShadowAlphaGradient) {
            this.mIsNavigationBarOverlap = mIsShadowAlphaGradient;
            return this;
        }

        public SwipeOptions build() {
            return new SwipeOptions(this);
        }
    }
}
