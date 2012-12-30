
package com.googamaphone.xternaldisplay;

import com.googamaphone.utils.WeakReferenceHandler;
import com.googamaphone.xternaldisplay.ExtDispServiceClient.ExtDispServiceListener;
import com.googamaphone.xternaldisplay.ExtDispServiceClient.SimpleExtDispServiceListener;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;

public class ConnectionService extends Service {
    public static final String ACTION = "com.googamaphone.xternaldisplay.CONNECTION_SERVICE";

    private static final int NOTIFY_STATUS = 1;

    private final ConnectionHandler mHandler;

    private CharSequence mNotifyTitle;
    private PendingIntent mDisableIntent;
    private NotificationCompat.Builder mNotificationBuilder;
    private NotificationManager mNotificationManager;
    private ExtDispServiceClient mClient;
    private boolean mConnected;
    private boolean mDisplaying;
    private boolean mStarted;

    public ConnectionService() {
        mHandler = new ConnectionHandler(this);
    }

    @Override
    public void onCreate() {
        mConnected = false;
        mDisplaying = false;
        mStarted = false;

        mNotifyTitle = getText(R.string.app_name);
        mDisableIntent = PendingIntent.getService(this, 0, new Intent(ACTION), 0);

        mNotificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon).setOngoing(true).setWhen(0)
                .setContentIntent(mDisableIntent);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mClient = new ExtDispServiceClient(this);
        mClient.setListener(mListener);
        mClient.bind();

        setStatus(R.string.initializing);
        startForeground(NOTIFY_STATUS, mNotificationBuilder.build());
    }

    @SuppressWarnings("deprecation")
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

        mNotificationBuilder.setContentTitle(mNotifyTitle).setContentText(text);
        mNotificationManager.notify(NOTIFY_STATUS, mNotificationBuilder.build());
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

    private static class ConnectionHandler extends WeakReferenceHandler<ConnectionService> {
        private static final int ENABLE_DETECTION = 1;
        private static final int ENABLE_DISPLAY = 2;
        private static final int DISABLE_DISPLAY = 3;

        private static final long DISPLAY_DELAY = 500;

        public ConnectionHandler(ConnectionService parent) {
            super(parent);
        }

        public void handleMessage(Message msg, ConnectionService parent) {
            switch (msg.what) {
                case ENABLE_DETECTION:
                    if (parent.mClient.enableDetection()) {
                        parent.setStatus(R.string.awaiting_hdmi);
                    } else {
                        parent.setStatus(R.string.error);
                    }
                    break;
                case ENABLE_DISPLAY:
                    if (parent.mClient.enableDisplay(null)) {
                        parent.setStatus(R.string.displaying);
                    } else {
                        parent.setStatus(R.string.error);
                    }
                    break;
                case DISABLE_DISPLAY:
                    parent.mClient.disableDisplay();
                    parent.setStatus(R.string.awaiting_hdmi);
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
