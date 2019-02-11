# view_module_swipeback

### 介绍
无需继承和实现，侧滑只需一步，0接入成本，已适配9P 。


#### 使用
1.引入 Module 或 gradle
```
  implementation 'com.from.view.swipeback:view_module_swipeback:1.0.0'
```

2.在 Application 初始化
```
  SwipeBackHelper.init(this);
```
值得注意的是，初始化之后你所使用的依赖库中的 Activity 也会拥有侧滑的能力.
不过你可使用 option 剔除，有需要也可以保留








