package com.xtr.compound

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.view.Surface
import com.xtr.compound.ui.theme.SurfaceViewActivity

class SurfaceService : Service() {

    // Binder given to clients.
    private val binder = LocalBinder()

    data class XdgTopLevel(
        var surface: Surface?,
        val appId: String,
        val title: String
    )

    private val xdgTopLevelList: MutableList<XdgTopLevel> = mutableListOf()

    /**
     * Called by native code to add a new xdg toplevel
     */
    fun addXdgTopLevel(appId: String, title: String) {
        xdgTopLevelList.add(XdgTopLevel(null, appId, title))
        val intent = Intent(this, SurfaceViewActivity::class.java).apply {
            putExtra("APP_ID", appId)
            putExtra("TITLE", title)
        }
        startActivity(intent)
    }


    /**
     * Called by native code to remove an xdg toplevel
     */
    fun removeXdgTopLevel(appId: String, title: String) {
        for (xdgTopLevel in xdgTopLevelList)
            if (xdgTopLevel.appId == appId && xdgTopLevel.title == title)
                xdgTopLevelList.remove(xdgTopLevel)
    }

    /**
     * Called by activities when a surface is available
     */
    fun onSurfaceAvailableForAppId(
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


    fun onSurfaceChanged(format: Int, width: Int, height: Int) {
        // Call to wlroots and resize xdg toplevel now
    }

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
}