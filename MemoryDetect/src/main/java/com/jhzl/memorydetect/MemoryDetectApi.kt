package com.jhzl.memorydetect

import android.content.Context

abstract class MemoryDetectApi(var context: Context) {
    var threshHold: Int = 1000
    var blockFun: ((logInfo:String) -> Unit?)? = null
    abstract fun start()

    open fun detectBlock(block: (logInfo:String) -> Unit):MemoryDetectApi {
        blockFun = block
        return this
    }

}