package routers;

import android.content.Intent;
import android.text.TextUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import routers.annotations.Input;
import routers.annotations.Notify;

/**
 * @author zhukun on 2017/5/26.
 */

final class ServiceMethod {

    final CallAdapter callAdapter;
    final String routePath;
    final int requestCode;
    final String packageName;
    private final ParameterHandler[] parameterHandlers;

    private ServiceMethod(Builder builder) {
        this.routePath = builder.routePath;
        this.callAdapter = builder.callAdapter;
        this.packageName = builder.routers.packageName;
        this.parameterHandlers = builder.parameterHandlers;
        this.requestCode = builder.requestCode;
    }

    void fillInput(Intent intent, Object... args) {
        int argumentCount = args != null ? args.length : 0;
        if (argumentCount != parameterHandlers.length) {
            throw new IllegalArgumentException("Argument count (" + argumentCount
                    + ") doesn't match expected count (" + parameterHandlers.length + ")");
        }

        for (int p = 0; p < argumentCount; p++) {
            parameterHandlers[p].apply(intent, args[p]);
        }
    }

    static final class Builder {
        final Routers routers;
        final Method method;
        final Annotation[] methodAnnotations;
        final Annotation[][] parameterAnnotationsArray;
        final Type[] parameterTypes;

        String routePath;
        int requestCode;
        CallAdapter callAdapter;
        ParameterHandler[] parameterHandlers;

        Builder(Routers routers, Method method) {
            this.routers = routers;
            this.method = method;
            this.methodAnnotations = method.getAnnotations();
            this.parameterTypes = method.getGenericParameterTypes();
            this.parameterAnnotationsArray = method.getParameterAnnotations();
        }

        ServiceMethod build() {
            callAdapter = createCallAdapter();
            for (Annotation annotation : methodAnnotations) {
                parseMethodAnnotation(annotation);
            }


            int parameterCount = parameterAnnotationsArray.length;
            parameterHandlers = new ParameterHandler[parameterCount];
            for (int p = 0; p < parameterCount; p++) {
                Type parameterType = parameterTypes[p];
                if (Utils.hasUnresolvableType(parameterType)) {
                    throw parameterError(p, "Parameter type must not include a type variable or wildcard: %s",
                            parameterType);
                }

                Annotation[] parameterAnnotations = parameterAnnotationsArray[p];
                if (parameterAnnotations == null) {
                    throw parameterError(p, "No Routers annotation found.");
                }

                parameterHandlers[p] = parseParameter(p, parameterType, parameterAnnotations);
            }

            return new ServiceMethod(this);
        }


        private ParameterHandler parseParameter(
                int p, Type parameterType, Annotation[] annotations) {
            ParameterHandler result = null;
            for (Annotation annotation : annotations) {
                ParameterHandler annotationAction = parseParameterAnnotation(
                        p, parameterType, annotations, annotation);

                if (annotationAction == null) {
                    continue;
                }

                if (result != null) {
                    throw parameterError(p, "Multiple Routers parameter annotations found, only one allowed.");
                }

                result = annotationAction;
            }

            if (result == null) {
                throw parameterError(p, "No Routers parameter annotation found.");
            }

            return result;
        }

        private ParameterHandler parseParameterAnnotation(
                int p, Type type, Annotation[] annotations, Annotation annotation) {
            if (annotation instanceof Input) {
                String value = ((Input) annotation).value();
                if (TextUtils.isEmpty(value)) {
                    value = Utils.getClassSimpleName(routePath) + "_" + Utils.getClassSimpleName(type.toString());
                }
                return new ParameterHandler(type, value);
            }

            return null; // Not a Routers annotation.
        }


        private CallAdapter createCallAdapter() {
            Type returnType = method.getGenericReturnType();
            if (Utils.hasUnresolvableType(returnType)) {
                throw methodError(
                        "Method return type must not include a type variable or wildcard: %s", returnType);
            }
//            if (returnType == void.class) {
//                throw methodError("Service methods cannot return void.");
//            }
            try {
                //noinspection unchecked
                return (CallAdapter) routers.callAdapter(returnType, methodAnnotations);
            } catch (RuntimeException e) { // Wide exception range because factories are user code.
                throw methodError(e, "Unable to create call adapter for %s", returnType);
            }
        }

        private void parseMethodAnnotation(Annotation annotation) {
            if (annotation instanceof Notify) {
                parseNotifyAndPath(((Notify) annotation));
            }
        }


        private void parseNotifyAndPath(Notify notify) {
            routePath = notify.value();
            if (TextUtils.isEmpty(routePath)) {
                routePath = notify.clazz().getName();
            }
            if (TextUtils.isEmpty(routePath)) {
                throw methodError("Parameter route Path is null");
            }
            requestCode = notify.requestCode();
        }


        private RuntimeException methodError(String message, Object... args) {
            return methodError(null, message, args);
        }

        private RuntimeException methodError(Throwable cause, String message, Object... args) {
            message = String.format(message, args);
            return new IllegalArgumentException(message
                    + "\n    for method "
                    + method.getDeclaringClass().getSimpleName()
                    + "."
                    + method.getName(), cause);
        }

        private RuntimeException parameterError(
                Throwable cause, int p, String message, Object... args) {
            return methodError(cause, message + " (parameter #" + (p + 1) + ")", args);
        }

        private RuntimeException parameterError(int p, String message, Object... args) {
            return methodError(message + " (parameter #" + (p + 1) + ")", args);
        }
    }
}
