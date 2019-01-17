package me.businesscomponent.example;

import android.arch.lifecycle.Lifecycle;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.from.business.http.component.AppComponent;
import com.from.business.http.integration.IRepositoryManager;
import com.from.business.http.lifecycle.AndroidLifecycleScopeProvider;
import com.from.business.http.retrofiturlmanager.RetrofitUrlManager;
import com.from.business.http.utils.HttpModuleUtils;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.businesscomponent.entity.GankBaseResponse;
import me.businesscomponent.entity.GankItemBean;
import me.businesscomponent.entity.User;
import me.businesscomponent.http.GankService;
import me.businesscomponent.http.UserService;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static com.uber.autodispose.AutoDispose.autoDisposable;
import static me.businesscomponent.http.GankService.GANK_DOMAIN;
import static me.businesscomponent.http.GankService.GANK_DOMAIN_NAME;

/**
 * @author Vea
 * @since 2019-01-16
 */
public class HttpExampleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RetrofitUrlManager.getInstance().putDomain(GANK_DOMAIN_NAME, GANK_DOMAIN);

        AppComponent appComponent = HttpModuleUtils.obtainAppComponentFromContext(this);

        IRepositoryManager mRepositoryManager = appComponent.repositoryManager();

        mRepositoryManager
            .obtainRetrofitService(UserService.class)
            .getUserseRROR(1, 10)
            .subscribeOn(Schedulers.io())
            .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
            .doOnSubscribe(disposable -> {
                //显示 loading View
            }).subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally(() -> {
                //隐藏 loading View
            })
            .as(autoDisposable(AndroidLifecycleScopeProvider.from(this)))
            .subscribe(new ErrorHandleSubscriber<List<User>>(appComponent.rxErrorHandler()) {
                @Override
                public void onNext(List<User> users) {
                    Toast.makeText(HttpExampleActivity.this, users.size() + "", Toast.LENGTH_SHORT).show();
                }
            });
        Lifecycle lifecycle = getLifecycle();
        mRepositoryManager
            .obtainRetrofitService(GankService.class)
            .getGirlList(10, 1)
            .subscribeOn(Schedulers.io())
            .retryWhen(new RetryWithDelay(3, 2))
            .doOnSubscribe(disposable -> {

            }).subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally(() -> {

            })
            .as(autoDisposable(AndroidLifecycleScopeProvider.from(lifecycle)))
            .subscribe(new ErrorHandleSubscriber<GankBaseResponse<List<GankItemBean>>>(appComponent.rxErrorHandler()) {
                @Override
                public void onNext(GankBaseResponse<List<GankItemBean>> response) {
                    Toast.makeText(HttpExampleActivity.this, response.getResults().size() + "", Toast.LENGTH_SHORT).show();
                }
            });
    }
}
