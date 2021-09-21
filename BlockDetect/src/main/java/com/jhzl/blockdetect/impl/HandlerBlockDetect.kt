package com.jhzl.blockdetect.impl

import android.content.Context
import android.util.Log
import android.util.Printer
import com.jhzl.blockdetect.BlockDetectApi


class HandlerBlockDetect(context: Context) : BlockDetectApi(context) {
    companion object {
        val TAG = "HandlerBlockDetect"
    }

    var startTime = 0L
    var endTime = 0L
    var isStart = true
    override fun start() {
        context.mainLooper.setMessageLogging(getPrinter())
    }

    private fun getPrinter(): Printer {
        return Printer { log ->
            log?.takeIf { log.contains(">>>>> Dispatching to ") }?.let {
                startTime = System.currentTimeMillis()
                isStart = true
            }
            log?.takeIf { isStart }?.contains("<<<<< Finished to").let {
                endTime = System.currentTimeMillis()
                isStart = false
                if (endTime - startTime > threshHold) {
                    // 获取线程堆栈信息
                    val append = StringBuilder().append("\n")
                    val allStackTraces = Thread.getAllStackTraces()
                    for (singleTrace in allStackTraces) {
                        append.append(singleTrace.key).append("\n")
                        for (item in singleTrace.value) {
                            append.append(item.toString()).append("\n")
                        }
                    }
                    Log.d(TAG,"blockFun = $blockFun")
                    blockFun?.let { blockCall -> blockCall(append.toString()) }
                }
            }


        }
    }
}


