package pig.dream.routers;

import android.content.Intent;

import routers.Navigate;
import routers.annotations.Input;
import routers.annotations.Notify;

/**
 * @author zhukun on 2017/5/26.
 */
public interface ActivityApi {

    @Notify("pig.dream.routers.SecondActivity")
    Intent toMainActivity(@Input("idKey") int id, @Input("nameKey") String name);

    @Notify(clazz = SecondActivity.class, requestCode = 1)
    Navigate toMainActivity2(@Input int id, @Input String name);
}
