package com.xtr.tinywl

import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.xtr.tinywl.ui.theme.AppTheme
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var waitForWindowInsetsJob: Job
    var captionBarHeight: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        waitForWindowInsetsJob = lifecycleScope.launch {
            while (isActive) {
                delay(1000L)
            }
        }
        setContent {
            captionBarHeight = window.decorView
                .rootWindowInsets
                .getInsets(android.view.WindowInsets.Type.captionBar())
                .top
            waitForWindowInsetsJob.cancel() // Cancel the coroutine
            App()
        }
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            // Join and wait for the coroutine to finish cancellation
            waitForWindowInsetsJob.join()
            startSurfaceService(intent)
        }
    }

    private fun startSurfaceService(intent: Intent) {
        if (intent.getBundleExtra(Tinywl.EXTRA_KEY) != null) {
            intent.putExtra("CAPTION_BAR_HEIGHT", captionBarHeight!!)
            intent.setClass(this@MainActivity, SurfaceService::class.java)
            startService(intent)
        }
    }

    override fun onNewIntent(intent: Intent, caller: ComponentCaller) {
        super.onNewIntent(intent, caller)
        startSurfaceService(intent)
    }
}

@Preview
@Composable
private fun App() {
    AppTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Keep this window open.")
            }
        }
    }
}