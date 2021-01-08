package com.insaze.pause.repository

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.insaze.pause.model.App

class AppManager(val context: Context) {

    companion object {
        private var appManager: AppManager? = null
        fun getInstance(context: Context) = appManager ?: AppManager(context)
    }

    val db = DBHelper(context)

    fun getInstalledApps(): List<App> {
        val pm = context.packageManager
        val appInfos = pm.getInstalledApplications(PackageManager.GET_META_DATA)

        return appInfos.filter { info ->
            info.flags and ApplicationInfo.FLAG_SYSTEM == 0
        }.map { info ->
            App(
                info.loadLabel(pm).toString(),
                info.packageName,
                info.loadIcon(pm),
                db.isBlocked(info.packageName)
            )
        }.sortedWith(compareBy<App> { !it.blocked }.thenBy { it.name })
    }

    fun isBlocked(packageName: String) = db.isBlocked(packageName)

    fun closeDb() {
        db.close()
    }
}