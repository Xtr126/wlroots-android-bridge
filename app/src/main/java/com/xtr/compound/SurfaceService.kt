package com.xtr.compound

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.view.Surface
import com.xtr.tinywl.ITinywlXdgTopLevel

class SurfaceService : Service() {
    /**
     * Class used for the client Binder. Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        // Return this instance of SurfaceService so clients can call public methods.
        fun getService(): SurfaceService = this@SurfaceService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    // Binder given to client activities.
    private val binder = LocalBinder()

    data class XdgTopLevel(
        var surface: Surface?,
        val appId: String,
        val title: String
    )

    private val xdgTopLevelList: MutableList<XdgTopLevel> = mutableListOf()

    val mXdgTopLevelRemoteCallback = object : ITinywlXdgTopLevel.Stub() {
        override fun addXdgTopLevel(appId: String, title: String) {
            xdgTopLevelList.add(XdgTopLevel(null, appId, title))
            var bundle = SurfaceViewActivityBundle(
                binder = binder,
                appId = appId,
                title = title
            )
            val intent = Intent(this@SurfaceService, SurfaceViewActivity::class.java)
            bundle.putTo(intent)

            startActivity(intent)
        }

        override fun removeXdgTopLevel(appId: String, title: String) {
            for (xdgTopLevel in xdgTopLevelList)
                if (xdgTopLevel.appId == appId && xdgTopLevel.title == title)
                    xdgTopLevelList.remove(xdgTopLevel)
        }

    }

    /**
     * Called by activities when a surface is available
     */
    fun onSurfaceCreated(
        appId: String,
        title: String,
        surface: Surface,
        width: Int,
        height: Int
    ) {
        for (xdgTopLevel in xdgTopLevelList)
            if (xdgTopLevel.appId == appId && xdgTopLevel.title == title)
                xdgTopLevel.surface = surface
        // Now surface is available, call to wlroots and make it render to it
    }
    
    fun onSurfaceChanged(
        appId: String,
        title: String,
        surface: Surface,
        width: Int,
        height: Int
    ) {
        // Call to wlroots and resize xdg toplevel now

    }

    fun onSurfaceDestroyed(appId: String, title: String) {
        // Call to wlroots and unmap xdg toplevel now
    }

}