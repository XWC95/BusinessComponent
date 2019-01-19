package me.businesscomponent.activity;

import android.arch.lifecycle.Lifecycle;

import com.from.business.http.lifecycle.AndroidLifecycleScopeProvider;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;

import me.businesscomponent.BaseApplication;
import timber.log.Timber;

import static com.from.business.http.utils.Preconditions.checkNotNull;

public class BasePresenterOrViewModel {

    protected Lifecycle mLifecycle;

    public void setLifecycle(Lifecycle mLifecycle) {
        this.mLifecycle = mLifecycle;
        Lifecycle.State currentState = mLifecycle.getCurrentState();
        Timber.tag(BaseApplication.TAG).d("setLifecycle:" + currentState.name());
    }

    public <T> AutoDisposeConverter<T> bindLifecycle() {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(checkNotNull(mLifecycle)));
    }
}
