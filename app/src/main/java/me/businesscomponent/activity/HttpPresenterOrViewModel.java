package me.businesscomponent.activity;

import android.arch.lifecycle.Lifecycle;

import me.businesscomponent.BaseApplication;
import timber.log.Timber;


public class HttpPresenterOrViewModel extends BasePresenterOrViewModel {


    public void stop() {
        Lifecycle.State currentState = mLifecycle.getCurrentState();
        Timber.tag(BaseApplication.TAG).d("stop:" + currentState.name());
    }
}
