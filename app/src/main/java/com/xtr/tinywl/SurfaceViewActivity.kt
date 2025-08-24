package com.xtr.tinywl

import android.os.Build
import android.os.Bundle
import android.view.InputQueue
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

class SurfaceViewActivity : ComponentActivity(), SurfaceHolder.Callback {

    lateinit var bundle: SurfaceViewActivityBundle
    val xdgTopLevel get() = bundle.xdgTopLevel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bundle = SurfaceViewActivityBundle(intent)
        setTitle(xdgTopLevel.title)
        takeSurface()
        takeInput()
        mService.xdgTopLevelActivityFinishCallbackMap.put(xdgTopLevel, ::finish)
    }


    val mService get() = bundle.binder.getService()

    override fun surfaceCreated(holder: SurfaceHolder) {
        mService.onSurfaceCreated(
            xdgTopLevel,
            holder.surface,
        )
    }

    override fun surfaceChanged(
        holder: SurfaceHolder,
        format: Int,
        width: Int,
        height: Int
    ) {
        mService.onSurfaceChanged(
            xdgTopLevel,
            holder.surface,
        )
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        mService.onSurfaceDestroyed(xdgTopLevel)
    }
    private fun takeSurface() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            /*
             * Only usable in Android 15 due to
             * ASurfaceControl_createFromWindow returning NULL
             * for root ANativeWindow: https://issuetracker.google.com/issues/320706287
             */
//            window.takeSurface(this)
            // TODO: request focus for window.takeSurface
            SurfaceView(this).also {
                it.holder.addCallback(this)
                setContentView(it)
                it.requestFocus()
            }
        } else {
            /*
             * On Android 14 and older we use a SurfaceView
             * instead of taking ownership of our window's surface.
             */
            SurfaceView(this).also {
                it.holder.addCallback(this)
                setContentView(it)
                it.requestFocus()
            }
        }
    }
    private fun takeInput() {
        // TODO: Use AInputReceiver APIs for Android >= 15

        // We take the input queue and use in native code for Android 13/14
        window.takeInputQueue(object : InputQueue.Callback {
            override fun onInputQueueCreated(queue: InputQueue) {
                nativeOnInputQueueCreated(queue)
            }

            override fun onInputQueueDestroyed(queue: InputQueue) {
            }
        })
    }

}