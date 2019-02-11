package com.from.business.http.module;

import com.from.business.http.utils.Preconditions;

import dagger.internal.Factory;
import okhttp3.OkHttpClient;

public final class ClientModuleProvideClientBuilderFactory
    implements Factory<OkHttpClient.Builder> {
    private static final ClientModuleProvideClientBuilderFactory INSTANCE =
        new ClientModuleProvideClientBuilderFactory();

    @Override
    public OkHttpClient.Builder get() {
        return provideInstance();
    }

    public static OkHttpClient.Builder provideInstance() {
        return proxyProvideClientBuilder();
    }

    public static ClientModuleProvideClientBuilderFactory create() {
        return INSTANCE;
    }

    public static OkHttpClient.Builder proxyProvideClientBuilder() {
        return Preconditions.checkNotNull(
            ClientModule.provideClientBuilder(),
            "Cannot return null from a non-@Nullable @Provides method");
    }
}
