package me.businesscomponent.utils;

import android.arch.lifecycle.LifecycleOwner;

import com.from.business.http.lifecycle.AndroidLifecycleScopeProvider;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;

public class RxLifecycleUtils {

    public static <T> AutoDisposeConverter<T> bindLifecycle(LifecycleOwner lifecycleOwner) {
        return AutoDispose.autoDisposable(
                AndroidLifecycleScopeProvider.from(lifecycleOwner)
        );
    }
}