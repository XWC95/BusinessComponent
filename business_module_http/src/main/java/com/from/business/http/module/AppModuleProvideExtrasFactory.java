package com.from.business.http.module;

import com.from.business.http.cache.Cache;
import dagger.internal.Factory;
import com.from.business.http.utils.Preconditions;

import javax.inject.Provider;


public final class AppModuleProvideExtrasFactory implements Factory<Cache<String, Object>> {
    private final Provider<Cache.Factory> cacheFactoryProvider;

    public AppModuleProvideExtrasFactory(Provider<Cache.Factory> cacheFactoryProvider) {
        this.cacheFactoryProvider = cacheFactoryProvider;
    }

    @Override
    public Cache<String, Object> get() {
        return provideInstance(cacheFactoryProvider);
    }

    public static Cache<String, Object> provideInstance(
        Provider<Cache.Factory> cacheFactoryProvider) {
        return proxyProvideExtras(cacheFactoryProvider.get());
    }

    public static AppModuleProvideExtrasFactory create(
        Provider<Cache.Factory> cacheFactoryProvider) {
        return new AppModuleProvideExtrasFactory(cacheFactoryProvider);
    }

    public static Cache<String, Object> proxyProvideExtras(Cache.Factory cacheFactory) {
        return Preconditions.checkNotNull(
            AppModule.provideExtras(cacheFactory),
            "Cannot return null from a non-@Nullable @Provides method");
    }
}
