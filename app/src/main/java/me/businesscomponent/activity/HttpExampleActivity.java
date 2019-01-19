package me.businesscomponent.activity;

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
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.Reply;
import me.businesscomponent.BaseApplication;
import me.businesscomponent.cache.CacheProviders;
import me.businesscomponent.entity.GankBaseResponse;
import me.businesscomponent.entity.GankItemBean;
import me.businesscomponent.entity.User;
import me.businesscomponent.http.GankService;
import me.businesscomponent.http.UserService;
import me.businesscomponent.utils.RxLifecycleUtils;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;
import timber.log.Timber;

import static me.businesscomponent.http.GankService.GANK_DOMAIN;
import static me.businesscomponent.http.GankService.GANK_DOMAIN_NAME;

/**
 * @author Vea
 * @since 2019-01-16
 */
public class HttpExampleActivity extends AppCompatActivity {

    private HttpPresenterOrViewModel pm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pm = new HttpPresenterOrViewModel();
        pm.setLifecycle(getLifecycle());

        RetrofitUrlManager.getInstance().putDomain(GANK_DOMAIN_NAME, GANK_DOMAIN);

        AppComponent appComponent = HttpModuleUtils.obtainAppComponentFromContext(this);

        IRepositoryManager mRepositoryManager = appComponent.repositoryManager();

        mRepositoryManager
                .obtainCacheService(CacheProviders.class)
                .getUsers(mRepositoryManager.obtainRetrofitService(UserService.class).getUsers(1, 10), new DynamicKey(1), new EvictDynamicKey(false))
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

        mRepositoryManager
                .obtainCacheService(CacheProviders.class)
                .getGirlList(mRepositoryManager.obtainRetrofitService(GankService.class).getGirlList(10, 1), new DynamicKey(1), new EvictDynamicKey(false))
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))
                .doOnSubscribe(disposable -> {

                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {

                })
                .as(bindLifecycle())
                .subscribe(new ErrorHandleSubscriber<Reply<GankBaseResponse<List<GankItemBean>>>>(appComponent.rxErrorHandler()) {
                    @Override
                    public void onNext(Reply<GankBaseResponse<List<GankItemBean>>> response) {
                        Timber.tag(BaseApplication.TAG).d(response.getData().getResults().size() + "结果");
                    }
                });
    }

    public <T> AutoDisposeConverter<T> bindLifecycle() {
        return RxLifecycleUtils.bindLifecycle(this);
    }


    @Override
    protected void onStop() {
        super.onStop();
        Lifecycle.State currentState = getLifecycle().getCurrentState();
        Timber.tag(BaseApplication.TAG).d("stop:" + currentState.name());
        pm.stop();
    }
}
