package com.jhzl.blockdetect.impl

import android.content.Context
import android.util.Log
import android.view.Choreographer
import android.view.Display
import android.view.WindowManager
import com.jhzl.blockdetect.BlockDetectApi


class ChoreographerBlockDetect(context: Context) : BlockDetectApi(context) {
    companion object {
        private const val TAG = "ChorBlockDetect"
    }


    private fun getRefreshRate(): Float { //获取屏幕主频频率
        var manager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display: Display = manager.defaultDisplay
        return display.refreshRate
    }

    inner class ChoreographyFrameCallback : Choreographer.FrameCallback {
        private var lastTime: Long = 0L
        override fun doFrame(frameTimeNanos: Long) {
            if (lastTime == 0L) {
                lastTime = frameTimeNanos
            } else {
                val times = (frameTimeNanos - lastTime) / 1000000
                val frames = (times / (1000 / getRefreshRate()))
                if (times > threshHold) {
                    Log.w(TAG, "(超过16ms):" + times + "ms" + " , 丢帧:" + frames)
                    blockFun?.let {
                        val append = StringBuilder().append("\n")
                        val allStackTraces = Thread.getAllStackTraces()
                        for (singleTrace in allStackTraces) {
                            append.append(singleTrace.key).append("\n")
                            for (item in singleTrace.value) {
                                append.append(item.toString()).append("\n")
                            }
                        }
                        it(append.toString())
                    }
                }
                lastTime = frameTimeNanos
            }
            Choreographer.getInstance().postFrameCallback(frameCallback)
        }

    }

    var frameCallback: ChoreographyFrameCallback = ChoreographyFrameCallback()


    override fun start() {
        Choreographer.getInstance().postFrameCallback(frameCallback)
    }


}