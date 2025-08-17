package com.xtr.compound

import android.content.Intent
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

class SurfaceViewActivity : ComponentActivity(), SurfaceHolder.Callback {
    lateinit var bundle: SurfaceViewActivityBundle
    lateinit var surfaceView: SurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bundle = SurfaceViewActivityBundle(intent)
        setTitle(bundle.title)

        enableEdgeToEdge()
        surfaceView = SurfaceView(this)
        surfaceView.holder.addCallback(this)
        setContentView(surfaceView)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        bundle.binder.getService().onSurfaceCreated(
            bundle.appId,
            bundle.title,
            holder.surface,
            surfaceView.width,
            surfaceView.height
        )
    }

    override fun surfaceChanged(
        holder: SurfaceHolder,
        format: Int,
        width: Int,
        height: Int
    ) {
        bundle.binder.getService().onSurfaceChanged(bundle.appId,
            bundle.title,
            holder.surface,
            width,
            height
        )
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        bundle.binder.getService().onSurfaceDestroyed(
            bundle.appId,
            bundle.title
        )
    }



}