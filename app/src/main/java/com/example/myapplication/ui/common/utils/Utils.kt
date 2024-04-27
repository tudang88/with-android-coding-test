package com.example.myapplication.ui.common.utils

import android.content.Context
import android.content.Intent

fun shareImage(context: Context, body: String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            getHtmlBody(body)
        )
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, "Share Photo")
    context.startActivity(shareIntent)
}

/**
 * build html text
 */
private fun getHtmlBody(imageUrl:String): String {
    return "<h1><img width=\"100\" src=\"$imageUrl\"></h1>"
}