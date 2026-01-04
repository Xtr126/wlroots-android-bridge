package com.xtr.tinywl

import android.app.ComponentCaller
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.captionBar
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

            var leftCaptionBarInset = 0

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
                val boundingRects =
                    window.decorView.rootWindowInsets.getBoundingRects(android.view.WindowInsets.Type.captionBar())
                for (rect in boundingRects) {
                    if (rect.left == 0) leftCaptionBarInset += rect.right
                }
            }

            val captionBarHeight = WindowInsets.captionBar
                .getTop(LocalDensity.current)

            App(leftCaptionBarInset, captionBarHeight)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
                window.insetsController?.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_TRANSPARENT_CAPTION_BAR_BACKGROUND,
                    WindowInsetsController.APPEARANCE_TRANSPARENT_CAPTION_BAR_BACKGROUND
                )
            }
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
            finish()
        }
    }

    override fun onNewIntent(intent: Intent, caller: ComponentCaller) {
        super.onNewIntent(intent, caller)
        startSurfaceService(intent)
    }
}

@Preview
@Composable
private fun App(
    leftCaptionBarInset: Int = 40,
    captionBarHeight: Int = 40
) {
    Box(Modifier
        .fillMaxWidth()
        .padding(start = leftCaptionBarInset.dp)
        .height(captionBarHeight.dp),
        contentAlignment = Alignment.CenterStart) {
        Text(
            text = "Keep this window open.",
            style = MaterialTheme.typography.titleSmall,
            color = Color.White
        )
    }

}