package com.xtr.tinywl

import android.content.Intent
import android.os.Bundle


class SurfaceViewActivityBundle(
    val binder: SurfaceService.LocalBinder,
    val xdgTopLevel: XdgTopLevel,

) {
    constructor(
        data: Bundle,
    ) : this(
        binder = data.getBinder("BINDER_KEY") as SurfaceService.LocalBinder,
        xdgTopLevel = data.getParcelable<XdgTopLevel>("XDG_TOP_LEVEL")!!
    )

    constructor(intent: Intent) : this(intent.extras!!)

    fun putTo(intent: Intent) {
        intent.putExtras(this.asBundle())
    }

    private fun asBundle(): Bundle = Bundle().apply {
        putBinder("BINDER_KEY", binder)
        putParcelable("XDG_TOP_LEVEL", xdgTopLevel)
    }
}