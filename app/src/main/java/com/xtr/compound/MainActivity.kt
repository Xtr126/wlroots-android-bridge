package com.xtr.compound

import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.InputQueue
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.activity.ComponentActivity


class MainActivity : ComponentActivity(), SurfaceHolder.Callback2, InputQueue.Callback {
    companion object {
        init {
            System.loadLibrary("inputqueue")
        }
    }

    external fun nativeOnInputQueueCreated(queue: InputQueue, binder: IBinder)

    private var mCallback: ITinywlCallback? = null

    private val deathRecipient: IBinder.DeathRecipient = IBinder.DeathRecipient {
        mCallback = null
        runOnUiThread { this.finish() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        intent?.getBundleExtra(Tinywl.EXTRA_KEY)
            ?.getBinder(Tinywl.BINDER_KEY)
            ?.let {
                mCallback = ITinywlCallback.Stub.asInterface(it)
                mCallback?.asBinder()?.linkToDeath(deathRecipient, 0).also {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
                        /*
                         * Only usable in Android 15 due to
                         * ASurfaceControl_createFromWindow returning NULL
                         * for root ANativeWindow: https://issuetracker.google.com/issues/320706287
                         */
                        window.takeSurface(this)
                        // Input is handled using AInputReceiver in remote process
                    } else {
                        /*
                         * On Android 14 and older we use a SurfaceView
                         * instead of taking ownership of our window's surface.
                         */
                        val surfaceView = SurfaceView(this)
                        setContentView(surfaceView)
                        surfaceView.holder.addCallback(this)
                        // We take the input queue and use in native code for Android 14
                        window.takeInputQueue(this)
                    }
                }
            }
    }

    override fun onDestroy() {
        mCallback?.asBinder()?.unlinkToDeath(deathRecipient, 0)
        super.onDestroy()
    }

    override fun surfaceRedrawNeeded(holder: SurfaceHolder) {
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM)
            mCallback?.onSurfaceCreated(holder.surface, window.rootSurfaceControl!!.inputTransferToken)
        else mCallback?.onSurfaceCreated(holder.surface, null)
    }

    override fun surfaceChanged(
        holder: SurfaceHolder,
        format: Int,
        width: Int,
        height: Int
    ) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
    }

    override fun onInputQueueCreated(queue: InputQueue) {
        nativeOnInputQueueCreated(queue, mCallback!!.asBinder())
    }

    override fun onInputQueueDestroyed(queue: InputQueue) {
        TODO("Not yet implemented")
    }
}