package com.from.business.http.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;

import java.io.File;

import javax.inject.Provider;

public final class ClientModuleProvideRxCacheDirectoryFactory implements Factory<File> {
    private final Provider<File> cacheDirProvider;

    public ClientModuleProvideRxCacheDirectoryFactory(Provider<File> cacheDirProvider) {
        this.cacheDirProvider = cacheDirProvider;
    }

    @Override
    public File get() {
        return provideInstance(cacheDirProvider);
    }

    public static File provideInstance(Provider<File> cacheDirProvider) {
        return proxyProvideRxCacheDirectory(cacheDirProvider.get());
    }

    public static ClientModuleProvideRxCacheDirectoryFactory create(
        Provider<File> cacheDirProvider) {
        return new ClientModuleProvideRxCacheDirectoryFactory(cacheDirProvider);
    }

    public static File proxyProvideRxCacheDirectory(File cacheDir) {
        return Preconditions.checkNotNull(
            ClientModule.provideRxCacheDirectory(cacheDir),
            "Cannot return null from a non-@Nullable @Provides method");
    }
}
