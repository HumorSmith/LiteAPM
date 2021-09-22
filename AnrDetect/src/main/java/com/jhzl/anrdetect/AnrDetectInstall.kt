package com.jhzl.anrdetect

import android.content.Context
import android.util.Log
import com.jhzl.anrdetect.impl.SignalAnrDetectImpl
import com.jhzl.anrdetect.util.TraceUtil

object AnrDetectInstall {
    var mBlockDetectApi: AnrDetectApi? = null
    fun install(context: Context): AnrDetectApi {
        return install(context, SignalAnrDetectImpl(context))
    }

    fun install(context: Context, blockDetectApi: AnrDetectApi): AnrDetectApi {
        blockDetectApi.start()
        mBlockDetectApi = blockDetectApi
        return blockDetectApi
    }

    private const val TAG = "AnrDetectInstall"

    fun onStackDump() {
        Log.d(TAG, "onStackDump")
        mBlockDetectApi?.blockFun?.let { it(TraceUtil.getMainThreadJavaStackTrace()) }
    }


}