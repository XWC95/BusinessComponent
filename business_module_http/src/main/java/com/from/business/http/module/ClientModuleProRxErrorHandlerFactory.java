package com.from.business.http.module;

import android.app.Application;


import javax.inject.Provider;

import dagger.internal.Factory;


import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.listener.ResponseErrorListener;

import static com.from.business.http.utils.Preconditions.checkNotNull;

public final class ClientModuleProRxErrorHandlerFactory implements Factory<RxErrorHandler> {
    private final Provider<Application> applicationProvider;
    private final Provider<ResponseErrorListener> listenerProvider;

    public ClientModuleProRxErrorHandlerFactory(
            Provider<Application> applicationProvider, Provider<ResponseErrorListener> listenerProvider) {
        this.applicationProvider = applicationProvider;
        this.listenerProvider = listenerProvider;
    }

    @Override
    public RxErrorHandler get() {
        return provideInstance(applicationProvider, listenerProvider);
    }

    public static RxErrorHandler provideInstance(
            Provider<Application> applicationProvider, Provider<ResponseErrorListener> listenerProvider) {
        return proxyProRxErrorHandler(applicationProvider.get(), listenerProvider.get());
    }

    public static ClientModuleProRxErrorHandlerFactory create(
            Provider<Application> applicationProvider, Provider<ResponseErrorListener> listenerProvider) {
        return new ClientModuleProRxErrorHandlerFactory(applicationProvider, listenerProvider);
    }

    public static RxErrorHandler proxyProRxErrorHandler(
            Application application, ResponseErrorListener listener) {
        return checkNotNull(ClientModule.proRxErrorHandler(application, listener),
                "Cannot return null from a non-@Nullable @Provides method");
    }
}
