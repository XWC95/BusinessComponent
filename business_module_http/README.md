# business_module_http

## 介绍
封装最火的Retrofit ，Rx ，Oktttp 网络请求框架，让开发者轻松面对网络请求

### 特点
- 可以动态切换任意一个 BaseUrl
- 绑定生命周期解决内存泄漏
- 支持缓存
- 自定义拦截器
- 自定义重试
- 统一处理网络请求发生的错误

### 使用
引入 Module 或 gradle
```
implementation 'com.from.business.http:business_module_http:1.0.0'
implementation 'com.uber.autodispose:autodispose-android:1.2.0'

allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```

### 初始化
```
HttpBusiness.init(Application, "host: https://api.github.com");
```
### 例子

- 发起一个网络请求

如果和host不一致，请先调用只需调用一次
```
RetrofitUrlManager.getInstance().putDomain("gank", "http://gank.io");
```
发起请求
```
 HttpComponent httpComponent = HttpBusiness.getHttpComponent();

        IRepositoryManager iRepositoryManager = httpComponent.repositoryManager();

        GankService gankService = iRepositoryManager.obtainRetrofitService(GankService.class);

        Observable<GankBaseResponse<List<GankItemBean>>> observable = gankService
            .getGirlList(10, 1)
            .subscribeOn(Schedulers.io())
            .retryWhen(new RetryWithDelay(3, 2))
            .observeOn(AndroidSchedulers.mainThread());

        observable
            .as(bindLifecycle())
            .subscribe(
                new ErrorHandleSubscriber<GankBaseResponse<List<GankItemBean>>>
                    (httpComponent.rxErrorHandler()) {
                    @Override
                    public void onNext(GankBaseResponse<List<GankItemBean>> response) {
                        // 业务逻辑
                    }
                }
            );
```

使用RxCache缓存加载可参考[HttpPresenterOrViewModel](https://github.com/xwc520/BusinessComponent/blob/master/app/src/main/java/me/businesscomponent/activity/HttpPresenterOrViewModel.java)


### 感谢依赖库的开发作者们
- [Uber](https://github.com/uber)
- [JessYan](https://github.com/JessYanCoding)
- more




