package com.jhzl.anrdetect.util

import android.os.Looper

class TraceUtil {
    companion object{
        fun getMainThreadJavaStackTrace(): String {
            val stackTrace = StringBuilder()
            for (stackTraceElement in Looper.getMainLooper().thread.stackTrace) {
                stackTrace.append(stackTraceElement.toString()).append("\n")
            }
            return stackTrace.toString()
        }
    }

}