package com.from.business.http.module;

import android.app.Application;

import com.from.business.http.dagger.Factory;
import com.from.business.http.dagger.Provider;
import com.from.business.http.utils.Preconditions;
import com.google.gson.Gson;

import java.io.File;

import io.rx_cache2.internal.RxCache;

public final class ClientModuleProvideRxCacheFactory implements Factory<RxCache> {
    private final Provider<Application> applicationProvider;
    private final Provider<ClientModule.RxCacheConfiguration> configurationProvider;
    private final Provider<File> cacheDirectoryProvider;
    private final Provider<Gson> gsonProvider;

    public ClientModuleProvideRxCacheFactory(
        Provider<Application> applicationProvider,
        Provider<ClientModule.RxCacheConfiguration> configurationProvider,
        Provider<File> cacheDirectoryProvider,
        Provider<Gson> gsonProvider) {
        this.applicationProvider = applicationProvider;
        this.configurationProvider = configurationProvider;
        this.cacheDirectoryProvider = cacheDirectoryProvider;
        this.gsonProvider = gsonProvider;
    }

    @Override
    public RxCache get() {
        return provideInstance(
            applicationProvider, configurationProvider, cacheDirectoryProvider, gsonProvider);
    }

    public static RxCache provideInstance(
        Provider<Application> applicationProvider,
        Provider<ClientModule.RxCacheConfiguration> configurationProvider,
        Provider<File> cacheDirectoryProvider,
        Provider<Gson> gsonProvider) {
        return proxyProvideRxCache(
            applicationProvider.get(),
            configurationProvider.get(),
            cacheDirectoryProvider.get(),
            gsonProvider.get());
    }

    public static ClientModuleProvideRxCacheFactory create(
        Provider<Application> applicationProvider,
        Provider<ClientModule.RxCacheConfiguration> configurationProvider,
        Provider<File> cacheDirectoryProvider,
        Provider<Gson> gsonProvider) {
        return new ClientModuleProvideRxCacheFactory(
            applicationProvider, configurationProvider, cacheDirectoryProvider, gsonProvider);
    }

    public static RxCache proxyProvideRxCache(
        Application application,
        ClientModule.RxCacheConfiguration configuration,
        File cacheDirectory,
        Gson gson) {
        return Preconditions.checkNotNull(
            ClientModule.provideRxCache(application, configuration, cacheDirectory, gson),
            "Cannot return null from a non-@Nullable @Provides method");
    }
}
