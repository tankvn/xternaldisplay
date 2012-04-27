
package com.googamaphone.xternaldisplay;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.util.Log;

public class ReflectUtils {
    private static final String TAG = "ReflectUtils";

    public static Class<?> getClass(String className) {
        Class<?> result = null;

        
        try {
            result = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        if (result == null) {
            Log.e(TAG, "getClass() failed: result is null");
        }

        return result;
    }

    public static Method getMethod(Class<?> targetClass, String name, Class<?>... parameterTypes) {
        if (targetClass == null) {
            Log.e(TAG, "getMethod() failed: target class not defined");
            return null;
        }

        Method result = null;

        try {
            result = targetClass.getMethod(name, parameterTypes);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        
        if (result == null) {
            Log.e(TAG, "getMethod() failed: result is null");
        }

        return result;
    }

    public static Object invoke(Object receiver, Method method, Object defaultResult,
            Object... args) {
        if (method == null) {
            Log.e(TAG, "invoke() failed: method not defined");
            return defaultResult;
        }

        Object result = defaultResult;

        try {
            result = method.invoke(receiver, args);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return result;
    }
}
