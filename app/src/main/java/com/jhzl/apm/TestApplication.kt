package com.jhzl.apm

import android.app.Application
import android.util.Log
import com.jhzl.anrdetect.AnrDetectInstall
import com.jhzl.anrdetect.impl.SignalAnrDetectImpl
import com.jhzl.blockdetect.BlockDetectInstall
import com.jhzl.blockdetect.impl.ChoreographerBlockDetect
import com.jhzl.blockdetect.listener.BlockListener
import com.jhzl.memorydetect.MemoryDetectInstall

class TestApplication : Application() {
    companion object {
        private const val TAG = "TestApplication"
    }

    override fun onCreate() {
        Log.d(TAG,"start")
        super.onCreate()

        BlockDetectInstall.install(this).detectBlock(block = {
            Log.d(TAG, "detect block =>\n$it")
        }).threshHold = 1000

        AnrDetectInstall.install(this).detectAnr(block = {
            Log.d(TAG, "detect anr =>\n$it")
        })

        MemoryDetectInstall.install(this).start()



    }
}