package com.jhzl.memorydetect

import android.content.Context
import com.jhzl.memorydetect.impl.JVMMemoryDetectImpl

object MemoryDetectInstall {
    var mBlockDetectApi: MemoryDetectApi? = null

    fun install(context: Context): MemoryDetectApi {
        return install(context, JVMMemoryDetectImpl(context))
    }

    fun install(context: Context, blockDetectApi: MemoryDetectApi): MemoryDetectApi {
        blockDetectApi.start()
        return blockDetectApi
    }

}