package me.businesscomponent;

import android.app.Application;

import com.from.view.swipeback.SwipeBackHelper;
import com.squareup.leakcanary.LeakCanary;

/**
 * @author Vea
 * @since 2019-01
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);


        /**
         * 必须在 Application 的 onCreate 方法中执行 SwipeBackHelper.init 来初始化滑动返回
         * 第一个参数：应用程序上下文
         * 第二个参数：如果发现滑动返回后立即触摸界面时应用崩溃，请把该界面里比较特殊的 View 的 class 添加到该集合中，目前在库中已经添加了 WebView 和 SurfaceView
         * 第三个参数：如果有些第三方库 Activity 不需要 swipeBack 可使用Option 配置
         */
//        List<String> exclude = new ArrayList<>();
//        exclude.add(SelectImageActivity.class.getSimpleName());
//        SwipeOptions options = SwipeOptions.builder().exclude(exclude).build();
        SwipeBackHelper.init(this, null, null);
    }
}
