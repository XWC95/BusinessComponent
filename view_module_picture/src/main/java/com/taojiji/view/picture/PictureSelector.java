package com.taojiji.view.picture;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.taojiji.view.picture.ui.SelectImageActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Doc  图片选择器入口
 *
 * @author ym.li
 * @version 2.9.0
 * @since 2018/11/2/002
 */
public class PictureSelector {
    private WeakReference<Activity> mActivity;
    private WeakReference<Fragment> mFragment;

    private PictureSelector(Activity activity) {
        this(activity, null);
    }

    public PictureSelector(Activity activity, Fragment fragment) {
        this.mActivity = new WeakReference<>(activity);
        this.mFragment = new WeakReference<>(fragment);
    }

    private PictureSelector(Fragment fragment) {
        this(fragment.getActivity(), fragment);
    }

    public static PictureSelector with(Fragment fragment) {
        return new PictureSelector(fragment);
    }

    public static PictureSelector with(Activity activity) {
        return new PictureSelector(activity);
    }

    public static ArrayList<String> obtainPathResult(Intent data) {
        return data.getStringArrayListExtra(SelectImageActivity.RESULT_IMAGES);
    }

    public SelectorSpec selectSpec() {
        return SelectorSpec.getCleanInstance().withPictureSelector(this);
    }

    @Nullable
    Activity getActivity() {
        return mActivity.get();
    }

    @Nullable
    Fragment getFragment() {
        return mFragment != null ? mFragment.get() : null;
    }
}
