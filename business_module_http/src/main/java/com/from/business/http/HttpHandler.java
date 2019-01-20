package com.from.business.http;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.from.business.http.module.http.HttpConfigModule;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * ================================================
 * 处理 Http 请求和响应结果的处理类
 * 使用 {@link HttpConfigModule.Builder#globalHttpHandler(HttpHandler)} 方法配置
 * <p>
 * ================================================
 */
public interface HttpHandler {
    /**
     * 这里可以先客户端一步拿到每一次 Http 请求的结果, 可以先解析成 Json, 再做一些操作, 如检测到 token 过期后
     * 重新请求 token, 并重新执行请求
     *
     * @param httpResult 服务器返回的结果 (已被框架自动转换为字符串)
     * @param chain      {@link Interceptor.Chain}
     * @param response   {@link Response}
     * @return {@link Response}
     */
    @NonNull
    Response onHttpResultResponse(@Nullable String httpResult, @NonNull Interceptor.Chain chain, @NonNull Response response);

    /**
     * 这里可以在请求服务器之前拿到 {@link Request}, 做一些操作比如给 {@link Request} 统一添加 token 或者 header 以及参数加密等操作
     *
     * @param chain   {@link Interceptor.Chain}
     * @param request {@link Request}
     * @return {@link Request}
     */
    @NonNull
    Request onHttpRequestBefore(@NonNull Interceptor.Chain chain, @NonNull Request request);

    /**
     * 空实现
     */
    HttpHandler EMPTY = new HttpHandler() {
        @NonNull
        @Override
        public Response onHttpResultResponse(@Nullable String httpResult, @NonNull Interceptor.Chain chain, @NonNull Response response) {
            //不管是否处理, 都必须将 response 返回出去
            return response;
        }

        @NonNull
        @Override
        public Request onHttpRequestBefore(@NonNull Interceptor.Chain chain, @NonNull Request request) {
            //不管是否处理, 都必须将 request 返回出去
            return request;
        }
    };
}
