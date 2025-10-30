package com.xtr.tinywl

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.xtr.tinywl.ui.theme.AppTheme

class InfoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { App() }
    }
    private fun openUrl(url: String) = Intent(Intent.ACTION_VIEW, url.toUri()).also(this::startActivity)

    private fun copyTextToClipboard(textToCopy: String, label: String = "Copied Text") {
        val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

        val clipData = ClipData.newPlainText(label, textToCopy)

        clipboardManager.setPrimaryClip(clipData)

        Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    @Preview(showBackground = true)
    @Composable
    private fun App() {
        AppTheme {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                Box (modifier = Modifier.padding(innerPadding)) {
                    Column (modifier = Modifier.padding(20.dp)) {
                        BuildInfo()
                        Spacer(modifier = Modifier.height(12.dp))
                        UsageInfo()
                        Spacer(modifier = Modifier.height(12.dp))
                        CopyrightAndLicense()
                    }
                }
            }
        }
    }

    @Composable
    private fun UsageInfo() {
        InfoCard(
            title = "Usage",
            body1 = "sh start.sh",
            body2 = { DescriptionCard(
                """Install the Android app (only source code is available in this repo).
Install wlroots and mesa packages from Xtr126/termux-packages (click to open).
Then run the above command in tinywl-ANativeWindow directory (after building)."""
            ) { openUrl("https://github.com/Xtr126/termux-packages/releases/tag/wlroots-0.18") } },
            icon = Icons.Default.Info,
            iconContentDescription = null
        )
    }


    @Composable
    private fun BuildInfo() {
        val text = """# Build in termux environment
git clone https://github.com/Xtr126/tinywl-ANativeWindow
cd tinywl-ANativeWindow
make"""
        InfoCard(
            title = "Build",
            body1 = "Click to copy to clipboard.",
            body2 = { DescriptionCard(text) { copyTextToClipboard(text) } },
            icon = Icons.Default.Build,
            iconContentDescription = null
        )
    }

    @Composable
    private fun CopyrightAndLicense() {
        InfoCard(
            title = "License",
            body1 = """The source code is available under the GPL v3 license at:""",
            body2 = { DescriptionCard("https://github.com/Xtr126/wlroots-android-bridge") { openUrl("https://github.com/Xtr126/wlroots-android-bridge") } },
            icon = Icons.Default.Warning,
            iconContentDescription = null
        )
    }
}

/**
 * A small card with a compact icon, title and body text.
 * - title is required, body is optional.
 */
@Composable
fun InfoCard(
    modifier: Modifier = Modifier,
    title: String,
    body1: String? = null,
    body2: @Composable (BoxScope.() -> Unit)? = null,
    icon: ImageVector? = Icons.Default.Info,
    iconContentDescription: String? = null,
) {

    // Use clickable Card when onClick provided, otherwise non-clickable card
    ElevatedCard(
        modifier = modifier,
    ) {
        Column (modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = iconContentDescription,
                        modifier = Modifier
                            .size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (!body1.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = body1,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
            if (body2 != null) {
                Spacer(Modifier.height(8.dp))
                Box(Modifier.padding(4.dp)) {
                    body2()
                }
            }
        }
    }
}


@Composable
fun DescriptionCard(text: String, onClick: () -> Unit = {}) {
    ElevatedCard(modifier =  Modifier.fillMaxWidth().wrapContentHeight(), onClick = onClick, colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)) {
        Text(
            text, Modifier.padding(12.dp),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
