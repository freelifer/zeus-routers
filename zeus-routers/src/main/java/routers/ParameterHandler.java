package routers;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * @author zhukun on 2017/5/26.
 * @version 1.0
 */
public class ParameterHandler {

    private String key;
    private Type type;

    public ParameterHandler(Type type, String key) {
        this.type = type;
        this.key = key;
    }

    public void apply(Intent intent, Object object) {
        if (object instanceof Integer) {
            intent.putExtra(key, (int) object);
        } else if (object instanceof String) {
            intent.putExtra(key, (String) object);
        } else if (object instanceof Boolean) {
            intent.putExtra(key, (boolean) object);
        } else if (object instanceof Serializable) {
            intent.putExtra(key, (Serializable) object);
        } else if (object instanceof Bundle) {
            intent.putExtra(key, (Bundle) object);
        } else if (object instanceof Parcelable) {
            intent.putExtra(key, (Parcelable) object);
        } else {
            throw new IllegalArgumentException("Not Support Parameter type. " + type);
        }
    }

}
