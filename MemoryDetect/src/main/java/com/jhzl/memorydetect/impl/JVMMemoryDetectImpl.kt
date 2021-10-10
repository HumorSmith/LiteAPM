package com.jhzl.memorydetect.impl

import android.content.Context
import com.jhzl.memorydetect.JVMTIHelper
import com.jhzl.memorydetect.MemoryDetectApi

class JVMMemoryDetectImpl(context: Context) : MemoryDetectApi(context) {
    override fun start() {
        JVMTIHelper.init(context)
    }

    override fun detectBlock(block: (logInfo: String) -> Unit): JVMMemoryDetectImpl {
        return this
    }

}