# Routers
Routers

How To Use?

```
// 页面跳转接口定义
public interface ActivityApi {

    @Notify("pig.dream.routers.SecondActivity")
    Intent toSecondActivity(@Input("idKey") int id, @Input("nameKey") String name);

    @Notify(clazz = SecondActivity.class, requestCode = 1)
    Navigate toSecondActivity2(@Input("idKey") int id, @Input("nameKey") String name);
}
```

```
Routers routers = new Routers.Builder().setPackageName("应用包名").build();
ActivityApi api = routers.create(ActivityApi.class);

//跳转SecondActivity页面
api.toSecondActivity2(1, "a").navigation(this);
```
