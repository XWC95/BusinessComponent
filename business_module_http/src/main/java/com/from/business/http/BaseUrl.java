package com.from.business.http;

import android.support.annotation.NonNull;

import okhttp3.HttpUrl;

/**
 * ================================================
 * 针对于 BaseUrl 在 App 启动时不能确定,需要请求服务器接口动态获取的应用场景
 * <p>
 * Created by JessYan on 11/07/2017 14:58
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public interface BaseUrl {
    /**
     * 在调用 Retrofit API 接口之前,使用 Okhttp 或其他方式,请求到正确的 BaseUrl 并通过此方法返回
     *
     * @return
     */
    @NonNull
    HttpUrl url();
}
