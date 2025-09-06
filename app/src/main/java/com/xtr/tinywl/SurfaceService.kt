package com.xtr.tinywl

import android.app.ActivityOptions
import android.app.Service
import android.content.Intent
import android.graphics.Rect
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


    private val mXdgTopLevelCallback = object : TinywlXdgTopLevelCallback.Stub() {
        var captionBarHeight: Int = 0

        override fun addXdgTopLevel(
            appId: String?,
            title: String?,
            nativePtr: Long,
            geoBox: WlrBox?
        ) {
            val xdgToplevel = XdgTopLevel().apply {
                this.appId = appId
                this.title = title
                this.nativePtr = nativePtr
            }
            val bundle = SurfaceViewActivityBundle(
                binder, xdgToplevel
            )
            val intent = Intent(this@SurfaceService, SurfaceViewActivity::class.java)
            bundle.putTo(intent)
            intent
            intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)

            startActivity(intent, ActivityOptions.makeBasic().apply {
                setLaunchBounds(Rect().apply {
                    left = geoBox!!.x                     // left = same as x
                    top = geoBox.y - captionBarHeight                    // top  = same as y
                    right = geoBox.x + geoBox.width - 1     // right: exclusive-end minus 1 -> inclusive-end
                    bottom = geoBox.y + geoBox.height - 1     // bottom: exclusive-end minus 1 -> inclusive-end
                })
            }.toBundle())
        }

        override fun removeXdgTopLevel(
            appId: String?,
            title: String?,
            nativePtr: Long
        ) {
            xdgTopLevelActivityFinishCallbackMap.filterKeys { it.nativePtr == nativePtr }.forEach { (xdgTopLevel, finishCallback) ->
                // Invoke the callback to finish the activity and remove from the map
                finishCallback.invoke()
                xdgTopLevelActivityFinishCallbackMap.remove(xdgTopLevel)
            }

        }

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

                if (mXdgTopLevelCallback.captionBarHeight == 0)
                    mXdgTopLevelCallback.captionBarHeight = intent.getIntExtra("CAPTION_BAR_HEIGHT", 0)

                ITinywlMain.Stub
                    .asInterface(getBinder(Tinywl.BINDER_KEY_TINYWL_MAIN))
                    .registerXdgTopLevelCallback(mXdgTopLevelCallback)
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