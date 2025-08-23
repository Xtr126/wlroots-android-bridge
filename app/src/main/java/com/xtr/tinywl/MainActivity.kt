package com.xtr.tinywl

import android.os.Bundle
import androidx.activity.ComponentActivity


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        assert(intent.getBundleExtra(Tinywl.EXTRA_KEY) != null)
        intent.setClass(this, SurfaceService::class.java)
        startService(intent)
    }
}