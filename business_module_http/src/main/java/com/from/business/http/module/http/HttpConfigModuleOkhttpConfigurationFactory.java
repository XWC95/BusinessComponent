package com.from.business.http.module.http;

import android.support.annotation.Nullable;

import com.from.business.http.module.ClientModule;

import dagger.internal.Factory;

public final class HttpConfigModuleOkhttpConfigurationFactory
    implements Factory<ClientModule.OkhttpConfiguration> {
  private final HttpConfigModule module;

  public HttpConfigModuleOkhttpConfigurationFactory(HttpConfigModule module) {
    this.module = module;
  }

  @Override
  @Nullable
  public ClientModule.OkhttpConfiguration get() {
    return provideInstance(module);
  }

  @Nullable
  public static ClientModule.OkhttpConfiguration provideInstance(HttpConfigModule module) {
    return proxyProvideOkhttpConfiguration(module);
  }

  public static HttpConfigModuleOkhttpConfigurationFactory create(
      HttpConfigModule module) {
    return new HttpConfigModuleOkhttpConfigurationFactory(module);
  }

  @Nullable
  public static ClientModule.OkhttpConfiguration proxyProvideOkhttpConfiguration(
      HttpConfigModule instance) {
    return instance.provideOkhttpConfiguration();
  }
}
