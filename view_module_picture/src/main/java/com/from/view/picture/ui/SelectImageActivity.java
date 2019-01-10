package com.from.view.picture.ui;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.from.view.picture.R;
import com.from.view.picture.SelectorSpec;
import com.from.view.picture.adapter.FolderAdapter;
import com.from.view.picture.adapter.ImagesAdapter;
import com.from.view.picture.bean.ImageFolder;
import com.from.view.picture.bean.ImageItem;
import com.from.view.picture.bean.SelectImageHelper;
import com.from.view.picture.data.DateFormatUtil;
import com.from.view.picture.data.ImageDataSource;
import com.from.view.picture.weight.ImageGridDecoration;
import com.from.view.picture.weight.SuperCheckBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Doc  选择图片Activity
 *
 * @author ym.li
 * @version 2.9.0
 * @since 2018/11/2/002
 */
public class SelectImageActivity extends AppCompatActivity implements ImagesAdapter.OnItemClickListener,
    ImagesAdapter.OnImageSelectListener, View.OnClickListener, FolderAdapter.OnFolderSelectListener,
    SelectImageHelper.OnImageSelectUpdateListener, ImageDataSource.DataCallback, ImagesAdapter.OnTakePhotoListener {
    /**
     * 返回图片路径
     */
    public static final String RESULT_IMAGES = "result_images";
    private static final int REQUEST_PREVIEW_IMAGES = 0x002f;
    private static final int CAMERA_REQUEST_PERMISSION = 0x004f;
    private static final int REQUEST_CAMERA = 0x003f;
    private static final long FOLDER_ANIM_DURATION = 300;
    private static final long SHOW_OR_HIDE_TIME_ANIM_DURATION = FOLDER_ANIM_DURATION;
    private static final long DELAYED_HIDE_TIME = 1500;
    private static final long MILLISECOND = 1000;
    private List<ImageFolder> mFolderList;
    private RecyclerView mImageRv;
    private RecyclerView mFolderRv;
    private ImagesAdapter mImagesAdapter;
    private FolderAdapter mFolderAdapter;
    private SelectImageHelper mSelectImageItem;
    private View mMaskView;
    private Button mPreviewBtn;
    private Button mSelectFolderBtn;
    private Button mCompleteBtn;
    private SelectorSpec mSelectorSpec;
    private TextView mPicTimeTv;
    private boolean mShowTvTime;
    private boolean mOpenFolderRv;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maven_picture_activity_select_image);
        setStatusBarColor();
        mSelectImageItem = SelectImageHelper.buildInstance();
        mSelectImageItem.setOnImageSelectUpdateListener(this);
        mSelectorSpec = SelectorSpec.getInstance();
        initView();
        initAdapter();
        initData();
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

    private void initView() {
        //init time tv
        mPicTimeTv = findViewById(R.id.tv_time);
        //init imageRv
        mImageRv = findViewById(R.id.rv_image);
        mImageRv.setHasFixedSize(true);
        ((DefaultItemAnimator) mImageRv.getItemAnimator()).setSupportsChangeAnimations(false);
        int spacing = getResources().getDimensionPixelSize(R.dimen.maven_picture_image_grid_spacing);
        //小于等于0时，设置默认值为3;   解决GridLayoutManager throw  Span count should be at least 1. Provided 0
        if (mSelectorSpec.getSpanCount() <= 0) {
            mSelectorSpec.setSpanCount(3);
        }
        mImageRv.addItemDecoration(new ImageGridDecoration(mSelectorSpec.getSpanCount(), spacing, false));
        GridLayoutManager manager = new GridLayoutManager(this, mSelectorSpec.getSpanCount());
        manager.setOrientation(GridLayoutManager.VERTICAL);
        mImageRv.setLayoutManager(manager);
        //init imageRv listener
        initImageRvListener();
        //init maskView
        mMaskView = findViewById(R.id.masking);
        mMaskView.setOnClickListener(this);
        //init FolderRv
        mFolderRv = findViewById(R.id.rv_folder);
        mFolderRv.setHasFixedSize(true);
        ((DefaultItemAnimator) mFolderRv.getItemAnimator()).setSupportsChangeAnimations(false);
        LinearLayoutManager folderManage = new LinearLayoutManager(this);
        folderManage.setOrientation(LinearLayoutManager.VERTICAL);
        mFolderRv.setLayoutManager(folderManage);
        hideFolderRv();
        //init btn
        mPreviewBtn = findViewById(R.id.btn_preview);
        mSelectFolderBtn = findViewById(R.id.btn_image_folder);
        mCompleteBtn = findViewById(R.id.btn_complete);
        //init listener
        mPreviewBtn.setOnClickListener(this);
        mSelectFolderBtn.setOnClickListener(this);
        mCompleteBtn.setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
    }

    private void initImageRvListener() {
        mImageRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                changeTvTime();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                changeTvTime();
            }
        });
    }

    private Runnable mHideTimeRunnable = new Runnable() {
        @Override
        public void run() {
            hideTvTime();
        }
    };

    private void changeTvTime() {
        GridLayoutManager manager = (GridLayoutManager) mImageRv.getLayoutManager();
        int firstVisibleItem = manager.findFirstVisibleItemPosition();
        if (firstVisibleItem >= 0 && firstVisibleItem < mImagesAdapter.getData().size()) {
            ImageItem image = mImagesAdapter.getData().get(firstVisibleItem);
            String time = DateFormatUtil.getImageTime(image.addTime * MILLISECOND);
            mPicTimeTv.setText(time);
            showTvTime();
            mHandler.removeCallbacks(mHideTimeRunnable);
            mHandler.postDelayed(mHideTimeRunnable, DELAYED_HIDE_TIME);
        }
    }

    private void showTvTime() {
        if (mShowTvTime) {
            ObjectAnimator.ofFloat(mPicTimeTv, "alpha", 0, 1).setDuration(SHOW_OR_HIDE_TIME_ANIM_DURATION).start();
            resetShowTime();
        }
    }

    private void hideTvTime() {
        if (!mShowTvTime) {
            ObjectAnimator.ofFloat(mPicTimeTv, "alpha", 1, 0).setDuration(SHOW_OR_HIDE_TIME_ANIM_DURATION).start();
            resetShowTime();
        }
    }

    private void resetShowTime() {
        mShowTvTime = !mShowTvTime;
    }

    private void initAdapter() {
        //init imageAdapter
        mImagesAdapter = new ImagesAdapter(null, mImageRv, mSelectImageItem);
        mImagesAdapter.setOnItemClickListener(this);
        mImagesAdapter.setOnImageSelectListener(this);
        mImagesAdapter.setOnTakePhotoListener(this);
        mImageRv.setAdapter(mImagesAdapter);
        //init folderAdapter
        mFolderAdapter = new FolderAdapter(this, null);
        mFolderAdapter.setFolderSelectListener(this);
        mFolderRv.setAdapter(mFolderAdapter);
    }

    private void initData() {
        ImageDataSource.loadImageForSDCard(this, this);
    }

    private void hideFolderRv() {
        mFolderRv.post(new Runnable() {
            @Override
            public void run() {
                mFolderRv.setTranslationY(mFolderRv.getHeight());
                mFolderRv.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        mSelectImageItem.selectPosition = position;
        mSelectImageItem.folderAllImage = true;
        mSelectImageItem.addAllImageItem(mImagesAdapter.getAllImageItem());
        PreviewImageActivity.start(this, REQUEST_PREVIEW_IMAGES);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != data && resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_PREVIEW_IMAGES) {
                ArrayList<String> paths = data.getStringArrayListExtra(RESULT_IMAGES);
                setResult(paths);
            } else if (requestCode == REQUEST_CAMERA) {
                ArrayList<String> list = new ArrayList<String>();
                String path = data.getStringExtra("result");
                list.add(path);
                setResult(list);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mOpenFolderRv) {
            closeFolderRv();
        } else {
            super.onBackPressed();
        }
    }

    private void closeFolderRv() {
        if (mOpenFolderRv) {
            mMaskView.setVisibility(View.GONE);
            ObjectAnimator animator = ObjectAnimator.ofFloat(mFolderRv, "translationY",
                0, mFolderRv.getHeight()).setDuration(FOLDER_ANIM_DURATION);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mFolderRv.setVisibility(View.GONE);
                }
            });
            animator.start();
            mOpenFolderRv = !mOpenFolderRv;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_PERMISSION && permissions[0].equals(Manifest.permission.CAMERA)) {
            CameraActivity.startForResult(this, REQUEST_CAMERA);
        }
    }

    private void setResult(ArrayList<String> paths) {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(RESULT_IMAGES, paths);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onImageSelect(SuperCheckBox checkBox, int position) {
        updateImageItemSelect(position);
    }

    private void updateImageItemSelect(int position) {
        ImageItem imageItem = mImagesAdapter.getItem(position);
        mSelectImageItem.addImageItem(imageItem);
        mImagesAdapter.notifyItemChanged(position);
        resetButton();
    }

    private void resetButton() {
        int selectImageCount = mSelectImageItem.getSelectCount();
        String previewCount = String.format(getString(R.string.maven_picture_preview_image_button_count), String.valueOf(selectImageCount));
        mPreviewBtn.setText(previewCount);
        mPreviewBtn.setEnabled(selectImageCount > 0);
        if (selectImageCount > 0 && !mSelectorSpec.singleImage()) {
            mCompleteBtn.setEnabled(true);
            mCompleteBtn.setText(getString(R.string.maven_picture_complete_with_select_image_count,
                String.valueOf(selectImageCount), String.valueOf(mSelectorSpec.getMaxSelectImage())));
        } else {
            mCompleteBtn.setEnabled(selectImageCount > 0 && mSelectorSpec.singleImage());
            mCompleteBtn.setText(getString(R.string.maven_picture_complete));
        }
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btn_image_folder) {
            if (mOpenFolderRv) {
                closeFolderRv();
            } else {
                openFolderRv();
            }
        } else if (viewId == R.id.btn_preview) {
            mSelectImageItem.folderAllImage = false;
            mSelectImageItem.resetSelectPosition();
            PreviewImageActivity.start(this, REQUEST_PREVIEW_IMAGES);
        } else if (viewId == R.id.masking) {
            closeFolderRv();
        } else if (viewId == R.id.btn_complete) {
            Intent intent = new Intent();
            ArrayList<String> paths = new ArrayList<>();
            for (ImageItem item : mSelectImageItem.getSelectImageItem()) {
                paths.add(item.path);
            }
            intent.putStringArrayListExtra(RESULT_IMAGES, paths);
            setResult(Activity.RESULT_OK, intent);
            finish();
        } else if (viewId == R.id.iv_back) {
            finish();
        }
    }

    private void openFolderRv() {
        if (!mOpenFolderRv) {
            mMaskView.setVisibility(View.VISIBLE);
            ObjectAnimator animator = ObjectAnimator.ofFloat(mFolderRv, "translationY",
                mFolderRv.getHeight(), 0).setDuration(FOLDER_ANIM_DURATION);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    mFolderRv.setVisibility(View.VISIBLE);
                }
            });
            animator.start();
            mOpenFolderRv = !mOpenFolderRv;
        }
    }

    @Override
    public void OnFolderSelect(int position) {
        //关闭当前目录弹窗
        closeFolderRv();
        //改变选中的目录
        mFolderAdapter.updateSelectItem(position);
        //切换不同目录下的图集
        mImagesAdapter.setNewData(mFolderList.get(position).images);
        //更换图片目录
        ImageFolder folder = mFolderAdapter.getItem(position);
        mSelectFolderBtn.setText(folder.name);
    }

    @Override
    public void notify(int position) {
        updateImageItemSelect(position);
    }

    @Override
    public void onSuccess(final ArrayList<ImageFolder> folders) {
        if (null != folders && !folders.isEmpty()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mFolderList = folders;
                    mImagesAdapter.setNewData(mFolderList.get(0).images);
                    updateFolderRv();
                    resetButton();
                }
            });
        }
    }

    private void updateFolderRv() {
        mFolderAdapter.setNewData(mFolderList);
    }

    @Override
    public void onTakePhoto() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            CameraActivity.startForResult(this, REQUEST_CAMERA);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_PERMISSION);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSelectImageItem.setOnImageSelectUpdateListener(null);
    }
}
