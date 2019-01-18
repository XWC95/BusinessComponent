package com.from.business.http.module;

import android.app.Application;

import com.from.business.http.cache.Cache;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import com.from.business.http.integration.RepositoryManager;
import com.from.business.http.integration.RepositoryManager_MembersInjector;

import javax.inject.Provider;

import io.rx_cache2.internal.RxCache;
import retrofit2.Retrofit;

public final class RepositoryManagerFactory implements Factory<RepositoryManager> {
    private final Provider<Retrofit> mRetrofitProvider;
    private final Provider<RxCache> mRxCacheProvider;
    private final Provider<Application> mApplicationProvider;
    private final Provider<Cache.Factory> mCachefactoryProvider;

    public RepositoryManagerFactory(
        Provider<Retrofit> mRetrofitProvider,
        Provider<RxCache> mRxCacheProvider,
        Provider<Application> mApplicationProvider,
        Provider<Cache.Factory> mCachefactoryProvider) {
        this.mRetrofitProvider = mRetrofitProvider;
        this.mRxCacheProvider = mRxCacheProvider;
        this.mApplicationProvider = mApplicationProvider;
        this.mCachefactoryProvider = mCachefactoryProvider;
    }

    @Override
    public RepositoryManager get() {
        return provideInstance(
            mRetrofitProvider, mRxCacheProvider, mApplicationProvider, mCachefactoryProvider);
    }

    public static RepositoryManager provideInstance(
        Provider<Retrofit> mRetrofitProvider,
        Provider<RxCache> mRxCacheProvider,
        Provider<Application> mApplicationProvider,
        Provider<Cache.Factory> mCachefactoryProvider) {
        RepositoryManager instance = new RepositoryManager();
        RepositoryManager_MembersInjector.injectMRetrofit(
            instance, DoubleCheck.lazy(mRetrofitProvider));
        RepositoryManager_MembersInjector.injectMRxCache(instance, DoubleCheck.lazy(mRxCacheProvider));
        RepositoryManager_MembersInjector.injectMApplication(instance, mApplicationProvider.get());
        RepositoryManager_MembersInjector.injectMCachefactory(instance, mCachefactoryProvider.get());
        return instance;
    }

    public static RepositoryManagerFactory create(
        Provider<Retrofit> mRetrofitProvider,
        Provider<RxCache> mRxCacheProvider,
        Provider<Application> mApplicationProvider,
        Provider<Cache.Factory> mCachefactoryProvider) {
        return new RepositoryManagerFactory(
            mRetrofitProvider, mRxCacheProvider, mApplicationProvider, mCachefactoryProvider);
    }

    public static RepositoryManager newRepositoryManager() {
        return new RepositoryManager();
    }
}
