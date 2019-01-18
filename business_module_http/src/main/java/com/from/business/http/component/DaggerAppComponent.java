package com.from.business.http.component;

import android.app.Application;

import com.from.business.http.HttpHandler;
import com.from.business.http.cache.Cache;
import dagger.internal.DoubleCheck;
import dagger.internal.InstanceFactory;
import com.from.business.http.integration.IRepositoryManager;
import com.from.business.http.integration.RepositoryManager;
import com.from.business.http.log.FormatPrinter;
import com.from.business.http.log.RequestInterceptor;
import com.from.business.http.log.RequestInterceptorFactory;
import com.from.business.http.module.AppModule;
import com.from.business.http.module.AppModuleProvideExtrasFactory;
import com.from.business.http.module.AppModuleProvideGsonFactory;
import com.from.business.http.module.ClientModule;
import com.from.business.http.module.ClientModuleProRxErrorHandlerFactory;
import com.from.business.http.module.ClientModuleProvideClientBuilderFactory;
import com.from.business.http.module.ClientModuleProvideClientFactory;
import com.from.business.http.module.ClientModuleProvideRetrofitBuilderFactory;
import com.from.business.http.module.ClientModuleProvideRetrofitFactory;
import com.from.business.http.module.ClientModuleProvideRxCacheDirectoryFactory;
import com.from.business.http.module.ClientModuleProvideRxCacheFactory;
import com.from.business.http.module.http.HttpConfigModuleCacheFactoryFactory;
import com.from.business.http.module.http.HttpConfigModule;
import com.from.business.http.module.http.HttpConfigModuleBaseUrlFactory;
import com.from.business.http.module.http.HttpConfigModuleCacheFileFactory;
import com.from.business.http.module.http.HttpConfigModuleExecutorServiceFactory;
import com.from.business.http.module.http.HttpConfigModuleFormatPrinterFactory;
import com.from.business.http.module.http.HttpConfigModuleGlobalHttpHandlerFactory;
import com.from.business.http.module.http.HttpConfigModuleGsonConfigurationFactory;
import com.from.business.http.module.http.HttpConfigModuleInterceptorsFactory;
import com.from.business.http.module.http.HttpConfigModulePrintHttpLogLevelFactory;
import com.from.business.http.module.http.HttpConfigModuleResponseErrorListenerFactory;
import com.from.business.http.module.http.HttpConfigModuleRetrofitConfigurationFactory;
import com.from.business.http.module.http.HttpConfigModuleRxCacheConfigurationFactory;
import com.from.business.http.module.http.HttpConfigModuleOkhttpConfigurationFactory;
import com.from.business.http.module.RepositoryManagerFactory;
import dagger.internal.Preconditions;
import com.google.gson.Gson;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.inject.Provider;

import io.rx_cache2.internal.RxCache;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.listener.ResponseErrorListener;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public final class DaggerAppComponent implements AppComponent {
    private Application application;
    private Provider<Application> applicationProvider;
    private Provider<ClientModule.RetrofitConfiguration> provideRetrofitConfigurationProvider;
    private Provider<Retrofit.Builder> provideRetrofitBuilderProvider;
    private Provider<ClientModule.OkhttpConfiguration> provideOkhttpConfigurationProvider;
    private Provider<OkHttpClient.Builder> provideClientBuilderProvider;
    private Provider<HttpHandler> provideGlobalHttpHandlerProvider;
    private Provider<FormatPrinter> provideFormatPrinterProvider;
    private Provider<RequestInterceptor.Level> providePrintHttpLogLevelProvider;
    private Provider<RequestInterceptor> requestInterceptorProvider;
    private Provider<List<Interceptor>> provideInterceptorsProvider;
    private Provider<ExecutorService> provideExecutorServiceProvider;
    private Provider<OkHttpClient> provideClientProvider;
    private Provider<HttpUrl> provideBaseUrlProvider;
    private Provider<AppModule.GsonConfiguration> provideGsonConfigurationProvider;
    private Provider<Gson> provideGsonProvider;
    private Provider<Retrofit> provideRetrofitProvider;
    private Provider<ClientModule.RxCacheConfiguration> provideRxCacheConfigurationProvider;
    private Provider<File> provideCacheFileProvider;
    private Provider<File> provideRxCacheDirectoryProvider;
    private Provider<RxCache> provideRxCacheProvider;
    private Provider<Cache.Factory> provideCacheFactoryProvider;
    private Provider<RepositoryManager> repositoryManagerProvider;
    private Provider<ResponseErrorListener> provideResponseErrorListenerProvider;
    private Provider<RxErrorHandler> proRxErrorHandlerProvider;
    private Provider<Cache<String, Object>> provideExtrasProvider;

    private DaggerAppComponent(Builder builder) {
        initialize(builder);
    }

    public static AppComponent.Builder builder() {
        return new Builder();
    }

    @SuppressWarnings("unchecked")
    private void initialize(final Builder builder) {
        this.application = builder.application;
        this.applicationProvider = InstanceFactory.create(builder.application);
        this.provideRetrofitConfigurationProvider =
            DoubleCheck.provider(HttpConfigModuleRetrofitConfigurationFactory.create(builder.globalConfigModule));

        this.provideRetrofitBuilderProvider =
            DoubleCheck.provider(ClientModuleProvideRetrofitBuilderFactory.create());

        this.provideOkhttpConfigurationProvider =
            DoubleCheck.provider(HttpConfigModuleOkhttpConfigurationFactory.create(
                builder.globalConfigModule));

        this.provideClientBuilderProvider =
            DoubleCheck.provider(ClientModuleProvideClientBuilderFactory.create());

        this.provideGlobalHttpHandlerProvider =
            DoubleCheck.provider(
                HttpConfigModuleGlobalHttpHandlerFactory.create(builder.globalConfigModule));

        this.provideFormatPrinterProvider =
            DoubleCheck.provider(HttpConfigModuleFormatPrinterFactory.create(builder.globalConfigModule));

        this.providePrintHttpLogLevelProvider =
            DoubleCheck.provider(HttpConfigModulePrintHttpLogLevelFactory.create(builder.globalConfigModule));

        this.requestInterceptorProvider =
            DoubleCheck.provider(RequestInterceptorFactory.create(
                provideGlobalHttpHandlerProvider,
                provideFormatPrinterProvider,
                providePrintHttpLogLevelProvider));

        this.provideInterceptorsProvider =
            DoubleCheck.provider(HttpConfigModuleInterceptorsFactory.create(builder.globalConfigModule));

        this.provideExecutorServiceProvider =
            DoubleCheck.provider(HttpConfigModuleExecutorServiceFactory.create(builder.globalConfigModule));

        this.provideClientProvider =
            DoubleCheck.provider(ClientModuleProvideClientFactory.create(
                applicationProvider,
                provideOkhttpConfigurationProvider,
                provideClientBuilderProvider,
                (Provider) requestInterceptorProvider,
                provideInterceptorsProvider,
                provideGlobalHttpHandlerProvider,
                provideExecutorServiceProvider));
        this.provideBaseUrlProvider =
            DoubleCheck.provider(HttpConfigModuleBaseUrlFactory.create(builder.globalConfigModule));

        this.provideGsonConfigurationProvider =
            DoubleCheck.provider(HttpConfigModuleGsonConfigurationFactory.create(builder.globalConfigModule));

        this.provideGsonProvider =
            DoubleCheck.provider(AppModuleProvideGsonFactory.create(
                applicationProvider, provideGsonConfigurationProvider));

        this.provideRetrofitProvider =
            DoubleCheck.provider(ClientModuleProvideRetrofitFactory.create(
                applicationProvider,
                provideRetrofitConfigurationProvider,
                provideRetrofitBuilderProvider,
                provideClientProvider,
                provideBaseUrlProvider,
                provideGsonProvider));

        this.provideRxCacheConfigurationProvider =
            DoubleCheck.provider(HttpConfigModuleRxCacheConfigurationFactory.create(
                builder.globalConfigModule));

        this.provideCacheFileProvider =
            DoubleCheck.provider(HttpConfigModuleCacheFileFactory.create(
                builder.globalConfigModule, applicationProvider));

        this.provideRxCacheDirectoryProvider =
            DoubleCheck.provider(ClientModuleProvideRxCacheDirectoryFactory.create(provideCacheFileProvider));

        this.provideRxCacheProvider =
            DoubleCheck.provider(ClientModuleProvideRxCacheFactory.create(
                applicationProvider,
                provideRxCacheConfigurationProvider,
                provideRxCacheDirectoryProvider,
                provideGsonProvider));

        this.provideCacheFactoryProvider =
            DoubleCheck.provider(HttpConfigModuleCacheFactoryFactory.create(
                builder.globalConfigModule, applicationProvider));

        this.repositoryManagerProvider =
            DoubleCheck.provider(RepositoryManagerFactory.create(
                provideRetrofitProvider,
                provideRxCacheProvider,
                applicationProvider,
                provideCacheFactoryProvider));

        this.provideResponseErrorListenerProvider =
            DoubleCheck.provider(HttpConfigModuleResponseErrorListenerFactory.create(
                builder.globalConfigModule));

        this.proRxErrorHandlerProvider =
            DoubleCheck.provider(ClientModuleProRxErrorHandlerFactory.create(
                applicationProvider, provideResponseErrorListenerProvider));

        this.provideExtrasProvider =
            DoubleCheck.provider(AppModuleProvideExtrasFactory.create(provideCacheFactoryProvider));
    }

    @Override
    public Application application() {
        return application;
    }

    @Override
    public IRepositoryManager repositoryManager() {
        return repositoryManagerProvider.get();
    }

    @Override
    public RxErrorHandler rxErrorHandler() {
        return proRxErrorHandlerProvider.get();
    }

    @Override
    public OkHttpClient okHttpClient() {
        return provideClientProvider.get();
    }

    @Override
    public Gson gson() {
        return provideGsonProvider.get();
    }

    @Override
    public File cacheFile() {
        return provideCacheFileProvider.get();
    }

    @Override
    public Cache<String, Object> extras() {
        return provideExtrasProvider.get();
    }

    @Override
    public Cache.Factory cacheFactory() {
        return provideCacheFactoryProvider.get();
    }

    @Override
    public ExecutorService executorService() {
        return provideExecutorServiceProvider.get();
    }

    private static final class Builder implements AppComponent.Builder {
        private HttpConfigModule globalConfigModule;
        private Application application;

        @Override
        public AppComponent build() {
            if (globalConfigModule == null) {
                throw new IllegalStateException(
                    HttpConfigModule.class.getCanonicalName() + " must be set");
            }
            if (application == null) {
                throw new IllegalStateException(Application.class.getCanonicalName() + " must be set");
            }
            return new DaggerAppComponent(this);
        }

        @Override
        public Builder application(Application application) {
            this.application = Preconditions.checkNotNull(application);
            return this;
        }

        @Override
        public Builder globalConfigModule(HttpConfigModule globalConfigModule) {
            this.globalConfigModule = Preconditions.checkNotNull(globalConfigModule);
            return this;
        }
    }
}
