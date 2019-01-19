package com.from.business.http.module.http;

import dagger.internal.Factory;
import com.from.business.http.utils.Preconditions;


import okhttp3.HttpUrl;

public final class HttpConfigModuleBaseUrlFactory implements Factory<HttpUrl> {
    private final HttpConfigModule module;

    public HttpConfigModuleBaseUrlFactory(HttpConfigModule module) {
        this.module = module;
    }

    @Override
    public HttpUrl get() {
        return provideInstance(module);
    }

    public static HttpUrl provideInstance(HttpConfigModule module) {
        return proxyProvideBaseUrl(module);
    }

    public static HttpConfigModuleBaseUrlFactory create(HttpConfigModule module) {
        return new HttpConfigModuleBaseUrlFactory(module);
    }

    public static HttpUrl proxyProvideBaseUrl(HttpConfigModule instance) {
        return Preconditions.checkNotNull(
            instance.provideBaseUrl(), "Cannot return null from a non-@Nullable @Provides method");
    }
}
