package com.from.business.http.module.http;

import android.app.Application;

import dagger.internal.Factory;
import dagger.internal.Preconditions;

import java.io.File;

import javax.inject.Provider;

public final class HttpConfigModuleCacheFileFactory implements Factory<File> {
    private final HttpConfigModule module;
    private final Provider<Application> applicationProvider;

    public HttpConfigModuleCacheFileFactory(
        HttpConfigModule module, Provider<Application> applicationProvider) {
        this.module = module;
        this.applicationProvider = applicationProvider;
    }

    @Override
    public File get() {
        return provideInstance(module, applicationProvider);
    }

    public static File provideInstance(
        HttpConfigModule module, Provider<Application> applicationProvider) {
        return proxyProvideCacheFile(module, applicationProvider.get());
    }

    public static HttpConfigModuleCacheFileFactory create(
        HttpConfigModule module, Provider<Application> applicationProvider) {
        return new HttpConfigModuleCacheFileFactory(module, applicationProvider);
    }

    public static File proxyProvideCacheFile(HttpConfigModule instance, Application application) {
        return Preconditions.checkNotNull(
            instance.provideCacheFile(application),
            "Cannot return null from a non-@Nullable @Provides method");
    }
}
