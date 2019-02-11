package com.from.business.http.module;

import com.from.business.http.utils.Preconditions;

import dagger.internal.Factory;
import retrofit2.Retrofit;

public final class ClientModuleProvideRetrofitBuilderFactory implements Factory<Retrofit.Builder> {
    private static final ClientModuleProvideRetrofitBuilderFactory INSTANCE =
        new ClientModuleProvideRetrofitBuilderFactory();

    @Override
    public Retrofit.Builder get() {
        return provideInstance();
    }

    public static Retrofit.Builder provideInstance() {
        return proxyProvideRetrofitBuilder();
    }

    public static ClientModuleProvideRetrofitBuilderFactory create() {
        return INSTANCE;
    }

    public static Retrofit.Builder proxyProvideRetrofitBuilder() {
        return Preconditions.checkNotNull(
            ClientModule.provideRetrofitBuilder(),
            "Cannot return null from a non-@Nullable @Provides method");
    }
}
