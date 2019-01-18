package me.businesscomponent.activity;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.from.business.http.component.AppComponent;
import com.from.business.http.integration.IRepositoryManager;
import com.from.business.http.lifecycle.AndroidLifecycleScopeProvider;
import com.from.business.http.retrofiturlmanager.RetrofitUrlManager;
import com.from.business.http.utils.HttpModuleUtils;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.Reply;
import me.businesscomponent.cache.CacheProviders;
import me.businesscomponent.entity.GankBaseResponse;
import me.businesscomponent.entity.GankItemBean;
import me.businesscomponent.entity.User;
import me.businesscomponent.http.GankService;
import me.businesscomponent.http.UserService;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;
import timber.log.Timber;

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
            .obtainCacheService(CacheProviders.class)
            .getUsers(mRepositoryManager.obtainRetrofitService(UserService.class).getUsers(1, 10), new DynamicKey(1), new EvictDynamicKey(true))
            .subscribeOn(Schedulers.io())
            .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
            .doOnSubscribe(disposable -> {
                //显示 loading View
            }).subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally(() -> {
                //隐藏 loading View
            })
            .as(bindLifecycle())
            .subscribe(new ErrorHandleSubscriber<Reply<List<User>>>(appComponent.rxErrorHandler()) {
                @Override
                public void onNext(Reply<List<User>> listReply) {
                    Toast.makeText(HttpExampleActivity.this, listReply.getData().size() + "", Toast.LENGTH_SHORT).show();
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
            .as(bindLifecycle())
            .subscribe(new ErrorHandleSubscriber<GankBaseResponse<List<GankItemBean>>>(appComponent.rxErrorHandler()) {
                @Override
                public void onNext(GankBaseResponse<List<GankItemBean>> response) {
                    Timber.d("结果：" + response.getResults().size());
                }
            });
    }

    public <T> AutoDisposeConverter<T> bindLifecycle() {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this));
    }
}
