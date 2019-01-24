# view_module_swipeback

### 介绍
无需继承和实现，侧滑只需一步。


#### 使用
1.引入 Module 或 gradle
```
implementation 'com.from.view.swipeback:view_module_swipeback:0.0.9'
```

2.在 Application 初始化
```
        // exclude 剔除不需要侧滑的类
//        List<String> exclude = new ArrayList<>();
//        exclude.add(SelectImageActivity.class.getSimpleName());
//        SwipeOptions options = SwipeOptions.builder().exclude(exclude).build();
        SwipeBackHelper.init(this, null, null);
```
值得注意的是，初始化之后你所使用的依赖库中的 Activity 也会拥有侧滑的能力.
不过你可使用 option 剔除，有需要也可以保留








