package com.from.business.http.module.http;

import android.app.Application;

import com.from.business.http.cache.Cache;

import javax.inject.Provider;

import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class HttpConfigModuleCacheFactoryFactory implements Factory<Cache.Factory> {
    private final HttpConfigModule module;
    private final Provider<Application> applicationProvider;

    public HttpConfigModuleCacheFactoryFactory(
        HttpConfigModule module, Provider<Application> applicationProvider) {
        this.module = module;
        this.applicationProvider = applicationProvider;
    }

    @Override
    public Cache.Factory get() {
        return provideInstance(module, applicationProvider);
    }

    public static Cache.Factory provideInstance(
        HttpConfigModule module, Provider<Application> applicationProvider) {
        return proxyProvideCacheFactory(module, applicationProvider.get());
    }

    public static HttpConfigModuleCacheFactoryFactory create(
        HttpConfigModule module, Provider<Application> applicationProvider) {
        return new HttpConfigModuleCacheFactoryFactory(module, applicationProvider);
    }

    public static Cache.Factory proxyProvideCacheFactory(
        HttpConfigModule instance, Application application) {
        return Preconditions.checkNotNull(
            instance.provideCacheFactory(application),
            "Cannot return null from a non-@Nullable @Provides method");
    }
}
