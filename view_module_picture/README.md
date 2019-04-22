# view_module_picture

### 介绍
高仿微信的图片选择库

#### 使用
1.引入 Module 或 gradle
```
implementation 'com.from.view.photoview:view_module_photoview:1.0.0'
implementation 'com.from.view.picture:view_module_picture:1.0.1'

```
#### history
- 1.0.1：添加选择图片时大小限制，超出限制toast提示。2.预览大图时增加图片大小显示

代码可参考[PicExampleActivity](https://github.com/xwc520/BusinessComponent/blob/master/app/src/main/java/me/businesscomponent/activity/PicExampleActivity.java)

```
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_IMAGE) {
            List<String> paths = PictureSelector.obtainPathResult(data);
            Toast.makeText(PicExampleActivity.this, paths.get(0), Toast.LENGTH_SHORT).show();
        }
    }

```





