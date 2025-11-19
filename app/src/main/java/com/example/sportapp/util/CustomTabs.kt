package com.example.sportapp.util

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent

fun openCustomTab(context: Context, url: String) {
    val builder = CustomTabsIntent.Builder()
        .setShowTitle(true)
        .setShareState(CustomTabsIntent.SHARE_STATE_ON)
        .setUrlBarHidingEnabled(true)

    val intent = builder.build()
    intent.intent.`package` = "com.android.chrome"
    intent.launchUrl(context, Uri.parse(url))
}