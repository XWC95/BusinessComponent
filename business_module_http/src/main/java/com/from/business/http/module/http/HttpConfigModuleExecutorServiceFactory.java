package com.from.business.http.module.http;

import com.from.business.http.dagger.Factory;
import com.from.business.http.utils.Preconditions;

import java.util.concurrent.ExecutorService;

public final class HttpConfigModuleExecutorServiceFactory
    implements Factory<ExecutorService> {
    private final HttpConfigModule module;

    public HttpConfigModuleExecutorServiceFactory(HttpConfigModule module) {
        this.module = module;
    }

    @Override
    public ExecutorService get() {
        return provideInstance(module);
    }

    public static ExecutorService provideInstance(HttpConfigModule module) {
        return proxyProvideExecutorService(module);
    }

    public static HttpConfigModuleExecutorServiceFactory create(HttpConfigModule module) {
        return new HttpConfigModuleExecutorServiceFactory(module);
    }

    public static ExecutorService proxyProvideExecutorService(HttpConfigModule instance) {
        return Preconditions.checkNotNull(
            instance.provideExecutorService(),
            "Cannot return null from a non-@Nullable @Provides method");
    }
}
