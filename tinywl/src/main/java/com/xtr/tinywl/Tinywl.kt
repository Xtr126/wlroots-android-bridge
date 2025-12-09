package com.xtr.tinywl

import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Process
import android.os.RemoteException
import com.termux.termuxam.Am

object Tinywl {
    const val EXTRA_KEY: String = "bundle"
    const val BINDER_KEY_TINYWL_MAIN: String = "main"
    const val BINDER_KEY_TINYWL_SURFACE: String = "surface"
    const val BINDER_KEY_TINYWL_INPUT: String = "input"

    private external fun nativeGetTinywlInputServiceBinder(): IBinder?
    private external fun nativeGetTinywlSurfaceBinder(): IBinder?
    private external fun nativeRegisterXdgTopLevelCallback(binder: IBinder?)
    private external fun runTinywlLoop(args: Array<String>)

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            ProcessBuilder("logcat", "-v", "color", "--pid=" + Process.myPid()).inheritIO().start()
            System.loadLibrary("labwc")
            Looper.prepareMainLooper()

            Handler(Looper.getMainLooper()).post {
                val data = Bundle()
                data.putBinder(BINDER_KEY_TINYWL_INPUT, nativeGetTinywlInputServiceBinder())
                data.putBinder(BINDER_KEY_TINYWL_SURFACE, nativeGetTinywlSurfaceBinder())
                data.putBinder(BINDER_KEY_TINYWL_MAIN, object : ITinywlMain.Stub() {
                    @Throws(RemoteException::class)
                    override fun registerXdgTopLevelCallback(xdgTopLevelCallback: TinywlXdgTopLevelCallback) {
                        nativeRegisterXdgTopLevelCallback(xdgTopLevelCallback.asBinder())
                    }
                })
                val exitCode = Am(data, EXTRA_KEY).run(
                    arrayOf<String>(
                        "start-activity",
                        "-n",
                        "com.xtr.tinywl/.MainActivity"
                    )
                )
                runTinywlLoop(arrayOf("labwc").plus(args))
            }

            Looper.loop()
        } catch (e: Exception) {
            e.printStackTrace(System.out)
        }
    }
}

