package com.xtr.tinywl

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.view.InputQueue
import android.view.Surface
import com.xtr.tinywl.Tinywl.BINDER_KEY_INPUT
import com.xtr.tinywl.Tinywl.BINDER_KEY_TINYWL
import com.xtr.tinywl.Tinywl.EXTRA_KEY

external fun nativeInputBinderReceived(binder: IBinder)
external fun nativeOnInputQueueCreated(queue: InputQueue)

class SurfaceService : Service() {
    companion object {
        init {
            System.loadLibrary("inputqueue")
        }
    }

    /**
     * Class used for the client Binder. Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        // Return this instance of SurfaceService so clients can call public methods.
        fun getService(): SurfaceService = this@SurfaceService
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    // Binder given to client activities.
    private val binder = LocalBinder()

    val mXdgTopLevelRemoteCallback = object : ITinywlXdgTopLevel.Stub() {
        override fun addXdgTopLevel(xdgToplevel: XdgTopLevel) {
            assert(xdgToplevel.surface == null)
            var bundle = SurfaceViewActivityBundle(
                binder, xdgToplevel
            )
            val intent = Intent(this@SurfaceService, SurfaceViewActivity::class.java)
            bundle.putTo(intent)

            startActivity(intent)
        }

        override fun removeXdgTopLevel(xdgToplevel: XdgTopLevel) {
            // TODO: Implement
        }

    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {
        intent!!.getBundleExtra(EXTRA_KEY)!!
            .apply {
                nativeInputBinderReceived(getBinder(BINDER_KEY_INPUT)!!)
                mService = ITinywlCallback.Stub.asInterface(getBinder(BINDER_KEY_TINYWL))
            }
        return super.onStartCommand(intent, flags, startId)
    }

    lateinit var mService: ITinywlCallback

    /**
     * Called by activities when a surface is available
     */
    fun onSurfaceCreated(
        xdgTopLevel: XdgTopLevel,
        surface: Surface,
    ) {
        // Now surface is available, call to wlroots and make it render to it
        xdgTopLevel.surface = surface
        mService.onSurfaceCreated(xdgTopLevel)
    }
    
    fun onSurfaceChanged(
        xdgTopLevel: XdgTopLevel,
        surface: Surface,
    ) {
        // Call to wlroots and resize xdg toplevel now
        xdgTopLevel.surface = surface
        mService.onSurfaceChanged(xdgTopLevel)
    }

    fun onSurfaceDestroyed(xdgTopLevel: XdgTopLevel) {
        // Call to wlroots and unmap xdg toplevel now
        mService.onSurfaceDestroyed(xdgTopLevel)
    }

}