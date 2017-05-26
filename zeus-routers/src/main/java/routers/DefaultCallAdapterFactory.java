package routers;

import android.content.Intent;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * @author zhukun on 2017/5/26.
 */
public class DefaultCallAdapterFactory extends CallAdapter.Factory {
    static final CallAdapter.Factory INSTANCE = new DefaultCallAdapterFactory();

    @Override
    public CallAdapter<?, ?> get(final Type returnType, Annotation[] annotations, Routers routers) {
        if (getRawType(returnType) != Intent.class) {
            return null;
        }

        return new CallAdapter<Call, Object>() {
            @Override
            public Type responseType() {
                return returnType;
            }

            @Override
            public Object adapt(Call call) {
                return call.execute();
            }
        };
    }
}
