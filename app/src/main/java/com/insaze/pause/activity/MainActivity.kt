package com.insaze.pause.activity

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.insaze.pause.repository.AppManager
import com.insaze.pause.adapter.InstalledAppsAdapter
import com.insaze.pause.R

class MainActivity : AppCompatActivity() {

    val appManager = AppManager.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        val apps = appManager.getInstalledApps()
        val adapter = InstalledAppsAdapter(this, apps) {
            if (it.blocked)
                appManager.db.insert(it.packageName)
            else appManager.db.delete(it.packageName)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        if (Settings.Secure.getInt(
                applicationContext.contentResolver,
                Settings.Secure.ACCESSIBILITY_ENABLED
            ) != 1
        ) {
            showAlertDialog()
        }
    }

    private fun showAlertDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.permission_title)
            .setMessage(R.string.permission_description)
            .setPositiveButton(R.string.permission_continue) { _: DialogInterface, _: Int ->
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                startActivityForResult(intent, 209)
            }
            .setNegativeButton(R.string.permission_cancel) { _: DialogInterface, _: Int -> }
            .show()
    }
}