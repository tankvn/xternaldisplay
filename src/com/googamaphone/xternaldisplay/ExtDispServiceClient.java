
package com.googamaphone.xternaldisplay;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

public class ExtDispServiceClient {
    private static final String TAG = ExtDispServiceClient.class.getSimpleName();

    private static final String ACTION_SERVICE = "com.motorola.android.extdispservice.IExtDispService";
    private static final String ACTION_CONTROL_SENDSTATUS = "com.motorola.intent.action.EXTDISP_CONTROL_SENDALLSTATUS";
    private static final String ACTION_STATUS_CONNECTION = "com.motorola.intent.action.EXTDISP_STATUS_CONNECTION";
    private static final String ACTION_STATUS_DETECTION = "com.motorola.intent.action.EXTDISP_STATUS_DETECTION";
    private static final String ACTION_STATUS_DISPLAY = "com.motorola.intent.action.EXTDISP_STATUS_DISPLAY";
    private static final String ACTION_STATUS_EXTCONTROL = "com.motorola.intent.action.EXTDISP_STATUS_EXTCONTROL";
    private static final String ACTION_STATUS_GLESCLIENT = "com.motorola.intent.action.EXTDISP_STATUS_GLESCLIENT";
    private static final String ACTION_STATUS_RESOLUTION = "com.motorola.intent.action.EXTDISP_STATUS_RESOLUTION";
    private static final String ACTION_STATUS_SUPPORT = "com.motorola.intent.action.EXTDISP_STATUS_SUPPORT";
    
    private static final String EXTDISP_PUBLIC_STATE = "com.motorola.intent.action.externaldisplaystate";

    private static final String HDMI_STATE = "hdmi";

    private static final int TYPE_HDMI = 1;

    private final Context mContext;
    private final ExtDispServiceConnection mServiceConnection;

    private IExtDispServiceWrapper mExtDispService;
    private ExtDispServiceListener mListener;

    public ExtDispServiceClient(Context context) {
        mContext = context;
        mServiceConnection = new ExtDispServiceConnection();
    }

    public void setListener(ExtDispServiceListener listener) {
        mListener = listener;
    }

    public void bind() {
        final Intent intent = new Intent(ACTION_SERVICE);
        final IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_STATUS_CONNECTION);
        filter.addAction(ACTION_STATUS_DETECTION);
        filter.addAction(ACTION_STATUS_DISPLAY);
        filter.addAction(ACTION_STATUS_EXTCONTROL);
        filter.addAction(ACTION_STATUS_GLESCLIENT);
        filter.addAction(ACTION_STATUS_RESOLUTION);
        filter.addAction(ACTION_STATUS_SUPPORT);
        filter.addAction(EXTDISP_PUBLIC_STATE);

        mContext.registerReceiver(mReceiver, filter);

        if (!mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE)) {
            if (mListener != null) {
                mListener.onInitialize(false);
            }
        }
    }

    public void unbind() {
        mContext.unregisterReceiver(mReceiver);
        mContext.unbindService(mServiceConnection);
    }

    private void onBind(IBinder binder) {
        Log.e(TAG, "Connected to external display service!");

        mExtDispService = IExtDispServiceWrapper.StubWrapper.asInterface(binder);

        if (mListener != null) {
            mListener.onInitialize(true);
        }

        final Intent intent = new Intent(ACTION_CONTROL_SENDSTATUS);

        mContext.sendBroadcast(intent);
    }

    private void onUnbind() {
        Log.e(TAG, "Disconnected from external display service.");

        mExtDispService = null;
    }

    public boolean enableDetection() {
        Log.e(TAG, "Enabling detection...");

        return mExtDispService.enableDetection(TYPE_HDMI, mExtDispService.asBinder());
    }

    public void disableDetection() {
        Log.e(TAG, "Disabling detection...");

        mExtDispService.disableDetection(TYPE_HDMI);
    }

    public boolean enableDisplay(String param) {
        Log.e(TAG, "Enabling display...");

        // TODO: Figure out what the string parameter does.
        return mExtDispService.enableDisplay(TYPE_HDMI, mExtDispService.asBinder(), param);
    }

    public void disableDisplay() {
        Log.e(TAG, "Disabling display...");

        mExtDispService.disableDisplay(TYPE_HDMI);
    }

    public boolean enableSolInFront() {
        return mExtDispService.enableSolInFront(TYPE_HDMI, mExtDispService.asBinder());
    }

    public void disableSolInFront() {
        mExtDispService.disableSolInFront(TYPE_HDMI);
    }

    public boolean enableStandardOverride(int standard) {
        return mExtDispService.enableStandardOverride(TYPE_HDMI, mExtDispService.asBinder(),
                standard);
    }

    public void disableStandardOverride() {
        mExtDispService.disableStandardOverride(TYPE_HDMI);
    }

    public byte[] getEdid() {
        return mExtDispService.getEdid(TYPE_HDMI);
    }

    public int getRecommendedResolution() {
        return mExtDispService.getRecommendedResolution(TYPE_HDMI);
    }

    public int[] getResolution() {
        return mExtDispService.getResolution(TYPE_HDMI);
    }

    public int[] getResolutions(boolean param) {
        // TODO: Figure out what the boolean parameter does.
        return mExtDispService.getResolutions(TYPE_HDMI, param);
    }

    public boolean setGfxAlign(String param) {
        return mExtDispService.setGfxAlign(TYPE_HDMI, param);
    }

    public boolean isConnected() {
        return mExtDispService.isConnected(TYPE_HDMI);
    }

    public boolean isDetectionEnabled() {
        return mExtDispService.isDetectionEnabled(TYPE_HDMI);
    }

    public boolean isDisplayEnabled() {
        return mExtDispService.isDisplayEnabled(TYPE_HDMI);
    }

    public boolean isSupported() {
        return mExtDispService.isSupported(TYPE_HDMI);
    }

    /**
     * Sets up display routing for the hardware video playback overlay.
     * 
     * @param overlay The type of overlay to route. One of:
     *            <ul>
     *            <li>nextvideoplayback</li>
     *            <li>stickyvideoplayback</li>
     *            <li>test_standard</li>
     *            </ul>
     * @param display The display to route to. One of:
     *            <ul>
     *            <li>hdmi</li>
     *            <li>hdmi_vd0</li>
     *            <li>hdmi_vd1</li>
     *            <li>hdmi_vd2</li>
     *            </ul>
     *            Or, if "test_standard" is used as the overlay, one of:
     *            <ul>
     *            <li>HDMI_STANDARD_XXX</li>
     *            </ul>
     * @param param2
     * @return
     */
    public boolean setRouting(String overlay, String display) {
        return mExtDispService.setRouting(overlay, display);
    }

    public boolean setRoutingEx(String overlay, String display) {
        return mExtDispService.setRoutingEx(overlay, display, mExtDispService.asBinder());
    }

    private class ExtDispServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName component, IBinder binder) {
            onBind(binder);
        }

        @Override
        public void onServiceDisconnected(ComponentName component) {
            onUnbind();
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent broadcast) {
            final String action = broadcast.getAction();

            if (ACTION_STATUS_CONNECTION.equals(action)) {
                final boolean hdmiConnected = broadcast.getBooleanExtra(HDMI_STATE, false);
                final String supportedResolutionsStr = broadcast.getStringExtra("hdmi_res_list");

                Log.e(TAG, "Received STATUS_CONNECTION with hdmi=" + hdmiConnected
                        + ", hdmi_res_list=\"" + supportedResolutionsStr + "\"");

                final String[] supportedResolutions = supportedResolutionsStr != null ? supportedResolutionsStr
                        .split(",") : null;

                if (mListener != null) {
                    mListener.onConnection(hdmiConnected, supportedResolutions);
                }
            } else if (ACTION_STATUS_DETECTION.equals(action)) {
                final boolean hdmiDetected = broadcast.getBooleanExtra(HDMI_STATE, false);

                Log.e(TAG, "Received STATUS_DETECTION with hdmi=" + hdmiDetected);

                if (mListener != null) {
                    mListener.onDetection(hdmiDetected);
                }
            } else if (ACTION_STATUS_DISPLAY.equals(action)) {
                final boolean hdmiActive = broadcast.getBooleanExtra(HDMI_STATE, false);
                final boolean audioActive = broadcast.getBooleanExtra("audio", false);

                Log.e(TAG, "Received STATUS_DISPLAY with hdmi=" + hdmiActive + ", audio="
                        + audioActive);

                if (mListener != null) {
                    mListener.onDisplay(hdmiActive, audioActive);
                }
            } else if (ACTION_STATUS_EXTCONTROL.equals(action)) {
                final String command = broadcast.getStringExtra(HDMI_STATE);

                Log.e(TAG, "Received STATUS_EXTCONTROL with hdmi=\"" + command + "\"");

                if (mListener != null) {
                    mListener.onExtControl(command);
                }
            } else if (ACTION_STATUS_GLESCLIENT.equals(action)) {
                final boolean glesClient = broadcast.getBooleanExtra(HDMI_STATE, false);

                Log.e(TAG, "Received STATUS_GLESCLIENT with hdmi=" + glesClient);

                if (mListener != null) {
                    mListener.onGLESClient(glesClient);
                }
            } else if (ACTION_STATUS_RESOLUTION.equals(action)) {
                final String resolution = broadcast.getStringExtra(HDMI_STATE);

                Log.e(TAG, "Received STATUS_RESOLUTION with hdmi=\"" + resolution + "\"");

                if (mListener != null) {
                    mListener.onResolution(resolution);
                }
            } else if (ACTION_STATUS_SUPPORT.equals(action)) {
                final boolean hdmiSupported = broadcast.getBooleanExtra(HDMI_STATE, false);
                final String supportedResolutionsStr = broadcast.getStringExtra("hdmi_res_list");

                Log.e(TAG, "Received STATUS_SUPPORT with hdmi=" + hdmiSupported
                        + ", hdmi_res_list=\"" + supportedResolutionsStr + "\"");

                final String[] supportedResolutions = supportedResolutionsStr != null ? supportedResolutionsStr
                        .split(",") : null;

                if (mListener != null) {
                    mListener.onSupport(hdmiSupported, supportedResolutions);
                }
            } else if (EXTDISP_PUBLIC_STATE.equals(action)) {
                mListener.removeStickyBroadcast(action);
            }
        }
    };

    public interface ExtDispServiceListener {
        public void onInitialize(boolean success);

        public void onDetection(boolean enabled);

        public void onConnection(boolean connected, String[] supportedResolutions);

        public void onDisplay(boolean displaying, boolean audioActive);

        public void onExtControl(String command);

        public void onGLESClient(boolean glesClient);

        public void onResolution(String resolution);

        public void onSupport(boolean supported, String[] supportedResolutions);
        
        public void removeStickyBroadcast(String action);
    }

    public static class SimpleExtDispServiceListener implements ExtDispServiceListener {
        public void onInitialize(boolean success) {
        }

        public void onConnection(boolean hdmiConnected, String[] supportedResolutions) {
        }

        public void onDetection(boolean hdmiDetected) {
        }

        public void onDisplay(boolean hdmiActive, boolean audioActive) {
        }

        public void onExtControl(String command) {
        }

        public void onGLESClient(boolean glesClient) {
        }

        public void onResolution(String resolution) {
        }

        public void onSupport(boolean hdmiSupported, String[] supportedResolutions) {
        }
        
        public void removeStickyBroadcast(String action) {
        }
    }
}
