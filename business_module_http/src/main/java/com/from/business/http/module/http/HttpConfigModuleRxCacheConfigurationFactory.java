package com.from.business.http.module.http;

import android.support.annotation.Nullable;

import com.from.business.http.module.ClientModule;

import dagger.internal.Factory;

public final class HttpConfigModuleRxCacheConfigurationFactory
    implements Factory<ClientModule.RxCacheConfiguration> {
    private final HttpConfigModule module;

    public HttpConfigModuleRxCacheConfigurationFactory(HttpConfigModule module) {
        this.module = module;
    }

    @Override
    @Nullable
    public ClientModule.RxCacheConfiguration get() {
        return provideInstance(module);
    }

    @Nullable
    public static ClientModule.RxCacheConfiguration provideInstance(HttpConfigModule module) {
        return proxyProvideRxCacheConfiguration(module);
    }

    public static HttpConfigModuleRxCacheConfigurationFactory create(
        HttpConfigModule module) {
        return new HttpConfigModuleRxCacheConfigurationFactory(module);
    }

    @Nullable
    public static ClientModule.RxCacheConfiguration proxyProvideRxCacheConfiguration(
        HttpConfigModule instance) {
        return instance.provideRxCacheConfiguration();
    }
}
