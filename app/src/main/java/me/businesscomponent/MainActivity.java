package me.businesscomponent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import me.businesscomponent.activity.HttpExampleActivity;
import me.businesscomponent.activity.PicExampleActivity;

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
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPicDemo:
                startActivity(new Intent(this,PicExampleActivity.class));
                break;
            case R.id.btnHttpDemo:
                startActivity(new Intent(this,HttpExampleActivity.class));
                break;
            default:
                break;
        }
    }
}
