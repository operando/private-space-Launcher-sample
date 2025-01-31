package com.example.myapplication

import android.content.Intent
import android.content.Intent.ACTION_PROFILE_ADDED
import android.content.Intent.ACTION_PROFILE_AVAILABLE
import android.content.Intent.ACTION_PROFILE_REMOVED
import android.content.Intent.ACTION_PROFILE_UNAVAILABLE
import android.content.IntentFilter
import android.content.pm.LauncherApps
import android.os.Bundle
import android.os.UserManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    var sampleReceiver: SampleReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intentFilter = IntentFilter().apply {
            addAction(Intent.ACTION_MANAGED_PROFILE_AVAILABLE)
            addAction(Intent.ACTION_MANAGED_PROFILE_UNAVAILABLE)
            addAction(Intent.ACTION_MANAGED_PROFILE_REMOVED)
            addAction(ACTION_PROFILE_ADDED)
            addAction(ACTION_PROFILE_REMOVED)
//            addAction(ACTION_PROFILE_UNLOCKED)
//            addAction(ACTION_PROFILE_LOCKED)
            addAction(ACTION_PROFILE_AVAILABLE)
            addAction(ACTION_PROFILE_UNAVAILABLE)
        }

        sampleReceiver = SampleReceiver()

        ContextCompat.registerReceiver(
            this, sampleReceiver, intentFilter, ContextCompat.RECEIVER_NOT_EXPORTED
        )

        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android", modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(sampleReceiver)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Text(
        text = "Hello $name!", modifier = modifier.clickable(onClick = {
//            val apps = context.packageManager.getInstalledApplications(0)
//            apps.forEach {
//                println("Installed App: $it")
//            }

            context.getSystemService(UserManager::class.java)?.let { userManager ->
                userManager.userProfiles.forEach { userHandle ->
                    println("UserHandle: $userHandle")
                    println("${userManager.isQuietModeEnabled(userHandle)}")
                    val launcherApps = context.getSystemService(LauncherApps::class.java)
                    launcherApps.getLauncherUserInfo(userHandle).let { launcherUserInfo ->
                        println("Launcher User Info: $launcherUserInfo")
                        println("launcherUserInfo: ${launcherUserInfo?.userType}")
                        println("private profile: ${launcherUserInfo?.userType == UserManager.USER_TYPE_PROFILE_PRIVATE}")
                        if(launcherUserInfo?.userType == UserManager.USER_TYPE_PROFILE_PRIVATE && userManager.isQuietModeEnabled(userHandle)) {
                            println("unlock private space")
                            userManager.requestQuietModeEnabled(false, userHandle)
                        }
                    }
                }
            }
        })
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}