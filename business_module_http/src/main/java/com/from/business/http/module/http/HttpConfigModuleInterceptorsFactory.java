package com.from.business.http.module.http;

import android.support.annotation.Nullable;

import dagger.internal.Factory;

import java.util.List;

import okhttp3.Interceptor;

public final class HttpConfigModuleInterceptorsFactory
    implements Factory<List<Interceptor>> {
    private final HttpConfigModule module;

    public HttpConfigModuleInterceptorsFactory(HttpConfigModule module) {
        this.module = module;
    }

    @Override
    @Nullable
    public List<Interceptor> get() {
        return provideInstance(module);
    }

    @Nullable
    public static List<Interceptor> provideInstance(HttpConfigModule module) {
        return proxyProvideInterceptors(module);
    }

    public static HttpConfigModuleInterceptorsFactory create(HttpConfigModule module) {
        return new HttpConfigModuleInterceptorsFactory(module);
    }

    @Nullable
    public static List<Interceptor> proxyProvideInterceptors(HttpConfigModule instance) {
        return instance.provideInterceptors();
    }
}
