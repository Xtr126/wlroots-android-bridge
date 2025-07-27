package com.xtr.compound

import android.os.Bundle
import android.os.IBinder
import android.view.SurfaceHolder
import androidx.activity.ComponentActivity


class MainActivity : ComponentActivity(), SurfaceHolder.Callback2 {
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
                    window.takeSurface(this)
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