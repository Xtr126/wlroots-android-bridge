package com.xtr.tinywl

import android.os.Build
import android.os.Bundle
import android.view.InputQueue
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.AndroidExternalSurface
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.captionBar
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class SurfaceViewActivity : ComponentActivity() {

    lateinit var bundle: SurfaceViewActivityBundle
    val xdgTopLevel get() = bundle.xdgTopLevel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bundle = SurfaceViewActivityBundle(intent)
        setTitle(xdgTopLevel.title)
        takeSurface()
        takeInput()
        mService.xdgTopLevelActivityFinishCallbackMap[xdgTopLevel] = ::finish

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            window.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_TRANSPARENT_CAPTION_BAR_BACKGROUND,
                WindowInsetsController.APPEARANCE_TRANSPARENT_CAPTION_BAR_BACKGROUND
            )
        }
        window.setBackgroundBlurRadius(40)
    }

    @Preview(showBackground = true)
    @Composable
    fun ExternalSurfaceWithTopCaption(
        text: String = "Top Caption",
        leftCaptionBarInset: Int = 40,
        captionBarHeight: Int = 40,
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(Modifier
                .fillMaxWidth()
                .padding(start = leftCaptionBarInset.dp)
                .height(captionBarHeight.dp),
                contentAlignment = Alignment.CenterStart) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White
                )
            }

            // External surface
            AndroidExternalSurface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                isOpaque = false
            ) {
                // A surface is available, we can start rendering
               onSurface { surface, width, height ->
                   mService.onSurfaceCreated(
                       xdgTopLevel,
                       surface,
                   )

                   // React to surface dimension changes
                   surface.onChanged { newWidth, newHeight ->
                       mService.onSurfaceChanged(
                           xdgTopLevel,
                           surface = this,
                       )
                   }

                   // Cleanup if needed
                   surface.onDestroyed {
                       mService.onSurfaceDestroyed(xdgTopLevel)
                   }

               }
            }


        }

    }


    val mService get() = bundle.binder.getService()


    private fun takeSurface() {
        /*
         * Only usable in Android 15 due to
         * ASurfaceControl_createFromWindow returning NULL
         * for root ANativeWindow: https://issuetracker.google.com/issues/320706287
         */
         // window.takeSurface(this) TODO: request focus for window.takeSurface
        setContent {
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

            ExternalSurfaceWithTopCaption(xdgTopLevel.title ?: "", leftCaptionBarInset, captionBarHeight)
        }

    }
    private fun takeInput() {
        // TODO: Use AInputReceiver APIs for Android >= 15

        // We take the input queue and use in native code for Android 13/14
        window.takeInputQueue(object : InputQueue.Callback {
            private var nativePtr: Long = 0

            override fun onInputQueueCreated(queue: InputQueue) {
                nativePtr = nativeOnInputQueueCreated(queue, xdgTopLevel.nativePtr)
            }

            override fun onInputQueueDestroyed(queue: InputQueue) {
                nativeOnInputQueueDestroyed(nativePtr)
            }
        })
    }

}