package com.piwniczna.mojakancelaria.utils

import android.os.Build
import android.text.Html
import android.text.Spanned

class SpannedText {
    companion object {
        fun getSpannedText(text: String): Spanned? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(text)
            }
        }
    }
}