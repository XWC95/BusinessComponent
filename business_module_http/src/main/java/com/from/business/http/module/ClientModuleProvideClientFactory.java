package com.from.business.http.module;

import android.app.Application;

import com.from.business.http.HttpHandler;
import dagger.internal.Factory;
import com.from.business.http.utils.Preconditions;


import java.util.List;
import java.util.concurrent.ExecutorService;


import javax.inject.Provider;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

public final class ClientModuleProvideClientFactory implements Factory<OkHttpClient> {
    private final Provider<Application> applicationProvider;
    private final Provider<ClientModule.OkhttpConfiguration> configurationProvider;
    private final Provider<OkHttpClient.Builder> builderProvider;
    private final Provider<Interceptor> interceptProvider;
    private final Provider<List<Interceptor>> interceptorsProvider;
    private final Provider<HttpHandler> handlerProvider;
    private final Provider<ExecutorService> executorServiceProvider;

    public ClientModuleProvideClientFactory(
        Provider<Application> applicationProvider,
        Provider<ClientModule.OkhttpConfiguration> configurationProvider,
        Provider<OkHttpClient.Builder> builderProvider,
        Provider<Interceptor> interceptProvider,
        Provider<List<Interceptor>> interceptorsProvider,
        Provider<HttpHandler> handlerProvider,
        Provider<ExecutorService> executorServiceProvider) {
        this.applicationProvider = applicationProvider;
        this.configurationProvider = configurationProvider;
        this.builderProvider = builderProvider;
        this.interceptProvider = interceptProvider;
        this.interceptorsProvider = interceptorsProvider;
        this.handlerProvider = handlerProvider;
        this.executorServiceProvider = executorServiceProvider;
    }

    @Override
    public OkHttpClient get() {
        return provideInstance(
            applicationProvider,
            configurationProvider,
            builderProvider,
            interceptProvider,
            interceptorsProvider,
            handlerProvider,
            executorServiceProvider);
    }

    public static OkHttpClient provideInstance(
        Provider<Application> applicationProvider,
        Provider<ClientModule.OkhttpConfiguration> configurationProvider,
        Provider<OkHttpClient.Builder> builderProvider,
        Provider<Interceptor> interceptProvider,
        Provider<List<Interceptor>> interceptorsProvider,
        Provider<HttpHandler> handlerProvider,
        Provider<ExecutorService> executorServiceProvider) {
        return proxyProvideClient(
            applicationProvider.get(),
            configurationProvider.get(),
            builderProvider.get(),
            interceptProvider.get(),
            interceptorsProvider.get(),
            handlerProvider.get(),
            executorServiceProvider.get());
    }

    public static ClientModuleProvideClientFactory create(
        Provider<Application> applicationProvider,
        Provider<ClientModule.OkhttpConfiguration> configurationProvider,
        Provider<OkHttpClient.Builder> builderProvider,
        Provider<Interceptor> interceptProvider,
        Provider<List<Interceptor>> interceptorsProvider,
        Provider<HttpHandler> handlerProvider,
        Provider<ExecutorService> executorServiceProvider) {
        return new ClientModuleProvideClientFactory(
            applicationProvider,
            configurationProvider,
            builderProvider,
            interceptProvider,
            interceptorsProvider,
            handlerProvider,
            executorServiceProvider);
    }

    public static OkHttpClient proxyProvideClient(
        Application application,
        ClientModule.OkhttpConfiguration configuration,
        OkHttpClient.Builder builder,
        Interceptor intercept,
        List<Interceptor> interceptors,
        HttpHandler handler,
        ExecutorService executorService) {
        return Preconditions.checkNotNull(
            ClientModule.provideClient(
                application, configuration, builder, intercept, interceptors, handler, executorService),
            "Cannot return null from a non-@Nullable @Provides method");
    }
}
