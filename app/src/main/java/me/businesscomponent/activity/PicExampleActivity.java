package me.businesscomponent.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.from.view.picture.PictureSelector;
import com.from.view.picture.imageloader.GlideImageLoader;
import com.from.view.swipeback.ISwipeBack;

import java.util.List;

import me.businesscomponent.R;

import static me.businesscomponent.MainActivity.SELECT_IMAGE;

/**
 * @author Vea
 * @since 2019-01-16
 */
public class PicExampleActivity extends AppCompatActivity implements ISwipeBack {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.with(PicExampleActivity.this)
                    .selectSpec()
                    .setMaxSelectImage(6)
                    .setOpenCamera(true)
                    .setImageLoader(new GlideImageLoader())
                    .setSpanCount(3)
                    .startForResult(SELECT_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_IMAGE) {
            List<String> paths = PictureSelector.obtainPathResult(data);
            Toast.makeText(PicExampleActivity.this, paths.get(0), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean isEnableGesture() {
        // 通过返回值控制是否可以侧滑
        return false;
    }
}
