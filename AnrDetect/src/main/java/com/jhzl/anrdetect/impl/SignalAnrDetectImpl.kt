package com.jhzl.anrdetect.impl

import android.content.Context
import android.os.Looper
import android.util.Log
import com.jhzl.anrdetect.AnrDetectApi

class SignalAnrDetectImpl(context: Context) : AnrDetectApi(context) {

    override fun start() {
        Log.d(TAG, "start")
        System.loadLibrary("anr-handle")
        Log.d(TAG, "start success")
    }



    private fun getMainThreadJavaStackTrace(): String {
        val stackTrace = StringBuilder()
        for (stackTraceElement in Looper.getMainLooper().thread.stackTrace) {
            stackTrace.append(stackTraceElement.toString()).append("\n")
        }
        return stackTrace.toString()
    }

     fun onAnrDumped() {
        blockFun?.let { it(getMainThreadJavaStackTrace()) }
    }

    companion object {
        private const val TAG = "SignalAnrDetectImpl"
    }
}