package me.businesscomponent.activity;

import android.arch.lifecycle.Lifecycle;
import android.util.Log;
import android.widget.Toast;

import com.from.business.http.HttpBusiness;
import com.from.business.http.component.HttpComponent;
import com.from.business.http.integration.IRepositoryManager;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
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
import me.businesscomponent.utils.PopUtils;
import me.businesscomponent.view.TipDialog;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;
import timber.log.Timber;

public class HttpPresenterOrViewModel extends BasePresenterOrViewModel {
    private final IRepositoryManager mRepositoryManager;
    private final HttpComponent mHttp;

    public HttpPresenterOrViewModel() {
        mHttp = HttpBusiness.getHttpComponent();
        mRepositoryManager = mHttp.repositoryManager();
    }

    public void stop() {
        Lifecycle.State currentState = mLifecycle.getCurrentState();
        Timber.tag(BaseApplication.TAG).d("stop:" + currentState.name());
    }

    public void getUserList(TipDialog loadingView) {
        getUserCacheObservable()
            .subscribeOn(Schedulers.io())
            //遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
            .retryWhen(new RetryWithDelay(3, 2))
            // 显示 loading
            .doOnSubscribe(disposable -> loadingView.show())
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            // 隐藏 loading
            .doFinally(() -> loadingView.dismiss())
            .as(bindLifecycle())
            .subscribe(new ErrorHandleSubscriber<Reply<List<User>>>(mHttp.rxErrorHandler()) {
                @Override
                public void onNext(Reply<List<User>> listReply) {
                    Timber.tag(BaseApplication.TAG).d(Thread.currentThread().getName()+"2");
                    Toast.makeText(mHttp.application(), listReply.getData().toString(), Toast.LENGTH_SHORT).show();
                }
            });
    }

    private Observable<Reply<List<User>>> getUserCacheObservable() {
        return getCacheService()
            .getUsers(getUserService().getUsers(1, 10),
                new DynamicKey(1),
                // false 不更新，在缓存时间内使用缓存数据
                new EvictDynamicKey(false));
    }

    public void getGirlList() {
        getCacheService()
            .getGirlList(getGankService().getGirlList(10, 1),
                new DynamicKey(1),
                // true 更新，不使用缓存的数据
                new EvictDynamicKey(true))
            .subscribeOn(Schedulers.io())
            .retryWhen(new RetryWithDelay(3, 2))
            .observeOn(AndroidSchedulers.mainThread())
            .as(bindLifecycle())
            .subscribe(new ErrorHandleSubscriber<Reply<GankBaseResponse<List<GankItemBean>>>>(mHttp.rxErrorHandler()) {
                @Override
                public void onNext(Reply<GankBaseResponse<List<GankItemBean>>> response) {
                    Timber.tag(BaseApplication.TAG).d(response.getData().getResults().toString());
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
}
