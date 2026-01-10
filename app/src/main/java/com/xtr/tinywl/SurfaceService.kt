package com.xtr.tinywl

import android.app.ActivityOptions
import android.app.Service
import android.content.Intent
import android.graphics.Rect
import android.os.Binder
import android.os.IBinder
import android.view.InputQueue
import android.view.Surface
import kotlin.system.exitProcess

external fun nativeInputBinderReceived(binder: IBinder)
external fun nativeOnInputQueueCreated(queue: InputQueue, nativePtr: Long): Long
external fun nativeOnCaptionBarHeightRecieved(captionBarHeight: Int, nativePtr: Long)
external fun nativeOnInputQueueDestroyed(nativePtr: Long)

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

        override fun addXdgTopLevel(xdgTopLevel: XdgTopLevel, geoBox: WlrBox) {
            val bundle = SurfaceViewActivityBundle(
                binder, xdgTopLevel
            )
            val intent = Intent(this@SurfaceService, SurfaceViewActivity::class.java)
            bundle.putTo(intent)
            intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)

            startActivity(intent, ActivityOptions.makeBasic().apply {
                setLaunchBounds(Rect().apply {
                    left = geoBox.x                     // left = same as x
                    top = geoBox.y + captionBarHeight                    // top  = same as y
                    right = geoBox.x + geoBox.width - 1     // right: exclusive-end minus 1 -> inclusive-end
                    bottom = geoBox.y + geoBox.height + captionBarHeight - 1     // bottom: exclusive-end minus 1 -> inclusive-end
                })
            }.toBundle())
        }

        override fun removeXdgTopLevel(xdgTopLevel: XdgTopLevel) {
            xdgTopLevelActivityFinishCallbackMap.filterKeys { it.nativePtr == xdgTopLevel.nativePtr }.forEach { (xdgTopLevel, finishCallback) ->
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

                val tinywlMain = ITinywlMain.Stub
                    .asInterface(getBinder(Tinywl.BINDER_KEY_TINYWL_MAIN))
                tinywlMain.registerXdgTopLevelCallback(mXdgTopLevelCallback)
                tinywlMain.asBinder().linkToDeath({ exitProcess(0) },0)
            }
        return super.onStartCommand(intent, flags, startId)
    }

    lateinit var mService: ITinywlSurface

    /**
     * Called by activities when a surface is available
     */
    fun onSurfaceCreated(xdgTopLevel: XdgTopLevel, surface: Surface) = mService.onSurfaceCreated(xdgTopLevel.nativePtr, xdgTopLevel.nativePtrType, surface)

    fun onSurfaceChanged(xdgTopLevel: XdgTopLevel, surface: Surface) = mService.onSurfaceChanged(xdgTopLevel.nativePtr, xdgTopLevel.nativePtrType, surface)

    fun onSurfaceDestroyed(xdgTopLevel: XdgTopLevel) = mService.onSurfaceDestroyed(xdgTopLevel.nativePtr, xdgTopLevel.nativePtrType)

}