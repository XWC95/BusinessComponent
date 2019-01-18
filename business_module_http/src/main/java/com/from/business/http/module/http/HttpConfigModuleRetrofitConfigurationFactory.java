package com.from.business.http.module.http;

import android.support.annotation.Nullable;

import dagger.internal.Factory;
import com.from.business.http.module.ClientModule;

public final class HttpConfigModuleRetrofitConfigurationFactory
    implements Factory<ClientModule.RetrofitConfiguration> {
    private final HttpConfigModule module;

    public HttpConfigModuleRetrofitConfigurationFactory(HttpConfigModule module) {
        this.module = module;
    }

    @Override
    @Nullable
    public ClientModule.RetrofitConfiguration get() {
        return provideInstance(module);
    }

    @Nullable
    public static ClientModule.RetrofitConfiguration provideInstance(HttpConfigModule module) {
        return proxyProvideRetrofitConfiguration(module);
    }

    public static HttpConfigModuleRetrofitConfigurationFactory create(
        HttpConfigModule module) {
        return new HttpConfigModuleRetrofitConfigurationFactory(module);
    }

    @Nullable
    public static ClientModule.RetrofitConfiguration proxyProvideRetrofitConfiguration(
        HttpConfigModule instance) {
        return instance.provideRetrofitConfiguration();
    }
}
