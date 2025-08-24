package com.xtr.tinywl

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.view.InputQueue
import android.view.Surface

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

    val xdgTopLevelActivityFinishCallbackMap = mutableMapOf<XdgTopLevel, () -> Unit>()

    fun addXdgTopLevel(xdgToplevel: XdgTopLevel) {
        val bundle = SurfaceViewActivityBundle(
            binder, xdgToplevel
        )
        val intent = Intent(this@SurfaceService, SurfaceViewActivity::class.java)
        bundle.putTo(intent)
        intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    fun removeXdgTopLevel(xdgToplevel: XdgTopLevel) {
        // Invoke the callback to finish the activity
        xdgTopLevelActivityFinishCallbackMap[xdgToplevel]?.invoke()
        xdgTopLevelActivityFinishCallbackMap.remove(xdgToplevel)
    }


    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {


        intent?.getBundleExtra(Tinywl.EXTRA_KEY)
            ?.apply {
                nativeInputBinderReceived(getBinder(Tinywl.BINDER_KEY_TINYWL_INPUT)!!)
                mService = ITinywlSurface.Stub.asInterface(getBinder(Tinywl.BINDER_KEY_TINYWL_SURFACE))
                ITinywlMain.Stub
                    .asInterface(getBinder(Tinywl.BINDER_KEY_TINYWL_MAIN))
                    .registerXdgTopLevelCallback()
            } ?:
        intent?.apply {
            if (getStringExtra(TinywlXdgTopLevelCallback.NATIVE_PTR) != null) {
                val xdgTopLevel = XdgTopLevel().apply {
                    appId = getStringExtra(TinywlXdgTopLevelCallback.APP_ID) ?: ""
                    title = getStringExtra(TinywlXdgTopLevelCallback.TITLE) ?: ""
                    nativePtr = getStringExtra(TinywlXdgTopLevelCallback.NATIVE_PTR)
                }
                val action = getStringExtra(TinywlXdgTopLevelCallback.ACTION)

                if (action == TinywlXdgTopLevelCallback.ACTION_ADD)
                    addXdgTopLevel(xdgTopLevel)
                else if (action == TinywlXdgTopLevelCallback.ACTION_REMOVE)
                    removeXdgTopLevel(xdgTopLevel)
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    lateinit var mService: ITinywlSurface

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