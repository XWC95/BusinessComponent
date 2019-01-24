package com.from.business.http.utils;

import android.content.Context;

import com.from.business.http.HttpHandlerImpl;
import com.from.business.http.IAppComponent;
import com.from.business.http.ResponseErrorListenerImpl;
import com.from.business.http.component.AppComponent;
import com.from.business.http.module.http.HttpConfigModule;
import com.from.business.http.retrofiturlmanager.RetrofitUrlManager;

import java.util.concurrent.TimeUnit;

/**
 * @author Vea
 * @since 2019-01-14
 */
public class HttpModuleUtils {
    public static AppComponent obtainAppComponent(Context context) {
        Preconditions.checkNotNull(context, "%s cannot be null", Context.class.getName());
//        Preconditions.checkState(context.getApplicationContext() instanceof IAppComponent, "%s must be implements %s", context.getApplicationContext().getClass().getName(), IAppComponent.class.getName());
        return ((IAppComponent) context.getApplicationContext()).getAppComponent();
    }

}
