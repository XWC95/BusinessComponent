package com.from.business.http;

import android.content.Context;
import android.net.ParseException;
import android.widget.Toast;

import com.from.business.http.utils.LogUtils;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import me.jessyan.rxerrorhandler.handler.listener.ResponseErrorListener;
import retrofit2.HttpException;

/**
 * ================================================
 * 展示 {@link ResponseErrorListener} 的用法
 * <p>
 * ================================================
 */
public class ResponseErrorListenerImpl implements ResponseErrorListener {
    @Override
    public void handleResponseError(Context context, Throwable t) {
        LogUtils.debugInfo("Catch-Error", t.getMessage());
        //这里不光只能打印错误, 还可以根据不同的错误做出不同的逻辑处理
        //这里只是对几个常用错误进行简单的处理, 展示这个类的用法, 在实际开发中请您自行对更多错误进行更严谨的处理
        String msg = getString(context,R.string.business_http_unknown_error);
        if (t instanceof UnknownHostException) {
            msg = getString(context,R.string.business_http_no_network);
        } else if (t instanceof SocketTimeoutException) {
            msg = getString(context,R.string.business_http_time_out_error);
        } else if (t instanceof HttpException) {
            HttpException httpException = (HttpException) t;
            msg = convertStatusCode(context,httpException);
        } else if (t instanceof JsonParseException || t instanceof ParseException || t instanceof JSONException || t instanceof JsonIOException) {
            msg = getString(context,R.string.business_http_data_parsing_error);
        }
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    private String convertStatusCode(Context ctx,HttpException httpException) {
        String msg;
        if (httpException.code() == 500) {
            msg = getString(ctx,R.string.business_http_service_error);
        } else if (httpException.code() == 404) {
            msg = getString(ctx,R.string.business_http_address_error);
        } else if (httpException.code() == 403) {
            msg = getString(ctx,R.string.business_http_request_denied);
        } else if (httpException.code() == 307) {
            msg = getString(ctx,R.string.business_http_request_redirect);
        } else {
            msg = httpException.message();
        }
        return msg;
    }

    private String getString(Context ctx, int id) {
        return ctx.getResources().getString(id);
    }
}
