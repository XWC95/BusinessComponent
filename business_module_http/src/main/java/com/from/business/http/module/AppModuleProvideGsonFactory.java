package com.from.business.http.module;

import android.app.Application;

import com.from.business.http.dagger.Factory;
import com.from.business.http.dagger.Provider;
import com.from.business.http.utils.Preconditions;
import com.google.gson.Gson;

public final class AppModuleProvideGsonFactory implements Factory<Gson> {
    private final Provider<Application> applicationProvider;
    private final Provider<AppModule.GsonConfiguration> configurationProvider;

    public AppModuleProvideGsonFactory(
        Provider<Application> applicationProvider,
        Provider<AppModule.GsonConfiguration> configurationProvider) {
        this.applicationProvider = applicationProvider;
        this.configurationProvider = configurationProvider;
    }

    @Override
    public Gson get() {
        return provideInstance(applicationProvider, configurationProvider);
    }

    public static Gson provideInstance(
        Provider<Application> applicationProvider,
        Provider<AppModule.GsonConfiguration> configurationProvider) {
        return proxyProvideGson(applicationProvider.get(), configurationProvider.get());
    }

    public static AppModuleProvideGsonFactory create(
        Provider<Application> applicationProvider,
        Provider<AppModule.GsonConfiguration> configurationProvider) {
        return new AppModuleProvideGsonFactory(applicationProvider, configurationProvider);
    }

    public static Gson proxyProvideGson(
        Application application, AppModule.GsonConfiguration configuration) {
        return Preconditions.checkNotNull(
            AppModule.provideGson(application, configuration),
            "Cannot return null from a non-@Nullable @Provides method");
    }
}
