package com.from.view.picture.ui;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.from.view.photoview.OnPhotoTapListener;
import com.from.view.picture.R;
import com.from.view.picture.SelectorSpec;
import com.from.view.picture.adapter.PreviewAdapter;
import com.from.view.picture.bean.ImageItem;
import com.from.view.picture.bean.SelectImageHelper;
import com.from.view.picture.weight.FixViewPager;
import com.from.view.picture.weight.SuperCheckBox;

import java.util.ArrayList;

import static com.from.view.picture.ui.SelectImageActivity.RESULT_IMAGES;

/**
 * 预览图片的Activity
 * <p>
 * author: ym.li
 * since: 2018/11/3
 */
public class PreviewImageActivity extends AppCompatActivity implements OnPhotoTapListener {
    private FixViewPager mPreViewPager;
    private SelectImageHelper mSelectImageHelper;
    private SuperCheckBox mPreViewCheckbox;
    private TextView mSelectImageTitle;
    private TextView mImageSize;
    private boolean mHideTitleBar;
    private Button mSelectCompleteBtn;
    private RelativeLayout mTitleBar;
    private RelativeLayout mBottomBar;
    private SelectorSpec mSelectorSpec;

    public static void start(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, PreviewImageActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maven_picture_activity_preview_image);
        setStatusBarColor();
        initData();
        initView();
        registerListener();
        initAdapter();
    }

    /**
     * 修改状态栏颜色
     */
    private void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.maven_picture_theme_color));
        }
    }

    private void initData() {
        mSelectImageHelper = SelectImageHelper.getInstance();
        mSelectorSpec = SelectorSpec.getInstance();
    }

    private void initView() {
        mPreViewPager = findViewById(R.id.previewVp);
        mPreViewCheckbox = findViewById(R.id.preview_checkbox);
        mSelectImageTitle = findViewById(R.id.image_title);
        mSelectCompleteBtn = findViewById(R.id.btn_complete);
        mTitleBar = findViewById(R.id.preview_title);
        mBottomBar = findViewById(R.id.bottom_bar);
        mImageSize = findViewById(R.id.tv_size);
        resetCheckBox(mSelectImageHelper.selectPosition);
        resetImageTitle(mSelectImageHelper.selectPosition);
        setImageSize(mSelectImageHelper.selectPosition);
        resetCompleteBtn();
    }

    private void registerListener() {
        //选中当前图片按钮
        mPreViewCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<ImageItem> imageItemList = mSelectImageHelper.getAllImageItem();
                //文件大小是否超过最大设置
                boolean overMaxLength = false;
                if (imageItemList != null && !imageItemList.isEmpty()) {
                    ImageItem imageItem = imageItemList.get(mPreViewPager.getCurrentItem());
                    overMaxLength = imageItem.size > mSelectorSpec.getMaxLength();
                }
                if (overMaxLength) {
                    //选择文件大小超出最大设置
                    Toast.makeText(getApplicationContext(),
                            String.format(getString(R.string.maven_picture_max_length),
                                    Formatter.formatShortFileSize(getApplicationContext(),
                                            mSelectorSpec.getMaxLength())),
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (mSelectImageHelper.getSelectCount() >= mSelectorSpec.getMaxSelectImage()
                            && mPreViewCheckbox.isChecked()) {
                        mPreViewCheckbox.setChecked(false);
                        Toast.makeText(PreviewImageActivity.this,
                                getString(R.string.maven_picture_max_select_image,
                                        String.valueOf(mSelectorSpec.getMaxSelectImage())), Toast.LENGTH_LONG).show();
                    } else {
                        mSelectImageHelper.notifyImageItem(mSelectorSpec.isOpenCamera()
                                ? mPreViewPager.getCurrentItem() + 1
                                : mPreViewPager.getCurrentItem());
                        resetCompleteBtn();
                    }
                }
            }
        });
        //viewPager切换事件
        mPreViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setImageSize(position);
                resetCheckBox(position);
                resetImageTitle(position);
            }
        });
        //完成选择图片按钮
        mSelectCompleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                ArrayList<String> paths = new ArrayList<>();
                for (ImageItem item : mSelectImageHelper.getSelectImageItem()) {
                    paths.add(item.path);
                }
                intent.putStringArrayListExtra(RESULT_IMAGES, paths);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initAdapter() {
        PreviewAdapter mPreViewAdapter = new PreviewAdapter(mSelectImageHelper.getAllImageItem(), this);
        mPreViewAdapter.setOnPhotoTapListener(this);
        mPreViewPager.setAdapter(mPreViewAdapter);
        mPreViewPager.setCurrentItem(mSelectImageHelper.selectPosition, false);
    }

    private void resetCheckBox(int position) {
        ArrayList<ImageItem> imageItemList = mSelectImageHelper.getAllImageItem();
        if (imageItemList != null && !imageItemList.isEmpty()) {
            ImageItem imageItem = imageItemList.get(position);
            mPreViewCheckbox.setChecked(mSelectImageHelper.contains(imageItem));
        }
    }

    private void setImageSize(int position) {
        ArrayList<ImageItem> imageItemList = mSelectImageHelper.getAllImageItem();
        if (imageItemList != null && !imageItemList.isEmpty()) {
            ImageItem imageItem = imageItemList.get(position);
            mImageSize.setText(Formatter.formatShortFileSize(this, imageItem.size));
        }
    }

    private void resetImageTitle(int position) {
        String selectCount = String.valueOf(position + 1);
        String maxImageCount = String.valueOf(mSelectImageHelper.getMaxImageCount());
        String imageCount = getString(R.string.maven_picture_preview_image_count, selectCount, maxImageCount);
        mSelectImageTitle.setText(imageCount);
    }

    private void resetCompleteBtn() {
        int selectImageCount = mSelectImageHelper.getSelectCount();
        if (selectImageCount > 0 && !mSelectorSpec.singleImage()) {
            String maxSelectImageCount = String.valueOf(mSelectorSpec.getMaxSelectImage());
            String selectImageWithMaxCount = getString(R.string.maven_picture_complete_with_select_image_count, String.valueOf(selectImageCount), maxSelectImageCount);
            mSelectCompleteBtn.setText(selectImageWithMaxCount);
            mSelectCompleteBtn.setEnabled(true);
        } else {
            mSelectCompleteBtn.setText(R.string.maven_picture_complete);
            mSelectCompleteBtn.setEnabled(selectImageCount > 0 && mSelectorSpec.singleImage());
        }
    }

    @Override
    public void onPhotoTap(ImageView view, float x, float y) {
        showOrHideBar();
    }

    /**
     * 显示头部和尾部栏
     */
    private void showOrHideBar() {
        if (!mHideTitleBar) {
            ObjectAnimator.ofFloat(mTitleBar, "translationY", 0, -mTitleBar.getHeight()).setDuration(300).start();
            ObjectAnimator.ofFloat(mBottomBar, "translationY", 0, mBottomBar.getHeight()).setDuration(300).start();
        } else {
            ObjectAnimator.ofFloat(mTitleBar, "translationY", mTitleBar.getTranslationY(), 0).setDuration(300).start();
            ObjectAnimator.ofFloat(mBottomBar, "translationY", mBottomBar.getTranslationY(), 0).setDuration(300).start();
        }
        mHideTitleBar = !mHideTitleBar;
    }
}
