package com.from.business.http.module.http;

import android.support.annotation.Nullable;

import com.from.business.http.module.AppModule;

import dagger.internal.Factory;

public final class HttpConfigModuleGsonConfigurationFactory
    implements Factory<AppModule.GsonConfiguration> {
    private final HttpConfigModule module;

    public HttpConfigModuleGsonConfigurationFactory(HttpConfigModule module) {
        this.module = module;
    }

    @Override
    @Nullable
    public AppModule.GsonConfiguration get() {
        return provideInstance(module);
    }

    @Nullable
    public static AppModule.GsonConfiguration provideInstance(HttpConfigModule module) {
        return proxyProvideGsonConfiguration(module);
    }

    public static HttpConfigModuleGsonConfigurationFactory create(
        HttpConfigModule module) {
        return new HttpConfigModuleGsonConfigurationFactory(module);
    }

    @Nullable
    public static AppModule.GsonConfiguration proxyProvideGsonConfiguration(
        HttpConfigModule instance) {
        return instance.provideGsonConfiguration();
    }
}
