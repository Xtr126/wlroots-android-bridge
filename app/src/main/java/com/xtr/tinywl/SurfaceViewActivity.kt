package com.xtr.tinywl

import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

class SurfaceViewActivity : ComponentActivity(), SurfaceHolder.Callback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
    }

    val bundle: SurfaceViewActivityBundle get() = SurfaceViewActivityBundle(intent)
    val xdgTopLevel = bundle.xdgTopLevel

    override fun onStart() {
        super.onStart()
        setTitle(xdgTopLevel.title)
        SurfaceView(this).also {
            it.holder.addCallback(this)
            setContentView(it)
        }
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
}