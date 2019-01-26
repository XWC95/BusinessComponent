package com.from.business.http;

import android.app.Application;
import android.content.Context;

import com.from.business.http.component.HttpComponent;
import com.from.business.http.component.DaggerHttpComponent;
import com.from.business.http.module.http.HttpConfigModule;
import com.from.business.http.retrofiturlmanager.RetrofitUrlManager;
import com.from.business.http.utils.LogUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author Vea
 * @since 2019-01-24
 */
public final class HttpBusiness {
    private volatile static HttpComponent instance = null;
    private volatile static boolean hasInit = false;

    private HttpBusiness() {
    }

    public static void init(Application application, String baseUrl) {
        init(application, getDefHttpConfigModule(application, baseUrl));
    }

    public static void init(Application application, HttpConfigModule config) {
        if (!hasInit) {
            if (instance == null) {
                synchronized (HttpBusiness.class) {
                    if (instance == null) {
                        instance = DaggerHttpComponent
                            .builder()
                            .application(application)
                            .globalConfigModule(config)
                            .build();
                    }
                }
            }
            hasInit = true;
            LogUtils.debugInfo("HttpBusiness init over.");
        }
    }

    public static HttpComponent getHttpComponent() {
        if (!hasInit) {
            throw new RuntimeException("HttpBusiness::Init::Invoke init(context) first!");
        } else {
            return instance;
        }
    }

    /**
     * @return HttpConfigModule
     */
    public static HttpConfigModule getDefHttpConfigModule(Context context, String baseUrl) {
        HttpConfigModule.Builder builder = HttpConfigModule
            .builder()

            //想支持多 BaseUrl, 以及运行时动态切换任意一个 BaseUrl, 请使用 https://github.com/JessYanCoding/RetrofitUrlManager
            //如果 BaseUrl 在 App 启动时不能确定, 需要请求服务器接口动态获取, 请使用以下代码
            //以下方式是 Arms 框架自带的切换 BaseUrl 的方式, 在整个 App 生命周期内只能切换一次, 若需要无限次的切换 BaseUrl, 以及各种复杂的应用场景还是需要使用 RetrofitUrlManager 框架
            //以下代码只是配置, 还要使用 Okhttp (AppComponent 中提供) 请求服务器获取到正确的 BaseUrl 后赋值给 GlobalConfiguration.sDomain
            //切记整个过程必须在第一次调用 Retrofit 接口之前完成, 如果已经调用过 Retrofit 接口, 此种方式将不能切换 BaseUrl
//                .baseurl(new BaseUrl() {
//                    @Override
//                    public HttpUrl url() {
//                        return HttpUrl.parse(sDomain);
//                    }
//                })

            //可根据当前项目的情况以及环境为框架某些部件提供自定义的缓存策略, 具有强大的扩展性
//            .cacheFactory(new Cache.Factory() {
//                @NonNull
//                @Override
//                public Cache build(CacheType type) {
//                    switch (type.getCacheTypeId()) {
//                        case CacheType.EXTRAS_TYPE_ID:
//                            return new IntelligentCache(500);
//                        case CacheType.CACHE_SERVICE_CACHE_TYPE_ID:
//                            return new Cache(type.calculateCacheSize(context));//自定义 Cache
//                        default:
//                            return new LruCache(200);
//                    }
//                }
//            })

            //若觉得框架默认的打印格式并不能满足自己的需求, 可自行扩展自己理想的打印格式 (以下只是简单实现)
//                .formatPrinter(new FormatPrinter() {
//                    @Override
//                    public void printJsonRequest(Request request, String bodyString) {
//                        Timber.i("printJsonRequest:" + bodyString);
//                    }
//
//                    @Override
//                    public void printFileRequest(Request request) {
//                        Timber.i("printFileRequest:" + request.url().toString());
//                    }
//
//                    @Override
//                    public void printJsonResponse(long chainMs, boolean isSuccessful, int code,
//                                                  String headers, MediaType contentType, String bodyString,
//                                                  List<String> segments, String message, String responseUrl) {
//                        Timber.i("printJsonResponse:" + bodyString);
//                    }
//
//                    @Override
//                    public void printFileResponse(long chainMs, boolean isSuccessful, int code, String headers,
//                                                  List<String> segments, String message, String responseUrl) {
//                        Timber.i("printFileResponse:" + responseUrl);
//                    }
//                })
            // 添加拦截器
//            .addInterceptor(new Interceptor() {
//                @Override
//                public Response intercept(Chain chain) throws IOException {
//                    Response originalResponse;
//                    originalResponse = chain.proceed(chain.request());
//                    return originalResponse;
//                }
//            })

            // 指定 其他 缓存路径，是默认 为 context.getCacheDir()
//            .cacheFile(context.getCacheDir())
            //可以自定义一个单例的线程池供全局使用
//            .executorService(Executors.newCachedThreadPool())
            // 打印日志 默认打印所有网络请求信息
//            .printHttpLogLevel(RequestInterceptor.Level.ALL)
            .baseurl(baseUrl)
            //用来处理 RxJava 中发生的所有错误, RxJava 中发生的每个错误都会回调此接口
            //RxJava 必须要使用 ErrorHandleSubscriber (默认实现 Subscriber 的 onError 方法),
            //此监听才生效
            .responseErrorListener(new ResponseErrorListenerImpl())
            .globalHttpHandler(new HttpHandlerImpl(context))
            .gsonConfiguration((context1, gsonBuilder) -> {//这里可以自己自定义配置 Gson 的参数
                gsonBuilder
                    .serializeNulls()//支持序列化值为 null 的参数
                    .enableComplexMapKeySerialization();//支持将序列化 key 为 Object 的 Map, 默认只能序列化 key 为 String 的 Map
            })
            .retrofitConfiguration((context1, retrofitBuilder) -> {//这里可以自己自定义配置 Retrofit 的参数, 甚至您可以替换框架配置好的 OkHttpClient 对象 (但是不建议这样做, 这样做您将损失框架提供的很多功能)
//                    retrofitBuilder.addConverterFactory(FastJsonConverterFactory.create());//比如使用 FastJson 替代 Gson
            }).okhttpConfiguration((context1, okhttpBuilder) -> {//这里可以自己自定义配置 Okhttp 的参数
//                    okhttpBuilder.sslSocketFactory(); //支持 Https, 详情请百度
                okhttpBuilder.writeTimeout(10, TimeUnit.SECONDS);
                //让 Retrofit 同时支持多个 BaseUrl 以及动态改变 BaseUrl, 详细使用方法请查看 https://github.com/JessYanCoding/RetrofitUrlManager
                RetrofitUrlManager.getInstance().with(okhttpBuilder);
            })
            .rxCacheConfiguration((context1, rxCacheBuilder) -> {//这里可以自己自定义配置 RxCache 的参数
                rxCacheBuilder.useExpiredDataIfLoaderNotAvailable(true);
                //想自定义 RxCache 的缓存文件夹或者解析方式, 如改成 FastJson, 请 return rxCacheBuilder.persistence(cacheDirectory, new FastJsonSpeaker());
                //否则请 return null;
                return null;
            });

        return builder.build();
    }
}
