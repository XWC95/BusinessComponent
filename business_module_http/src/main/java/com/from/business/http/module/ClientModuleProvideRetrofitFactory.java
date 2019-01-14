package com.from.business.http.module;

import android.app.Application;

import com.from.business.http.dagger.Factory;
import com.from.business.http.dagger.Provider;
import com.from.business.http.utils.Preconditions;
import com.google.gson.Gson;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public final class ClientModuleProvideRetrofitFactory implements Factory<Retrofit> {
    private final Provider<Application> applicationProvider;
    private final Provider<ClientModule.RetrofitConfiguration> configurationProvider;
    private final Provider<Retrofit.Builder> builderProvider;
    private final Provider<OkHttpClient> clientProvider;
    private final Provider<HttpUrl> httpUrlProvider;
    private final Provider<Gson> gsonProvider;

    public ClientModuleProvideRetrofitFactory(
        Provider<Application> applicationProvider,
        Provider<ClientModule.RetrofitConfiguration> configurationProvider,
        Provider<Retrofit.Builder> builderProvider,
        Provider<OkHttpClient> clientProvider,
        Provider<HttpUrl> httpUrlProvider,
        Provider<Gson> gsonProvider) {
        this.applicationProvider = applicationProvider;
        this.configurationProvider = configurationProvider;
        this.builderProvider = builderProvider;
        this.clientProvider = clientProvider;
        this.httpUrlProvider = httpUrlProvider;
        this.gsonProvider = gsonProvider;
    }

    @Override
    public Retrofit get() {
        return provideInstance(
            applicationProvider,
            configurationProvider,
            builderProvider,
            clientProvider,
            httpUrlProvider,
            gsonProvider);
    }

    public static Retrofit provideInstance(
        Provider<Application> applicationProvider,
        Provider<ClientModule.RetrofitConfiguration> configurationProvider,
        Provider<Retrofit.Builder> builderProvider,
        Provider<OkHttpClient> clientProvider,
        Provider<HttpUrl> httpUrlProvider,
        Provider<Gson> gsonProvider) {
        return proxyProvideRetrofit(
            applicationProvider.get(),
            configurationProvider.get(),
            builderProvider.get(),
            clientProvider.get(),
            httpUrlProvider.get(),
            gsonProvider.get());
    }

    public static ClientModuleProvideRetrofitFactory create(
        Provider<Application> applicationProvider,
        Provider<ClientModule.RetrofitConfiguration> configurationProvider,
        Provider<Retrofit.Builder> builderProvider,
        Provider<OkHttpClient> clientProvider,
        Provider<HttpUrl> httpUrlProvider,
        Provider<Gson> gsonProvider) {
        return new ClientModuleProvideRetrofitFactory(
            applicationProvider,
            configurationProvider,
            builderProvider,
            clientProvider,
            httpUrlProvider,
            gsonProvider);
    }

    public static Retrofit proxyProvideRetrofit(
        Application application,
        ClientModule.RetrofitConfiguration configuration,
        Retrofit.Builder builder,
        OkHttpClient client,
        HttpUrl httpUrl,
        Gson gson) {
        return Preconditions.checkNotNull(
            ClientModule.provideRetrofit(application, configuration, builder, client, httpUrl, gson),
            "Cannot return null from a non-@Nullable @Provides method");
    }
}
