package routers;

import android.app.Activity;
import android.content.Intent;

/**
 * @author zhukun on 2017/5/26.
 */
public class NavigateImpl implements Navigate {

    private final ServiceMethod serviceMethod;
    private Intent intent;

    public NavigateImpl(ServiceMethod serviceMethod, Intent intent) {
        this.serviceMethod = serviceMethod;
        this.intent = intent;
    }

    @Override
    public void navigation(Object object) {
        if (object instanceof Activity) {
            if (serviceMethod.requestCode < 0) {
                ((Activity) object).startActivity(intent);
            } else {
                ((Activity) object).startActivityForResult(intent, serviceMethod.requestCode);
            }
        }
    }
}
