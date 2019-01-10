package me.businesscomponent;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.from.view.picture.PictureSelector;
import com.from.view.picture.imageloader.GlideImageLoader;

import java.util.List;

/**
 * @author Vea
 * @since 2019-01
 */
public class MainActivity extends AppCompatActivity {
    public static final int SELECT_IMAGE = 0x006F;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.with(MainActivity.this)
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
        }
    }
}
