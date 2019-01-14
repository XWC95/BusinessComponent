package com.from.business.http.module.http;

import com.from.business.http.dagger.Factory;
import com.from.business.http.utils.Preconditions;

import me.jessyan.rxerrorhandler.handler.listener.ResponseErrorListener;

public final class HttpConfigModuleResponseErrorListenerFactory
    implements Factory<ResponseErrorListener> {
    private final HttpConfigModule module;

    public HttpConfigModuleResponseErrorListenerFactory(HttpConfigModule module) {
        this.module = module;
    }

    @Override
    public ResponseErrorListener get() {
        return provideInstance(module);
    }

    public static ResponseErrorListener provideInstance(HttpConfigModule module) {
        return proxyProvideResponseErrorListener(module);
    }

    public static HttpConfigModuleResponseErrorListenerFactory create(
        HttpConfigModule module) {
        return new HttpConfigModuleResponseErrorListenerFactory(module);
    }

    public static ResponseErrorListener proxyProvideResponseErrorListener(
        HttpConfigModule instance) {
        return Preconditions.checkNotNull(
            instance.provideResponseErrorListener(),
            "Cannot return null from a non-@Nullable @Provides method");
    }
}
