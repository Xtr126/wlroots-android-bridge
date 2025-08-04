package com.xtr.compound;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Surface;
import android.window.InputTransferToken;

import com.termux.termuxam.Am;

public class Tinywl {
    private static final String TAG = "Tinywl";
    public static final String EXTRA_KEY = "bundle";
    public static final String BINDER_KEY = "callback";

    private static native int onSurfaceCreated(Surface surface, InputTransferToken inputTransferToken);

    public static void main(String[] args) {
        try {
            new ProcessBuilder("logcat", "-v", "color", "--pid=" + android.os.Process.myPid()).inheritIO().start();
            System.loadLibrary("tinywl");
            Looper.prepareMainLooper();
            // 1. Create your Parcelable object (example: Bundle)
            Bundle data = new Bundle();
            data.putBinder(BINDER_KEY, new ITinywlCallback.Stub() {
                @Override
                public void onSurfaceCreated(Surface surface, InputTransferToken inputTransferToken) {
                    new Handler(Looper.getMainLooper()).post(() -> Tinywl.onSurfaceCreated(surface, inputTransferToken));
                }
            });

            Integer exitCode = new Am(data, EXTRA_KEY).run(new String[]{"start-activity", "-n", "com.xtr.compound/.MainActivity", "--activity-clear-task"});
            Looper.loop();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }


}

