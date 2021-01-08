package com.insaze.pause.activity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextSwitcher
import androidx.appcompat.app.AppCompatActivity
import com.insaze.pause.R


class PauseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pause)

        var ableToContinue = false
        val appName = intent.extras?.getString("app_name")

        supportActionBar?.hide()

        val textSwitcher = findViewById<TextSwitcher>(R.id.textSwitcher)
        textSwitcher.setText(resources.getString(R.string.pause_default))

        textSwitcher.setOnClickListener {
            if (ableToContinue) finishAndRemoveTask()
        }

        textSwitcher.setInAnimation(this, android.R.anim.fade_in)
        textSwitcher.setOutAnimation(this, android.R.anim.fade_out)

        val butterfly = findViewById<ImageView>(R.id.butterfly)

        val anim = AlphaAnimation(0.4f, 1f)
        anim.duration = 2000
        anim.fillAfter = true
        anim.repeatMode = Animation.REVERSE
        anim.repeatCount = Animation.INFINITE

        butterfly.startAnimation(anim)

        val countDownTimer = object : CountDownTimer(5200, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                ableToContinue = true
                textSwitcher.setText(resources.getString(R.string.pause_continue).format(appName))
            }
        }

        countDownTimer.start()
    }

    override fun onBackPressed() {
        finishAndRemoveTask()
        val i = Intent(Intent.ACTION_MAIN)
        i.addCategory(Intent.CATEGORY_HOME)
        startActivity(i)
    }

    override fun onPause() {
        super.onPause()
        finishAndRemoveTask()
    }
}