package com.xtr.compound.ui.theme

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.xtr.compound.SurfaceService

class SurfaceViewActivity : ComponentActivity(), SurfaceHolder.Callback {
    var appId: String? = null
    lateinit var surfaceView: SurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appId = intent.getStringExtra("APP_ID")
        title = intent.getStringExtra("TITLE")

        if (appId.isNullOrEmpty() || title.isNullOrEmpty()) {
            finish()
            return
        }

        enableEdgeToEdge()
        surfaceView = SurfaceView(this)
        surfaceView.holder.addCallback(this)
        setContentView(surfaceView)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        mService.onSurfaceAvailableForAppId(
            appId!!,
            title.toString(),
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
        mService.onSurfaceChanged(format, width, height)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
    }

    private lateinit var mService: SurfaceService
    private var mBound: Boolean = false

    /** Defines callbacks for service binding, passed to bindService().  */
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance.
            val binder = service as SurfaceService.LocalBinder
            mService = binder.getService()
            mBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }


    override fun onStart() {
        super.onStart()
        // Bind to LocalService.
        Intent(this, SurfaceService::class.java).also { intent ->
            bindService(intent, connection, BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        mBound = false
    }

}
