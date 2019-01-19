package me.businesscomponent.http;


import java.util.List;

import io.reactivex.Observable;
import me.businesscomponent.entity.GankBaseResponse;
import me.businesscomponent.entity.GankItemBean;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

import static com.from.business.http.retrofiturlmanager.RetrofitUrlManager.DOMAIN_NAME_HEADER;

public interface GankService {
    String GANK_DOMAIN_NAME = "gank";
    String GANK_DOMAIN = "http://gank.io";

    /**
     * 妹纸列表
     */
    @Headers({DOMAIN_NAME_HEADER + GANK_DOMAIN_NAME})
    @GET("/api/data/福利/{num}/{page}")
    Observable<GankBaseResponse<List<GankItemBean>>> getGirlList(@Path("num") int num, @Path("page") int page);
}
