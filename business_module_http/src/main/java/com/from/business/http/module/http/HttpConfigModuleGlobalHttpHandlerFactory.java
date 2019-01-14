package com.from.business.http.module.http;

import android.support.annotation.Nullable;

import com.from.business.http.HttpHandler;
import com.from.business.http.dagger.Factory;

public final class HttpConfigModuleGlobalHttpHandlerFactory
    implements Factory<HttpHandler> {
    private final HttpConfigModule module;

    public HttpConfigModuleGlobalHttpHandlerFactory(HttpConfigModule module) {
        this.module = module;
    }

    @Override
    @Nullable
    public HttpHandler get() {
        return provideInstance(module);
    }

    @Nullable
    public static HttpHandler provideInstance(HttpConfigModule module) {
        return proxyProvideGlobalHttpHandler(module);
    }

    public static HttpConfigModuleGlobalHttpHandlerFactory create(
        HttpConfigModule module) {
        return new HttpConfigModuleGlobalHttpHandlerFactory(module);
    }

    @Nullable
    public static HttpHandler proxyProvideGlobalHttpHandler(HttpConfigModule instance) {
        return instance.provideGlobalHttpHandler();
    }
}
