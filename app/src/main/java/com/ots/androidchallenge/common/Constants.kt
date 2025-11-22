package com.ots.androidchallenge.common

import android.net.Uri
import androidx.core.net.toUri

object Constants {
    val CONTENT_URI: Uri = "content://com.iav.contestdataprovider/text?limit=6".toUri()
    const val RETRY_ATTEMPT = 3
}
