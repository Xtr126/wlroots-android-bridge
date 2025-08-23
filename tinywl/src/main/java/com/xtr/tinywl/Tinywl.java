package com.xtr.tinywl;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import com.termux.termuxam.Am;

public class Tinywl {
    private static final String TAG = "Tinywl";
    public static final String EXTRA_KEY = "bundle";
    public static final String BINDER_KEY_TINYWL_MAIN = "main";
    public static final String BINDER_KEY_TINYWL_SURFACE = "surface";
    public static final String BINDER_KEY_TINYWL_INPUT = "input";

    private static native IBinder nativeGetTinywlInputServiceBinder();
    private static native IBinder nativeGetTinywlSurfaceBinder();
    private static native void nativeRegisterXdgTopLevelCallback();
    private static native void runTinywlLoop();

    public static void main(String[] args) {
        try {
            new ProcessBuilder("logcat", "-v", "color", "--pid=" + android.os.Process.myPid()).inheritIO().start();
            System.loadLibrary("tinywl");
            Looper.prepareMainLooper();

            new Handler(Looper.getMainLooper()).post(() -> {
                Bundle data = new Bundle();
                data.putBinder(BINDER_KEY_TINYWL_INPUT, nativeGetTinywlInputServiceBinder());
                data.putBinder(BINDER_KEY_TINYWL_SURFACE, nativeGetTinywlSurfaceBinder());
                data.putBinder(BINDER_KEY_TINYWL_MAIN, new ITinywlMain.Stub() {
                    @Override
                    public void registerXdgTopLevelCallback() {
                        nativeRegisterXdgTopLevelCallback();
                    }
                });
                Integer exitCode = new Am(data, EXTRA_KEY).run(new String[]{"start-activity", "-n", "com.xtr.tinywl/.MainActivity"});
                runTinywlLoop();
            });

            Looper.loop();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

}

