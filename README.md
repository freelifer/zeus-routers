# Routers

### 1. How To Use?

> ##### ActivityApi.java

```
// 页面跳转接口定义
public interface ActivityApi {

    // 调用startActivity跳转页面
    @Notify("pig.dream.routers.SecondActivity")
    Intent toSecondActivity(@Input("idKey") int id, @Input String name);

    // 调用startActivityForResult跳转页面
    @Notify(clazz = SecondActivity.class, requestCode = 1)
    Navigate toSecondActivity2(@Input("idKey") int id, @Input String name);
}
```

> ##### MainActivity.java

```
Routers routers = new Routers.Builder().setPackageName("应用包名").build();
ActivityApi api = routers.create(ActivityApi.class);

//跳转SecondActivity页面
api.toSecondActivity2(101, "tom").navigation(this);
```

> ##### SecondActivity.java

```
int id = getIntent().getIntExtra("idKey", -1); \\  使用显式key id = 101
String name = getIntent().getStringExtra("SecondActivity_String"); \\ 使用默认key name = tom
```

### 2. 版本更新说明

> 1.0.1

```
1. 添加Fragment跳转页面接口
2. Input注解支持默认key, 规则(类名_参数类型:SecondActivity_int), 多个相同参数类型请使用显式的传入key
3. 修复应用包名在build创建的时候检查
```

> 1.0.0

```
1. 定义接口实现Activity跳转页面
```