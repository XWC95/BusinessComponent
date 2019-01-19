package com.from.business.http.utils;

import android.content.Context;

import com.from.business.http.IAppComponent;
import com.from.business.http.component.AppComponent;


/**
 * @author Vea
 * @since 2019-01-14
 */
public class HttpModuleUtils {
    public static AppComponent obtainAppComponentFromContext(Context context) {
        Preconditions.checkNotNull(context, "%s cannot be null", Context.class.getName());
//        Preconditions.checkState(context.getApplicationContext() instanceof IAppComponent, "%s must be implements %s", context.getApplicationContext().getClass().getName(), IAppComponent.class.getName());
        return ((IAppComponent) context.getApplicationContext()).getAppComponent();
    }
}
