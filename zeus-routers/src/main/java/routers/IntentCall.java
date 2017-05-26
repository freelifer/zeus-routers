package routers;

import android.content.Intent;

/**
 * @author zhukun on 2017/5/26.
 */
public class IntentCall implements Call<Intent> {
    private final ServiceMethod serviceMethod;
    private final Object[] args;

    IntentCall(ServiceMethod serviceMethod, Object[] args) {
        this.serviceMethod = serviceMethod;
        this.args = args;
    }

    @Override
    public Intent execute() {
        Intent intent = new Intent();
        intent.setClassName(serviceMethod.packageName, serviceMethod.routePath);
        serviceMethod.fillInput(intent, args);
        return intent;
    }

    public ServiceMethod getServiceMethod() {
        return serviceMethod;
    }
}
