package com.insaze.pause.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.ComponentName
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import com.insaze.pause.repository.AppManager
import com.insaze.pause.activity.PauseActivity


class AppLaunchDetectService: AccessibilityService() {

    var state = 0
    val appManager = AppManager.getInstance(this)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("SERVICE", "START")
        Toast.makeText(baseContext, "Service started", Toast.LENGTH_SHORT).show()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onServiceConnected() {
        super.onServiceConnected()

        val config = AccessibilityServiceInfo()
        config.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
        config.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
        config.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS

        serviceInfo = config
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            if (event.packageName != null && event.className != null) {
                val componentName =  ComponentName(
                    event.packageName.toString(),
                    event.className.toString()
                )
                val activityInfo = tryGetActivity(componentName)
                if (activityInfo != null) {
                    if (appManager.isBlocked(activityInfo.packageName)) {
                        if (state == 0) {
                            val intent = Intent(this, PauseActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.putExtra("app_name", when(activityInfo.applicationInfo) {
                                null -> "Unknown"
                                else -> packageManager.getApplicationLabel(activityInfo.applicationInfo)
                            })
                            startActivity(intent)
                        }
                        state += 1
                    } else if (activityInfo.packageName == packageName)
                        state += 1
                    else state = 0
                    Log.i("CurrentActivity", componentName.flattenToShortString())
                }
            }
        }
    }

    private fun tryGetActivity(componentName: ComponentName): ActivityInfo? {
        return try {
            packageManager.getActivityInfo(componentName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            null;
        }
    }

    override fun onInterrupt() {
        appManager.closeDb()
    }

    override fun onDestroy() {
        super.onDestroy()
        appManager.closeDb()
    }
}