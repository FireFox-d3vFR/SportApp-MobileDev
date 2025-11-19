package com.example.sportapp.ui.common

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import android.net.Uri

fun openUrl(context: Context, url: String) {
    val intent = CustomTabsIntent.Builder().build()
    intent.launchUrl(context, Uri.parse(url))
}