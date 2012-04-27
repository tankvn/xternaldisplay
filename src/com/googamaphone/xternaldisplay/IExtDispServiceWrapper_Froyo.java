
package com.googamaphone.xternaldisplay;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

public class IExtDispServiceWrapper_Froyo {
    private static final String TAG = "FroyoExternalDisplayHelper";

    private static final String ACTION_EXTDISP_CONTROL_DETECTION = "com.motorola.intent.action.EXTDISP_CONTROL_DETECTION";
    private static final String ACTION_EXTDISP_CONTROL_DISPLAY = "com.motorola.intent.action.EXTDISP_CONTROL_DISPLAY";
    private static final String ACTION_EXTDISP_CONTROL_GFXALIGN = "com.motorola.intent.action.EXTDISP_CONTROL_GFXALIGN";
    private static final String ACTION_EXTDISP_CONTROL_MUTE = "com.motorola.intent.action.EXTDISP_CONTROL_MUTE";
    private static final String ACTION_EXTDISP_CONTROL_SENDALLSTATUS = "com.motorola.intent.action.EXTDISP_CONTROL_SENDALLSTATUS";
    private static final String ACTION_EXTDISP_SERVICE_IPC = "com.motorola.intent.action.EXTDISP_SERVICE";
    private static final String ACTION_EXTDISP_STATUS_CONNECTION = "com.motorola.intent.action.EXTDISP_STATUS_CONNECTION";
    private static final String ACTION_EXTDISP_STATUS_DISPLAY = "com.motorola.intent.action.EXTDISP_STATUS_DISPLAY";
    private static final String ACTION_EXTDISP_STATUS_EXTCONTROL = "com.motorola.intent.action.EXTDISP_STATUS_EXTCONTROL";
    private static final String ACTION_EXTDISP_STATUS_RESOLUTION = "com.motorola.intent.action.EXTDISP_STATUS_RESOLUTION";

    private static final String EXTRA_HDMI = "hdmi";
    private static final String EXTRA_TVOUT = "tvout";
    private static final String EXTRA_ALIGN = "align";

    private final Context mContext;
    private ExternalDisplayServiceConnection mServiceConnection;
    private ExternalDisplayBroadcastReceiver mReceiver;

    private InitListener mInitListener;
    private ControlListener mControlListener;
    private ConnectionListener mDetectListener;
    private DisplayListener mDetachListener;

    private boolean mDisplayed;
    private boolean mConnection;

    public IExtDispServiceWrapper_Froyo(Context context, InitListener initListener) {
        mContext = context;
        mInitListener = initListener;

        mDisplayed = false;
        mConnection = false;

        initialize();
    }

    public void release() {
        unbindService();
    }

    public void setDetectListener(ConnectionListener detectListener) {
        mDetectListener = detectListener;
    }

    public void setDetachListener(DisplayListener detachListener) {
        mDetachListener = detachListener;
    }

    public void setExtControlListener(ControlListener extControlListener) {
        mControlListener = extControlListener;
    }

    private void initialize() {
        resetAllStatus();
        bindService();
    }

    private void bindService() {
        if (mServiceConnection != null) {
            return;
        }

        mServiceConnection = new ExternalDisplayServiceConnection();

        if (!mContext.bindService(new Intent(ACTION_EXTDISP_SERVICE_IPC), mServiceConnection,
                Context.BIND_AUTO_CREATE)) {
            unbindService();

            if (mInitListener != null) {
                mInitListener.onInit(false);
                mInitListener = null;
            }
        }
    }

    private void unbindService() {
        if (mServiceConnection != null) {
            mContext.unbindService(mServiceConnection);
        }

        mServiceConnection = null;

        return;
    }

    public boolean isServiceConnected() {
        if (mServiceConnection == null) {
            return false;
        }

        return mServiceConnection.isServiceConnected();
    }

    public void registerReceiver() {
        mReceiver = new ExternalDisplayBroadcastReceiver();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_EXTDISP_STATUS_CONNECTION);
        filter.addAction(ACTION_EXTDISP_STATUS_DISPLAY);
        filter.addAction(ACTION_EXTDISP_STATUS_EXTCONTROL);
        filter.addAction(ACTION_EXTDISP_STATUS_RESOLUTION);

        mContext.registerReceiver(mReceiver, filter);
    }

    public void resetAllStatus() {
        mDisplayed = false;
        mConnection = false;
    }

    protected void sendAllStatus() {
        Intent intent = new Intent(ACTION_EXTDISP_CONTROL_SENDALLSTATUS);

        mContext.sendBroadcast(intent);
    }

    protected void setGfxAlign(String alignment) {
        Intent intent = new Intent(ACTION_EXTDISP_CONTROL_GFXALIGN);
        intent.putExtra(EXTRA_ALIGN, alignment);

        mContext.sendBroadcast(intent);
    }

    protected void setMute(boolean muted) {
        Intent intent = new Intent(ACTION_EXTDISP_CONTROL_MUTE);
        intent.putExtra(EXTRA_HDMI, muted);
        intent.putExtra(EXTRA_TVOUT, false);

        mContext.sendBroadcast(intent);
    }

    protected void setDetection(boolean detected) {
        mConnection = detected;

        Intent intent = new Intent(ACTION_EXTDISP_CONTROL_DETECTION);
        intent.putExtra(EXTRA_HDMI, detected);
        intent.putExtra(EXTRA_TVOUT, false);

        mContext.sendBroadcast(intent);
    }

    protected void setDisplay(boolean displayed) {
        mDisplayed = displayed;

        Intent intent = new Intent(ACTION_EXTDISP_CONTROL_DISPLAY);
        intent.putExtra(EXTRA_HDMI, displayed);
        intent.putExtra(EXTRA_TVOUT, false);

        mContext.sendBroadcast(intent);
    }

    private class ExternalDisplayBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(ACTION_EXTDISP_STATUS_CONNECTION)) {
                mConnection = intent.getBooleanExtra(EXTRA_HDMI, false);

                if (mDetectListener != null) {
                    mDetectListener.onStatusChange(mConnection);
                }
            } else if (action.equals(ACTION_EXTDISP_CONTROL_DISPLAY)) {
                mDisplayed = intent.getBooleanExtra(EXTRA_HDMI, false);

                if (mDetachListener != null) {
                    mDetachListener.onStatusChange(mDisplayed);
                }
            } else if (action.equals(ACTION_EXTDISP_STATUS_EXTCONTROL)) {
                String control = intent.getStringExtra(EXTRA_HDMI);

                if (control != null && mControlListener != null) {
                    mControlListener.onControlCode(control);
                }
            } else if (action.equals(ACTION_EXTDISP_STATUS_RESOLUTION)) {
                if (mDisplayed) {
                    String resolution = intent.getStringExtra(EXTRA_HDMI);
                    if (resolution != null && resolution.equals("1280x720")) {
                        setGfxAlign("BOTTOM_CENTER");
                    }
                }
            } else {
                Log.e(TAG, "Received unknown broadcast: " + action);
            }
        }
    }

    public interface InitListener {
        public void onInit(boolean success);
    }

    public interface ControlListener {
        public void onControlCode(String control);
    }

    public interface ConnectionListener {
        public void onStatusChange(boolean isConnected);
    }

    public interface DisplayListener {
        public void onStatusChange(boolean isAttached);
    }

    private class ExternalDisplayServiceConnection implements ServiceConnection {
        // private static final int STATUS_DISCONNECT = 30517;
        private static final int STATUS_CONNECT = 30516;

        private IBinder mBinderService;

        public boolean isServiceConnected() {
            return (mBinderService != null);
        }

        public void sendStatus(int status) {
            if (mBinderService == null) {
                return;
            }

            Parcel parcel1 = Parcel.obtain();
            Parcel parcel2 = Parcel.obtain();

            try {
                mBinderService.transact(status, parcel1, parcel2, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            } finally {
                parcel1.recycle();
                parcel2.recycle();
            }
        }

        @Override
        public void onServiceConnected(ComponentName component, IBinder binder) {
            mBinderService = binder;

            sendStatus(STATUS_CONNECT);

            mContext.sendBroadcast(new Intent(ACTION_EXTDISP_CONTROL_SENDALLSTATUS));

            if (mInitListener != null) {
                registerReceiver();

                mInitListener.onInit(true);
                mInitListener = null;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName component) {
            mBinderService = null;
        }
    }
}
