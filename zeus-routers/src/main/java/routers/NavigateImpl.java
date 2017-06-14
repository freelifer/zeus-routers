package routers;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * 实际Activity Fragment跳转接口
 *
 * @author zhukun on 2017/5/26.
 * @version 1.0
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
        } else if (object instanceof Fragment) {
            if (serviceMethod.requestCode < 0) {
                ((Fragment) object).startActivity(intent);
            } else {
                ((Fragment) object).startActivityForResult(intent, serviceMethod.requestCode);
            }
        } else if (object instanceof android.app.Fragment) {
            if (serviceMethod.requestCode < 0) {
                ((android.app.Fragment) object).startActivity(intent);
            } else {
                ((android.app.Fragment) object).startActivityForResult(intent, serviceMethod.requestCode);
            }
        } else {
            throw new IllegalArgumentException("navigation Object must type Activity or Fragment");
        }
    }
}
