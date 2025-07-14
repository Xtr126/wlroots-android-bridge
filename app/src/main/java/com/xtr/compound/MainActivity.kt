package com.xtr.compound

import android.os.Bundle
import android.os.IBinder
import android.view.SurfaceHolder
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.xtr.compound.ui.theme.CompoundWlTheme


class MainActivity : ComponentActivity(), SurfaceHolder.Callback2 {
    private var mCallback: ITinywlCallback? = null
    private val deathRecipient: IBinder.DeathRecipient = IBinder.DeathRecipient { runOnUiThread { this.finish() } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
/*
        enableEdgeToEdge()
        setContent {
            CompoundWlTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
*/
        intent?.getBundleExtra("bundle")
            ?.getBinder("callback")
            ?.let {
                mCallback = ITinywlCallback.Stub.asInterface(it)
            }

        mCallback?.asBinder()?.linkToDeath(deathRecipient, 0)

        window.takeSurface(this)
    }

    override fun onDestroy() {
        mCallback?.asBinder()?.unlinkToDeath(deathRecipient, 0)
        super.onDestroy()
    }

    override fun surfaceRedrawNeeded(holder: SurfaceHolder) {
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        mCallback?.onSurfaceCreated(holder.surface)
    }

    override fun surfaceChanged(
        holder: SurfaceHolder,
        format: Int,
        width: Int,
        height: Int
    ) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CompoundWlTheme {
        Greeting("Android")
    }
}