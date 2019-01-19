package com.from.business.http.log;

import com.from.business.http.HttpHandler;

import javax.inject.Provider;

import dagger.MembersInjector;


public final class RequestInterceptorMembersInjector
    implements MembersInjector<RequestInterceptor> {
    private final Provider<HttpHandler> mHandlerProvider;
    private final Provider<FormatPrinter> mPrinterProvider;
    private final Provider<RequestInterceptor.Level> printLevelProvider;

    public RequestInterceptorMembersInjector(
        Provider<HttpHandler> mHandlerProvider,
        Provider<FormatPrinter> mPrinterProvider,
        Provider<RequestInterceptor.Level> printLevelProvider) {
        this.mHandlerProvider = mHandlerProvider;
        this.mPrinterProvider = mPrinterProvider;
        this.printLevelProvider = printLevelProvider;
    }

    public static MembersInjector<RequestInterceptor> create(
        Provider<HttpHandler> mHandlerProvider,
        Provider<FormatPrinter> mPrinterProvider,
        Provider<RequestInterceptor.Level> printLevelProvider) {
        return new RequestInterceptorMembersInjector(
            mHandlerProvider, mPrinterProvider, printLevelProvider);
    }

    @Override
    public void injectMembers(RequestInterceptor instance) {
        injectMHandler(instance, mHandlerProvider.get());
        injectMPrinter(instance, mPrinterProvider.get());
        injectPrintLevel(instance, printLevelProvider.get());
    }

    public static void injectMHandler(RequestInterceptor instance, HttpHandler mHandler) {
        instance.mHandler = mHandler;
    }

    public static void injectMPrinter(RequestInterceptor instance, FormatPrinter mPrinter) {
        instance.mPrinter = mPrinter;
    }

    public static void injectPrintLevel(
        RequestInterceptor instance, RequestInterceptor.Level printLevel) {
        instance.printLevel = printLevel;
    }
}
