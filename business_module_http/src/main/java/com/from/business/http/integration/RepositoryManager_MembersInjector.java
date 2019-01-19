package com.from.business.http.integration;

import android.app.Application;

import com.from.business.http.cache.Cache;

import javax.inject.Provider;

import dagger.internal.DoubleCheck;
import dagger.Lazy;
import dagger.MembersInjector;


import io.rx_cache2.internal.RxCache;
import retrofit2.Retrofit;

public final class RepositoryManager_MembersInjector implements MembersInjector<RepositoryManager> {
    private final Provider<Retrofit> mRetrofitProvider;
    private final Provider<RxCache> mRxCacheProvider;
    private final Provider<Application> mApplicationProvider;
    private final Provider<Cache.Factory> mCachefactoryProvider;

    public RepositoryManager_MembersInjector(
        Provider<Retrofit> mRetrofitProvider,
        Provider<RxCache> mRxCacheProvider,
        Provider<Application> mApplicationProvider,
        Provider<Cache.Factory> mCachefactoryProvider) {
        this.mRetrofitProvider = mRetrofitProvider;
        this.mRxCacheProvider = mRxCacheProvider;
        this.mApplicationProvider = mApplicationProvider;
        this.mCachefactoryProvider = mCachefactoryProvider;
    }

    public static MembersInjector<RepositoryManager> create(
        Provider<Retrofit> mRetrofitProvider,
        Provider<RxCache> mRxCacheProvider,
        Provider<Application> mApplicationProvider,
        Provider<Cache.Factory> mCachefactoryProvider) {
        return new RepositoryManager_MembersInjector(
            mRetrofitProvider, mRxCacheProvider, mApplicationProvider, mCachefactoryProvider);
    }

    @Override
    public void injectMembers(RepositoryManager instance) {
        injectMRetrofit(instance, DoubleCheck.lazy(mRetrofitProvider));
        injectMRxCache(instance, DoubleCheck.lazy(mRxCacheProvider));
        injectMApplication(instance, mApplicationProvider.get());
        injectMCachefactory(instance, mCachefactoryProvider.get());
    }

    public static void injectMRetrofit(RepositoryManager instance, Lazy<Retrofit> mRetrofit) {
        instance.mRetrofit = mRetrofit;
    }

    public static void injectMRxCache(RepositoryManager instance, Lazy<RxCache> mRxCache) {
        instance.mRxCache = mRxCache;
    }

    public static void injectMApplication(RepositoryManager instance, Application mApplication) {
        instance.mApplication = mApplication;
    }

    public static void injectMCachefactory(RepositoryManager instance, Cache.Factory mCachefactory) {
        instance.mCachefactory = mCachefactory;
    }
}
