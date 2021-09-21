package com.jhzl.blockdetect

import android.content.Context

abstract class BlockDetectApi(var context: Context) {
    var threshHold: Int = 1000
    var blockFun: ((logInfo:String) -> Unit?)? = null
    abstract fun start()

    fun detectBlock(block: (logInfo:String) -> Unit):BlockDetectApi {
        blockFun = block
        return this
    }

}