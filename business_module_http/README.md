# business_module_http

### 介绍
封装最火的Retrofit ，Rx ，Oktttp 网络请求框架，让开发者轻松面对网络请求

#### 依赖库
```
    api rootProject.ext.dependencies["rxjava2"]
    api rootProject.ext.dependencies["rxandroid2"]
    api rootProject.ext.dependencies["rxcache2"] // 项目里排除了 dagger
    implementation rootProject.ext.dependencies["rxcache-jolyglot-gson"]

    [api rootProject.ext.dependencies['rxerrorhandler2']](https://github.com/JessYanCoding/RxErrorHandler)

    api rootProject.ext.dependencies["retrofit"]
    implementation rootProject.ext.dependencies["retrofit-converter-gson"]
    implementation rootProject.ext.dependencies["retrofit-adapter-rxjava2"]
    api rootProject.ext.dependencies["okhttp3"]
    api rootProject.ext.dependencies["gson"]
    [compileOnly 'com.uber.autodispose:autodispose-android:1.1.0'](https://github.com/uber/AutoDispose)
    compileOnly "javax.inject:javax.inject:1"
```

#### 特点
- 可以动态切换任意一个 BaseUrl
- 绑定生命周期解决内存泄漏
- 支持缓存
- 自定义拦截器
- 自定义重试
- 统一处理网络请求发生的错误

#### 使用
引入 Module 或 gradle
```
implementation 'com.from.business.http:business_module_http:0.0.9'
implementation 'com.uber.autodispose:autodispose-android:1.1.0'
```
代码可参考[HttpPresenterOrViewModel](https://github.com/xwc520/BusinessComponent/blob/master/app/src/main/java/me/businesscomponent/activity/HttpPresenterOrViewModel.java)
```
    getUserCacheObservable(update)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试
                .doOnSubscribe(disposable -> {
                    //显示 loading View
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    //隐藏 loading View
                })
                // 绑定生命周期
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(checkNotNull(mLifecycle))))
                .subscribe(new ErrorHandleSubscriber<Reply<List<User>>>(mHttp.rxErrorHandler()) {
                    @Override
                    public void onNext(Reply<List<User>> listReply) {
                        Toast.makeText(appComponent.application(), listReply.getData().size() + "", Toast.LENGTH_SHORT).show();
                    }
                });

```

### 感谢依赖库的开发作者们
- [Uber](https://github.com/uber)
- [JessYan](https://github.com/JessYanCoding)
- more




