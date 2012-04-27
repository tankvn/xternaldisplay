
package com.googamaphone.xternaldisplay;

import java.lang.reflect.Method;

import android.os.IBinder;
import android.util.Log;


public class IExtDispServiceWrapper {
    private static final String TAG = IExtDispServiceWrapper.class.getSimpleName();

    private static final Class<?> CLASS = ReflectUtils
            .getClass("com.motorola.android.extdispservice.IExtDispService");

    private static final Method METHOD_asBinder = ReflectUtils.getMethod(CLASS, "asBinder");
    private static final Method METHOD_isSupported = ReflectUtils.getMethod(CLASS, "isSupported",
            int.class);
    private static final Method METHOD_isDetectionEnabled = ReflectUtils.getMethod(CLASS,
            "isDetectionEnabled", int.class);
    private static final Method METHOD_isConnected = ReflectUtils.getMethod(CLASS, "isConnected",
            int.class);
    private static final Method METHOD_isDisplayEnabled = ReflectUtils.getMethod(CLASS,
            "isDisplayEnabled", int.class);
    private static final Method METHOD_enableDetection = ReflectUtils.getMethod(CLASS,
            "enableDetection", int.class, IBinder.class);
    private static final Method METHOD_disableDetection = ReflectUtils.getMethod(CLASS,
            "disableDetection", int.class);
    private static final Method METHOD_enableDisplay = ReflectUtils.getMethod(CLASS,
            "enableDisplay", int.class, IBinder.class, String.class);
    private static final Method METHOD_disableDisplay = ReflectUtils.getMethod(CLASS,
            "disableDisplay", int.class);
    private static final Method METHOD_enableStandardOverride = ReflectUtils.getMethod(CLASS,
            "enableStandardOverride", int.class, IBinder.class, int.class);
    private static final Method METHOD_disableStandardOverride = ReflectUtils.getMethod(CLASS,
            "disableStandardOverride", int.class);
    private static final Method METHOD_setRouting = ReflectUtils.getMethod(CLASS, "setRouting",
            String.class, String.class);
    private static final Method METHOD_setRoutingEx = ReflectUtils.getMethod(CLASS, "setRoutingEx",
            String.class, String.class, IBinder.class);
    private static final Method METHOD_setGfxAlign = ReflectUtils.getMethod(CLASS, "setGfxAlign",
            int.class, String.class);
    private static final Method METHOD_getResolution = ReflectUtils.getMethod(CLASS,
            "getResolution", int.class);
    private static final Method METHOD_getResolutions = ReflectUtils.getMethod(CLASS,
            "getResolutions", int.class, boolean.class);
    private static final Method METHOD_getRecommendedResolution = ReflectUtils.getMethod(CLASS,
            "getRecommendedResolution", int.class);
    private static final Method METHOD_getEdid = ReflectUtils.getMethod(CLASS, "getEdid",
            int.class);
    private static final Method METHOD_enableSolInFront = ReflectUtils.getMethod(CLASS,
            "enableSolInFront", int.class, IBinder.class);
    private static final Method METHOD_disableSolInFront = ReflectUtils.getMethod(CLASS,
            "disableSolInFront", int.class);
    private static final Method METHOD_getSolInFront = ReflectUtils.getMethod(CLASS,
            "getSolInFront");

    private final Object mReceiver;

    public IExtDispServiceWrapper(Object receiver) {
        mReceiver = receiver;
    }

    public IBinder asBinder() {
        return (IBinder) ReflectUtils.invoke(mReceiver, METHOD_asBinder, null);
    }

    public boolean isSupported(int paramInt) {
        return (Boolean) ReflectUtils.invoke(mReceiver, METHOD_isSupported, false, paramInt);
    }

    public boolean isDetectionEnabled(int paramInt) {
        return (Boolean) ReflectUtils.invoke(mReceiver, METHOD_isDetectionEnabled, false, paramInt);
    }

    public boolean isConnected(int paramInt) {
        return (Boolean) ReflectUtils.invoke(mReceiver, METHOD_isConnected, false, paramInt);
    }

    public boolean isDisplayEnabled(int paramInt) {
        return (Boolean) ReflectUtils.invoke(mReceiver, METHOD_isDisplayEnabled, false, paramInt);
    }

    public boolean enableDetection(int paramInt, IBinder paramIBinder) {
        return (Boolean) ReflectUtils.invoke(mReceiver, METHOD_enableDetection, false, paramInt,
                paramIBinder);
    }

    public void disableDetection(int paramInt) {
        ReflectUtils.invoke(mReceiver, METHOD_disableDetection, null, paramInt);
    }

    public boolean enableDisplay(int paramInt, IBinder paramIBinder, String paramString) {
        return (Boolean) ReflectUtils.invoke(mReceiver, METHOD_enableDisplay, false, paramInt,
                paramIBinder, paramString);
    }

    public void disableDisplay(int paramInt) {
        ReflectUtils.invoke(mReceiver, METHOD_disableDisplay, null, paramInt);
    }

    public boolean enableStandardOverride(int paramInt1, IBinder paramIBinder, int paramInt2) {
        return (Boolean) ReflectUtils.invoke(mReceiver, METHOD_enableStandardOverride, false,
                paramInt1, paramIBinder, paramInt2);
    }

    public void disableStandardOverride(int paramInt) {
        ReflectUtils.invoke(mReceiver, METHOD_disableStandardOverride, null, paramInt);
    }

    public boolean setRouting(String paramString1, String paramString2) {
        return (Boolean) ReflectUtils.invoke(mReceiver, METHOD_setRouting, false, paramString1,
                paramString2);
    }

    public boolean setRoutingEx(String paramString1, String paramString2, IBinder paramIBinder) {
        return (Boolean) ReflectUtils.invoke(mReceiver, METHOD_setRoutingEx, false, paramString1,
                paramString2, paramIBinder);
    }

    public boolean setGfxAlign(int paramInt, String paramString) {
        return (Boolean) ReflectUtils.invoke(mReceiver, METHOD_setGfxAlign, false, paramInt,
                paramString);
    }

    public int[] getResolution(int paramInt) {
        return (int[]) ReflectUtils.invoke(mReceiver, METHOD_getResolution, null, paramInt);
    }

    public int[] getResolutions(int paramInt, boolean paramBoolean) {
        return (int[]) ReflectUtils.invoke(mReceiver, METHOD_getResolutions, null, paramInt,
                paramBoolean);
    }

    public int getRecommendedResolution(int paramInt) {
        return (Integer) ReflectUtils.invoke(mReceiver, METHOD_getRecommendedResolution, -1,
                paramInt);
    }

    public byte[] getEdid(int paramInt) {
        return (byte[]) ReflectUtils.invoke(mReceiver, METHOD_getEdid, null, paramInt);
    }

    public boolean enableSolInFront(int paramInt, IBinder paramIBinder) {
        return (Boolean) ReflectUtils.invoke(mReceiver, METHOD_enableSolInFront, false, paramInt,
                paramIBinder);
    }

    public void disableSolInFront(int paramInt) {
        ReflectUtils.invoke(mReceiver, METHOD_disableSolInFront, null, paramInt);
    }

    public boolean getSolInFront() {
        return (Boolean) ReflectUtils.invoke(mReceiver, METHOD_getSolInFront, false);
    }

    public static class StubWrapper {
        private static final Class<?> STUB_CLASS = ReflectUtils
                .getClass("com.motorola.android.extdispservice.IExtDispService$Stub");

        private static final Method METHOD_asInterface = ReflectUtils.getMethod(STUB_CLASS,
                "asInterface", IBinder.class);

        public static IExtDispServiceWrapper asInterface(IBinder paramIBinder) {
            Object result = ReflectUtils.invoke(null, METHOD_asInterface, null, paramIBinder);

            if (result != null) {
                Log.e(TAG, "Interface class is " + result.toString());
                return new IExtDispServiceWrapper(result);
            } else {
                Log.e(TAG, "Failed to obtain interface!");
                return null;
            }
        }
    }
}
