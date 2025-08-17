package com.xtr.compound

import android.content.Intent
import android.os.Bundle


class SurfaceViewActivityBundle(
    val binder: SurfaceService.LocalBinder,
    val appId: String,
    val title: String

) {
    constructor(
        data: Bundle,
    ) : this(
        binder = data.getBinder("BINDER_KEY") as SurfaceService.LocalBinder,
        appId = data.getString("APP_ID")!!,
        title = data.getString("TITLE")!!
    )

    constructor(intent: Intent) : this(intent.extras!!)
    
    fun putTo(intent: Intent) {
        intent.extras?.putThisBundle()
    }
    
    private fun Bundle.putThisBundle() {
        putBinder("BINDER_KEY", binder)
        putString("APP_ID", appId)
        putString("TITLE", title)
    }
}