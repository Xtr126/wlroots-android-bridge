package com.xtr.tinywl;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import com.termux.termuxam.Am;

public class Tinywl {
    private static final String TAG = "Tinywl";
    public static final String EXTRA_KEY = "bundle";
    public static final String BINDER_KEY_TINYWL = "callback";
    public static final String BINDER_KEY_INPUT = "input";

    private static native IBinder nativeGetInputServiceBinder();
    private static native IBinder nativeGetTinywlServiceBinder();

    public static void main(String[] args) {
        try {
            new ProcessBuilder("logcat", "-v", "color", "--pid=" + android.os.Process.myPid()).inheritIO().start();
            System.loadLibrary("tinywl");
            Looper.prepareMainLooper();

            new Handler(Looper.getMainLooper()).post(() -> {
                Bundle data = new Bundle();
                data.putBinder(BINDER_KEY_INPUT, nativeGetInputServiceBinder());
                data.putBinder(BINDER_KEY_TINYWL, nativeGetTinywlServiceBinder());
                Integer exitCode = new Am(data, EXTRA_KEY).run(new String[]{"start-service", "-n", "com.xtr.tinywl/.SurfaceService"});
            });

            Looper.loop();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

}

