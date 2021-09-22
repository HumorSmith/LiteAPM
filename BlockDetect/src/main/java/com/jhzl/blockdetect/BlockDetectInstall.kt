package com.jhzl.blockdetect

import android.content.Context
import com.jhzl.blockdetect.impl.HandlerBlockDetect
import com.jhzl.blockdetect.listener.BlockListener

object BlockDetectInstall {


    fun install(context: Context): BlockDetectApi {
        return install(context, HandlerBlockDetect(context))
    }

    fun install(context: Context, blockDetectApi: BlockDetectApi): BlockDetectApi {
        blockDetectApi.start()
        return blockDetectApi
    }


}