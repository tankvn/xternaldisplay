
package com.googamaphone.xternaldisplay;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.googamaphone.xternaldisplay.ExtDispServiceClient.ExtDispServiceListener;
import com.googamaphone.xternaldisplay.ExtDispServiceClient.SimpleExtDispServiceListener;

public class ConnectionService extends Service {
    public static final String ACTION = "com.googamaphone.xternaldisplay.CONNECTION_SERVICE";

    private static final int NOTIFY_STATUS = 1;

    private final ConnectionHandler mHandler;

    private CharSequence mNotifyTitle;
    private PendingIntent mDisableIntent;
    private Notification mNotification;
    private NotificationManager mNotificationManager;
    private ExtDispServiceClient mClient;
    private boolean mConnected;
    private boolean mDisplaying;
    private boolean mStarted;

    public ConnectionService() {
        mHandler = new ConnectionHandler();
    }

    @Override
    public void onCreate() {
        mConnected = false;
        mDisplaying = false;
        mStarted = false;

        mNotifyTitle = getText(R.string.app_name);
        mDisableIntent = PendingIntent.getService(this, 0, new Intent(ACTION), 0);

        mNotification = new Notification(R.drawable.icon, null, System.currentTimeMillis());
        mNotification.flags |= Notification.FLAG_NO_CLEAR;

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mClient = new ExtDispServiceClient(this);
        mClient.setListener(mListener);
        mClient.bind();

        setStatus(R.string.initializing);
        startForeground(NOTIFY_STATUS, mNotification);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        if (mStarted) {
            setStatus(R.string.disabled);
            stopSelf();
        }

        mStarted = true;
    }

    @Override
    public void onDestroy() {
        mClient.unbind();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void setStatus(int resId) {
        final CharSequence text = getText(resId);

        mNotification.setLatestEventInfo(this, mNotifyTitle, text, mDisableIntent);
        mNotificationManager.notify(NOTIFY_STATUS, mNotification);
    }

    private final ExtDispServiceListener mListener = new SimpleExtDispServiceListener() {
        @Override
        public void onInitialize(boolean success) {
            if (success) {
                mHandler.enableDetection();
            }
        }

        @Override
        public void onSupport(boolean supported, String[] supportedResolutions) {
            // Received list of available resolutions.
        }

        @Override
        public void onDetection(boolean enabled) {
            // Detection is enabled/disabled.
        }

        @Override
        public void onConnection(boolean connected, String[] supportedResolutions) {
            if (connected && !mConnected) {
                mConnected = true;
                mHandler.enableDisplayDelayed();
            } else if (!connected && mConnected) {
                mConnected = false;
                mHandler.disableDisplay();
            }
        }

        @Override
        public void onResolution(String resolution) {
            // Received current resolution.
        }

        @Override
        public void onDisplay(boolean displaying, boolean audioActive) {
            if (displaying && !mDisplaying) {
                mDisplaying = true;
            } else if (!displaying && mDisplaying) {
                mDisplaying = false;
            }
        }

        @Override
        public void onExtControl(String command) {
            // Received CEC command.
        }

        @Override
        public void removeStickyBroadcast(String action) {
            final Intent intent = new Intent(action);
            ConnectionService.this.removeStickyBroadcast(intent);
        }
    };

    private class ConnectionHandler extends Handler {
        private static final int ENABLE_DETECTION = 1;
        private static final int ENABLE_DISPLAY = 2;
        private static final int DISABLE_DISPLAY = 3;

        private static final long DISPLAY_DELAY = 500;

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ENABLE_DETECTION:
                    if (mClient.enableDetection()) {
                        setStatus(R.string.awaiting_hdmi);
                    } else {
                        setStatus(R.string.error);
                    }
                    break;
                case ENABLE_DISPLAY:
                    if (mClient.enableDisplay(null)) {
                        setStatus(R.string.displaying);
                    } else {
                        setStatus(R.string.error);
                    }
                    break;
                case DISABLE_DISPLAY:
                    mClient.disableDisplay();
                    setStatus(R.string.awaiting_hdmi);
                    break;
            }
        }

        public void enableDetection() {
            sendEmptyMessage(ENABLE_DETECTION);
        }

        public void enableDisplayDelayed() {
            sendEmptyMessageDelayed(ENABLE_DISPLAY, DISPLAY_DELAY);

            // TODO: Now would be a good time to make sure the display is in
            // landscape mode.
        }

        public void disableDisplay() {
            sendEmptyMessage(DISABLE_DISPLAY);
        }
    }
}
