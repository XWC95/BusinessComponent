package com.from.business.http.module.http;

import dagger.internal.Factory;
import com.from.business.http.log.RequestInterceptor;
import com.from.business.http.utils.Preconditions;

public final class HttpConfigModulePrintHttpLogLevelFactory
    implements Factory<RequestInterceptor.Level> {
    private final HttpConfigModule module;

    public HttpConfigModulePrintHttpLogLevelFactory(HttpConfigModule module) {
        this.module = module;
    }

    @Override
    public RequestInterceptor.Level get() {
        return provideInstance(module);
    }

    public static RequestInterceptor.Level provideInstance(HttpConfigModule module) {
        return proxyProvidePrintHttpLogLevel(module);
    }

    public static HttpConfigModulePrintHttpLogLevelFactory create(
        HttpConfigModule module) {
        return new HttpConfigModulePrintHttpLogLevelFactory(module);
    }

    public static RequestInterceptor.Level proxyProvidePrintHttpLogLevel(
        HttpConfigModule instance) {
        return Preconditions.checkNotNull(
            instance.providePrintHttpLogLevel(),
            "Cannot return null from a non-@Nullable @Provides method");
    }
}
