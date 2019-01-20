package me.businesscomponent.activity;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.widget.Toast;

import com.from.business.http.component.AppComponent;
import com.from.business.http.integration.IRepositoryManager;
import com.from.business.http.utils.HttpModuleUtils;

import java.util.List;

import io.reactivex.Observable;
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
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;
import timber.log.Timber;


public class HttpPresenterOrViewModel extends BasePresenterOrViewModel {


    private final IRepositoryManager mRepositoryManager;
    private final AppComponent appComponent;

    public HttpPresenterOrViewModel(Application application) {
        appComponent = HttpModuleUtils.obtainAppComponentFromContext(application);
        mRepositoryManager = appComponent.repositoryManager();
    }

    public void stop() {
        Lifecycle.State currentState = mLifecycle.getCurrentState();
        Timber.tag(BaseApplication.TAG).d("stop:" + currentState.name());
    }


    public void getUserList(boolean update) {
        getUserCacheObservable(update)
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
                        Toast.makeText(appComponent.application(), listReply.getData().size() + "", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    public void getGirlList() {
       getCacheService()
                .getGirlList(getGankService().getGirlList(10, 1),
                        new DynamicKey(1),
                        new EvictDynamicKey(false))
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

    private CacheProviders getCacheService() {
        return mRepositoryManager.obtainCacheService(CacheProviders.class);
    }

    private UserService getUserService() {
        return mRepositoryManager.obtainRetrofitService(UserService.class);
    }
    private GankService getGankService() {
        return mRepositoryManager.obtainRetrofitService(GankService.class);
    }

    private Observable<Reply<List<User>>> getUserCacheObservable(boolean update) {
        return getCacheService()
                .getUsers(getUserService().getUsers(1, 10),
                        new DynamicKey(1),
                        new EvictDynamicKey(update));
    }
}
