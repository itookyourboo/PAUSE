package com.insaze.pause.model

import android.graphics.drawable.Drawable

data class App(val name: String, val packageName: String, val logo: Drawable?, var blocked: Boolean = false)