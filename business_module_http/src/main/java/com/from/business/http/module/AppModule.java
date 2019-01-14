/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.from.business.http.module;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import com.from.business.http.cache.Cache;
import com.from.business.http.cache.CacheType;
import com.from.business.http.integration.IRepositoryManager;
import com.from.business.http.integration.RepositoryManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * ================================================
 * 提供一些框架必须的实例的 {@link  }
 * <p>
 * Created by JessYan on 8/4/2016.
 * ================================================
 */
public abstract class AppModule {
    static Gson provideGson(Application application, @Nullable GsonConfiguration configuration) {
        GsonBuilder builder = new GsonBuilder();
        if (configuration != null)
            configuration.configGson(application, builder);
        return builder.create();
    }

    abstract IRepositoryManager bindRepositoryManager(RepositoryManager repositoryManager);

    static Cache<String, Object> provideExtras(Cache.Factory cacheFactory) {
        return cacheFactory.build(CacheType.EXTRAS);
    }

    public interface GsonConfiguration {
        void configGson(@NonNull Context context, @NonNull GsonBuilder builder);
    }
}
