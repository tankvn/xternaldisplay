
package com.googamaphone.xternaldisplay;

import com.googamaphone.compat.CompatUtils;

import java.lang.reflect.Method;

import android.os.IBinder;
import android.util.Log;

public class IExtDispServiceWrapper {
    private static final String TAG = IExtDispServiceWrapper.class.getSimpleName();

    private static final Class<?> CLASS = CompatUtils
            .getClass("com.motorola.android.extdispservice.IExtDispService");

    private static final Method METHOD_asBinder = CompatUtils.getMethod(CLASS, "asBinder");
    private static final Method METHOD_isSupported = CompatUtils.getMethod(CLASS, "isSupported",
            int.class);
    private static final Method METHOD_isDetectionEnabled = CompatUtils.getMethod(CLASS,
            "isDetectionEnabled", int.class);
    private static final Method METHOD_isConnected = CompatUtils.getMethod(CLASS, "isConnected",
            int.class);
    private static final Method METHOD_isDisplayEnabled = CompatUtils.getMethod(CLASS,
            "isDisplayEnabled", int.class);
    private static final Method METHOD_enableDetection = CompatUtils.getMethod(CLASS,
            "enableDetection", int.class, IBinder.class);
    private static final Method METHOD_disableDetection = CompatUtils.getMethod(CLASS,
            "disableDetection", int.class);
    private static final Method METHOD_enableDisplay = CompatUtils.getMethod(CLASS,
            "enableDisplay", int.class, IBinder.class, String.class);
    private static final Method METHOD_disableDisplay = CompatUtils.getMethod(CLASS,
            "disableDisplay", int.class);
    private static final Method METHOD_enableStandardOverride = CompatUtils.getMethod(CLASS,
            "enableStandardOverride", int.class, IBinder.class, int.class);
    private static final Method METHOD_disableStandardOverride = CompatUtils.getMethod(CLASS,
            "disableStandardOverride", int.class);
    private static final Method METHOD_setRouting = CompatUtils.getMethod(CLASS, "setRouting",
            String.class, String.class);
    private static final Method METHOD_setRoutingEx = CompatUtils.getMethod(CLASS, "setRoutingEx",
            String.class, String.class, IBinder.class);
    private static final Method METHOD_setGfxAlign = CompatUtils.getMethod(CLASS, "setGfxAlign",
            int.class, String.class);
    private static final Method METHOD_getResolution = CompatUtils.getMethod(CLASS,
            "getResolution", int.class);
    private static final Method METHOD_getResolutions = CompatUtils.getMethod(CLASS,
            "getResolutions", int.class, boolean.class);
    private static final Method METHOD_getRecommendedResolution = CompatUtils.getMethod(CLASS,
            "getRecommendedResolution", int.class);
    private static final Method METHOD_getEdid = CompatUtils.getMethod(CLASS, "getEdid",
            int.class);
    private static final Method METHOD_enableSolInFront = CompatUtils.getMethod(CLASS,
            "enableSolInFront", int.class, IBinder.class);
    private static final Method METHOD_disableSolInFront = CompatUtils.getMethod(CLASS,
            "disableSolInFront", int.class);
    private static final Method METHOD_getSolInFront = CompatUtils.getMethod(CLASS,
            "getSolInFront");

    private final Object mReceiver;

    public IExtDispServiceWrapper(Object receiver) {
        mReceiver = receiver;
    }

    public IBinder asBinder() {
        return (IBinder) CompatUtils.invoke(mReceiver, null, METHOD_asBinder);
    }

    public boolean isSupported(int paramInt) {
        return (Boolean) CompatUtils.invoke(mReceiver, false, METHOD_isSupported, paramInt);
    }

    public boolean isDetectionEnabled(int paramInt) {
        return (Boolean) CompatUtils.invoke(mReceiver, false, METHOD_isDetectionEnabled, paramInt);
    }

    public boolean isConnected(int paramInt) {
        return (Boolean) CompatUtils.invoke(mReceiver, false, METHOD_isConnected, paramInt);
    }

    public boolean isDisplayEnabled(int paramInt) {
        return (Boolean) CompatUtils.invoke(mReceiver, false, METHOD_isDisplayEnabled, paramInt);
    }

    public boolean enableDetection(int paramInt, IBinder paramIBinder) {
        return (Boolean) CompatUtils.invoke(mReceiver, false, METHOD_enableDetection, paramInt,
                paramIBinder);
    }

    public void disableDetection(int paramInt) {
        CompatUtils.invoke(mReceiver, null, METHOD_disableDetection, paramInt);
    }

    public boolean enableDisplay(int paramInt, IBinder paramIBinder, String paramString) {
        return (Boolean) CompatUtils.invoke(mReceiver, false, METHOD_enableDisplay, paramInt,
                paramIBinder, paramString);
    }

    public void disableDisplay(int paramInt) {
        CompatUtils.invoke(mReceiver, null, METHOD_disableDisplay, paramInt);
    }

    public boolean enableStandardOverride(int paramInt1, IBinder paramIBinder, int paramInt2) {
        return (Boolean) CompatUtils.invoke(mReceiver, false, METHOD_enableStandardOverride,
                paramInt1, paramIBinder, paramInt2);
    }

    public void disableStandardOverride(int paramInt) {
        CompatUtils.invoke(mReceiver, null, METHOD_disableStandardOverride, paramInt);
    }

    public boolean setRouting(String paramString1, String paramString2) {
        return (Boolean) CompatUtils.invoke(mReceiver, false, METHOD_setRouting, paramString1,
                paramString2);
    }

    public boolean setRoutingEx(String paramString1, String paramString2, IBinder paramIBinder) {
        return (Boolean) CompatUtils.invoke(mReceiver, false, METHOD_setRoutingEx, paramString1,
                paramString2, paramIBinder);
    }

    public boolean setGfxAlign(int paramInt, String paramString) {
        return (Boolean) CompatUtils.invoke(mReceiver, false, METHOD_setGfxAlign, paramInt,
                paramString);
    }

    public int[] getResolution(int paramInt) {
        return (int[]) CompatUtils.invoke(mReceiver, null, METHOD_getResolution, paramInt);
    }

    public int[] getResolutions(int paramInt, boolean paramBoolean) {
        return (int[]) CompatUtils.invoke(mReceiver, null, METHOD_getResolutions, paramInt,
                paramBoolean);
    }

    public int getRecommendedResolution(int paramInt) {
        return (Integer) CompatUtils.invoke(mReceiver, -1, METHOD_getRecommendedResolution,
                paramInt);
    }

    public byte[] getEdid(int paramInt) {
        return (byte[]) CompatUtils.invoke(mReceiver, null, METHOD_getEdid, paramInt);
    }

    public boolean enableSolInFront(int paramInt, IBinder paramIBinder) {
        return (Boolean) CompatUtils.invoke(mReceiver, false, METHOD_enableSolInFront, paramInt,
                paramIBinder);
    }

    public void disableSolInFront(int paramInt) {
        CompatUtils.invoke(mReceiver, null, METHOD_disableSolInFront, paramInt);
    }

    public boolean getSolInFront() {
        return (Boolean) CompatUtils.invoke(mReceiver, false, METHOD_getSolInFront);
    }

    public static class StubWrapper {
        private static final Class<?> STUB_CLASS = CompatUtils
                .getClass("com.motorola.android.extdispservice.IExtDispService$Stub");

        private static final Method METHOD_asInterface = CompatUtils.getMethod(STUB_CLASS,
                "asInterface", IBinder.class);

        public static IExtDispServiceWrapper asInterface(IBinder paramIBinder) {
            Object result = CompatUtils.invoke(null, null, METHOD_asInterface, paramIBinder);

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
