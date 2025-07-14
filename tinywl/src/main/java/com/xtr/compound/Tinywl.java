package com.xtr.compound;

import android.os.Bundle;
import android.os.Looper;
import android.view.Surface;

import com.termux.termuxam.Am;

public class Tinywl {
    private static final String TAG = "Tinywl";

    private static native int onSurfaceCreated(Surface surface);

    public static void main(String[] args) {
        try {
            new ProcessBuilder("logcat", "-v", "color", "--pid=" + android.os.Process.myPid()).inheritIO().start();
            System.loadLibrary("tinywl");
            Looper.prepare();
            // 1. Create your Parcelable object (example: Bundle)
            Bundle data = new Bundle();
            data.putBinder("callback", new ITinywlCallback.Stub() {
                @Override
                public void onSurfaceCreated(Surface surface) {
                    Tinywl.onSurfaceCreated(surface);
                }
            });

            Integer exitCode = new Am(data, "bundle").run(new String[]{"start-activity", "-n", "com.xtr.compound/.MainActivity", "--activity-clear-task"});
            Looper.loop();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }


}

