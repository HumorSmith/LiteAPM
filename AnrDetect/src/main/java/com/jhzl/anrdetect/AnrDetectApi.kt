package com.jhzl.anrdetect

import android.content.Context

abstract class AnrDetectApi(var context: Context) {
    var blockFun: ((logInfo:String) -> Unit?)? = null
    abstract fun start()


    fun detectAnr(block: (logInfo:String) -> Unit): AnrDetectApi {
        blockFun = block
        return this
    }

}