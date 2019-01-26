package me.businesscomponent.activity;

import android.arch.lifecycle.Lifecycle;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.from.business.http.retrofiturlmanager.RetrofitUrlManager;
import com.uber.autodispose.AutoDisposeConverter;

import me.businesscomponent.BaseApplication;
import me.businesscomponent.utils.RxLifecycleUtils;
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

        RetrofitUrlManager.getInstance().putDomain(GANK_DOMAIN_NAME, GANK_DOMAIN);

        pm = new HttpPresenterOrViewModel();
        pm.setLifecycle(getLifecycle());

        pm.getUserList(false);
        pm.getGirlList();

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
