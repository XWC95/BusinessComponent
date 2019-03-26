
package com.from.view.swipeback;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.SurfaceView;
import android.view.View;
import android.webkit.WebView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * @author Vea
 * @version 0.0.9
 * @since 2019-1
 */
final class SwipeBackManager implements Application.ActivityLifecycleCallbacks {
    private static final SwipeBackManager sInstance = new SwipeBackManager();
    private Stack<Activity> mActivityStack = new Stack<>();
    private Set<Class<? extends View>> mProblemViewClassSet = new HashSet<>();
    private SwipeOptions mOptions;
    private SwipeBackHelper.Delegate mDelegate;

    public static SwipeBackManager getInstance() {
        return sInstance;
    }

    private SwipeBackManager() {
    }

    public void init(@NonNull Application application,
        @Nullable List<Class<? extends View>> problemViewClassList,
        @Nullable SwipeOptions options,
        SwipeBackHelper.Delegate delegate) {
        application.registerActivityLifecycleCallbacks(this);

        mProblemViewClassSet.add(WebView.class);
        mProblemViewClassSet.add(SurfaceView.class);
        if (problemViewClassList != null) {
            mProblemViewClassSet.addAll(problemViewClassList);
        }
        if (options != null) {
            mOptions = options;
        }
        if (delegate != null) {
            this.mDelegate = delegate;
        }
    }

    @Override
    public void onActivityCreated(final Activity activity, Bundle savedInstanceState) {
        SwipeBackUtil.d("SwipeBackManager observe Activity onCreate");
        SwipeBackUtil.d("Activity info : package" + activity.getPackageName() + "___SimpleName：" + activity.getClass().getSimpleName());

        mActivityStack.add(activity);
        if (mOptions != null) {
            for (String className : mOptions.getClassNameList()) {
                if (activity.getClass().getSimpleName().equals(className)) {
                    SwipeBackUtil.d("SwipeBackManager exclude of  " + activity.getClass().getSimpleName());
                    return;
                }
            }
        }
        SwipeBackHelper swipeBackHelper = SwipeBackHelper.create(activity);
        swipeBackHelper.setSlideDelegate(mDelegate);

//        activity.getWindow().getDecorView().post(new Runnable() {
//            @Override
//            public void run() {
//                ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
//                int childCount = decorView.getChildCount();
//                for(int i = 0 ; i<childCount ; i ++){
//                    String simpleName = decorView.getChildAt(i).getClass().getSimpleName();
//                    SwipeBackUtil.d(simpleName);
//                }
//            }
//        });

    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        SwipeBackUtil.d("SwipeBackManager observe Activity Destroyed");
        mActivityStack.remove(activity);
    }

    /**
     * 获取倒数第二个 Activity
     *
     * @return
     */
    @Nullable
    public Activity getPenultimateActivity(Activity currentActivity) {
        Activity activity = null;
        try {
            if (mActivityStack.size() > 1) {
                activity = mActivityStack.get(mActivityStack.size() - 2);

                if (currentActivity.equals(activity)) {
                    int index = mActivityStack.indexOf(currentActivity);
                    if (index > 0) {
                        // 处理内存泄漏或最后一个 Activity 正在 finishing 的情况
                        activity = mActivityStack.get(index - 1);
                    } else if (mActivityStack.size() == 2) {
                        // 处理屏幕旋转后 mActivityStack 中顺序错乱
                        activity = mActivityStack.lastElement();
                    }
                }
            }
        } catch (Exception e) {
        }
        return activity;
    }

    /**
     * 滑动返回是否可用
     *
     * @return
     */
    public boolean isSwipeBackEnable() {
        return mActivityStack.size() > 1;
    }

    /**
     * 某个 view 是否会导致滑动返回后立即触摸界面时应用崩溃
     *
     * @param view
     * @return
     */
    public boolean isProblemView(View view) {
        return mProblemViewClassSet.contains(view.getClass());
    }

    @NonNull
    public SwipeOptions getOptions() {
        if (mOptions == null) {
            return SwipeOptions.builder().build();
        }
        return mOptions;
    }
}