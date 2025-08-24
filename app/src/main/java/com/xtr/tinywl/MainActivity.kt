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
import com.xtr.tinywl.ui.theme.AppTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { App() }
    }

    override fun onStart() {
        super.onStart()
        if (intent.getBundleExtra(Tinywl.EXTRA_KEY) != null) {
            intent.setClass(this, SurfaceService::class.java)
            startService(intent)
        }
    }

    override fun onNewIntent(intent: Intent, caller: ComponentCaller) {
        super.onNewIntent(intent, caller)
        if (intent.getBundleExtra(Tinywl.EXTRA_KEY) != null) {
            intent.setClass(this, SurfaceService::class.java)
            startService(intent)
        }
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