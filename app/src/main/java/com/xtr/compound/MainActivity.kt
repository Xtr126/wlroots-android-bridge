package com.xtr.compound

import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.InputQueue
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.activity.ComponentActivity


class MainActivity : ComponentActivity(), SurfaceHolder.Callback2 {
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
        intent?.getBundleExtra(Tinywl.EXTRA_KEY)?.apply {
            getBinder(Tinywl.BINDER_KEY)
                ?.let { binder ->
                    mCallback = ITinywlCallback.Stub.asInterface(binder)
                    binder.linkToDeath(deathRecipient, 0)
                    takeSurface()
                    takeInput()
                }
        }


    }

    private fun takeSurface() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            /*
             * Only usable in Android 15 due to
             * ASurfaceControl_createFromWindow returning NULL
             * for root ANativeWindow: https://issuetracker.google.com/issues/320706287
             */
            window.takeSurface(this)
        } else {
            /*
             * On Android 14 and older we use a SurfaceView
             * instead of taking ownership of our window's surface.
             */
            val surfaceView = SurfaceView(this)
            setContentView(surfaceView)
            surfaceView.holder.addCallback(this)
        }
    }

    private fun Bundle.takeInput() {
        // TODO: Use AInputReceiver APIs for Android >= 15

        // We take the input queue and use in native code for Android 13/14
        getBinder(Tinywl.BINDER_KEY_INPUT)
            ?.let { binder ->
                window.takeInputQueue(object : InputQueue.Callback {
                    override fun onInputQueueCreated(queue: InputQueue) {
                        nativeOnInputQueueCreated(queue, binder)
                    }

                    override fun onInputQueueDestroyed(queue: InputQueue) {
                    }
                })

            }

    }

    override fun onDestroy() {
        mCallback?.asBinder()?.unlinkToDeath(deathRecipient, 0)
        super.onDestroy()
    }

    override fun surfaceRedrawNeeded(holder: SurfaceHolder) {
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        mCallback?.onSurfaceCreated(holder.surface)
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
}