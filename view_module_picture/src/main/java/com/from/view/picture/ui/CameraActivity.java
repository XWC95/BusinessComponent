package com.from.view.picture.ui;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.from.view.picture.FileTools;
import com.from.view.picture.R;
import com.from.view.picture.SelectorSpec;
import com.from.view.picture.bean.ImageItem;

import java.io.File;
import java.util.List;

/**
 * @author yuyh.
 * @date 2017/4/18.
 */
public class CameraActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA = 5;
    private static final int IMAGE_CROP_CODE = 1;
    private File mCropImageFile;
    private File mTempPhotoFile;
    private SelectorSpec mSelectorSpec;

    public static void startForResult(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, CameraActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSelectorSpec = SelectorSpec.getInstance();
        camera();
    }

    private void camera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            mTempPhotoFile = new File(FileTools.createRootPath(this) + "/" + System.currentTimeMillis() + ".jpg");
            FileTools.createFile(mTempPhotoFile);
            Uri uri = null;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                uri = Uri.fromFile(mTempPhotoFile);
            } else {
                uri = FileProvider.getUriForFile(this, getApplicationInfo().processName + ".fileprovider", mTempPhotoFile);
            }
            List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri); //Uri.fromFile(tempFile)
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        } else {
            Toast.makeText(this, getResources().getString(R.string.maven_picture_open_camera_failure), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_CROP_CODE && resultCode == RESULT_OK) {
            complete(new ImageItem(mCropImageFile.getPath(), mCropImageFile.getName()));
        } else if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                if (mTempPhotoFile != null) {
                    if (mSelectorSpec.isNeedCrop()) {
                        crop(mTempPhotoFile.getAbsolutePath());
                    } else {
                        complete(new ImageItem(mTempPhotoFile.getPath(), mTempPhotoFile.getName()));
                    }
                }
            } else {
                if (mTempPhotoFile != null && mTempPhotoFile.exists()) {
                    mTempPhotoFile.delete();
                }
                finish();
            }
        } else {
            finish();
        }
    }

    private void complete(ImageItem image) {
        Intent intent = new Intent();
        if (image != null) {
            intent.putExtra("result", image.path);
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    private void crop(String imagePath) {
        mCropImageFile = new File(FileTools.createRootPath(this) + "/" + System.currentTimeMillis() + ".jpg");
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(getImageContentUri(new File(imagePath)), "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", mSelectorSpec.getAspectX());
        intent.putExtra("aspectY", mSelectorSpec.getAspectY());
        intent.putExtra("outputX", mSelectorSpec.getOutputX());
        intent.putExtra("outputY", mSelectorSpec.getOutputY());
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCropImageFile));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, IMAGE_CROP_CODE);
    }

    public Uri getImageContentUri(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = getContentResolver().query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            new String[]{MediaStore.Images.Media._ID},
            MediaStore.Images.Media.DATA + "=? ",
            new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            cursor.close();
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                if (cursor != null) {
                    cursor.close();
                }
                return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }
}
