package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.LauncherApps
import android.os.UserHandle
import android.os.UserManager
import android.util.Log

class SampleReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("SampleReceiver", "onReceive called intent action : ${intent?.action}")
        val user = intent?.getParcelableExtra<UserHandle>(Intent.EXTRA_USER)
        user?.let { userHandle ->
            Log.d("SampleReceiver", "onReceive called user : ${userHandle}")
            context?.getSystemService(LauncherApps::class.java)?.let { launcherApps ->
                launcherApps.getLauncherUserInfo(userHandle).let { launcherUserInfo ->
                    Log.d("SampleReceiver", "Launcher User Info: $launcherUserInfo")
                    Log.d("SampleReceiver", "launcherUserInfo: ${launcherUserInfo?.userType}")
                    Log.d("SampleReceiver", "private profile: ${launcherUserInfo?.userType == UserManager.USER_TYPE_PROFILE_PRIVATE}")
                }
            }
        }
    }
}