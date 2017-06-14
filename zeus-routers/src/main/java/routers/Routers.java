package routers;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.unmodifiableList;
import static routers.Utils.checkNotNull;

/**
 * @author zhukun on 2017/5/26.
 */

public class Routers {

    private final Map<Method, ServiceMethod> serviceMethodCache = new HashMap<>();

    final List<CallAdapter.Factory> adapterFactories;
    final String packageName;

    Routers(String packageName, List<CallAdapter.Factory> adapterFactories) {
        this.packageName = packageName;
        this.adapterFactories = unmodifiableList(adapterFactories);
    }


    @SuppressWarnings("unchecked") // Single-interface proxy creation guarded by parameter safety.
    public <T> T create(final Class<T> service) {
        Utils.validateServiceInterface(service);

        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[]{service},
                new InvocationHandler() {

                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args)
                            throws Throwable {
                        // If the method is a method from Object then defer to normal invocation.
                        if (method.getDeclaringClass() == Object.class) {
                            return method.invoke(this, args);
                        }

                        ServiceMethod serviceMethod = loadServiceMethod(method);
                        IntentCall call = new IntentCall(serviceMethod, args);
                        return serviceMethod.callAdapter.adapt(call);
                    }
                });
    }

    public CallAdapter<?, ?> callAdapter(Type returnType, Annotation[] annotations) {
        checkNotNull(returnType, "returnType == null");
        checkNotNull(annotations, "annotations == null");

        for (int i = 0, count = adapterFactories.size(); i < count; i++) {
            CallAdapter<?, ?> adapter = adapterFactories.get(i).get(returnType, annotations, this);
            if (adapter != null) {
                return adapter;
            }
        }

        StringBuilder builder = new StringBuilder("Could not locate call adapter for ")
                .append(returnType)
                .append(".\n");
        builder.append("  Tried:");
        for (int i = 0, count = adapterFactories.size(); i < count; i++) {
            builder.append("\n   * ").append(adapterFactories.get(i).getClass().getName());
        }
        throw new IllegalArgumentException(builder.toString());
    }

    private ServiceMethod loadServiceMethod(Method method) {
        ServiceMethod result = serviceMethodCache.get(method);
        if (result != null) return result;

        result = new ServiceMethod.Builder(this, method).build();
        serviceMethodCache.put(method, result);
        return result;
    }


    public static final class Builder {
        private String packageName;
        public Builder() {
        }

        public Builder setPackageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        public Routers build() {
            Utils.checkNotNull(packageName, "packageName is null, Please do <Builder.setPackageName()>");
            List<CallAdapter.Factory> adapterFactories = new ArrayList<>();
            adapterFactories.add(DefaultCallAdapterFactory.INSTANCE);
            adapterFactories.add(NavigateCallAdapterFactory.INSTANCE);
            return new Routers(packageName, adapterFactories);
        }
    }

}
