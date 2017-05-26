package routers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * @author zhukun on 2017/5/26.
 */
public class NavigateCallAdapterFactory extends CallAdapter.Factory {
    static final CallAdapter.Factory INSTANCE = new NavigateCallAdapterFactory();

    @Override
    public CallAdapter<?, ?> get(final Type returnType, Annotation[] annotations, Routers routers) {
        if (getRawType(returnType) != Navigate.class) {
            return null;
        }

        return new CallAdapter<IntentCall, Navigate>() {
            @Override
            public Type responseType() {
                return returnType;
            }

            @Override
            public Navigate adapt(IntentCall call) {
                return new NavigateImpl(call.getServiceMethod(), call.execute());
            }
        };
    }
}
