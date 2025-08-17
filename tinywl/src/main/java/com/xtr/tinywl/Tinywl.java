package com.xtr.tinywl;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.Surface;

import com.termux.termuxam.Am;

public class Tinywl {
    private static final String TAG = "Tinywl";
    public static final String EXTRA_KEY = "bundle";
    public static final String BINDER_KEY = "callback";
    public static final String BINDER_KEY_INPUT = "input";

    private static native int onSurfaceCreated(Surface surface);
    private static native void onSurfaceChanged(Surface surface);
    private static native IBinder nativeGetBinder();

    public static void main(String[] args) {
        try {
            new ProcessBuilder("logcat", "-v", "color", "--pid=" + android.os.Process.myPid()).inheritIO().start();
            System.loadLibrary("tinywl");
            Looper.prepareMainLooper();
            Bundle data = new Bundle();
            data.putBinder(BINDER_KEY_INPUT, nativeGetBinder());

            data.putBinder(BINDER_KEY, new ITinywlCallback.Stub() {
                @Override
                public void onSurfaceCreated(Surface surface) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(
                        () -> Tinywl.onSurfaceCreated(surface)
                    );
                }

                @Override
                public void onSurfaceChanged(Surface surface) {
                    Tinywl.onSurfaceChanged(surface);
                }
            });

            Integer exitCode = new Am(data, EXTRA_KEY).run(new String[]{"start-activity", "-n", "com.xtr.tinywl/.MainActivity", "--activity-clear-task"});
            Looper.loop();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }


}

