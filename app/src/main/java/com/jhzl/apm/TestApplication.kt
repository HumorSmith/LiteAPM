package com.jhzl.apm

import android.app.Application
import android.util.Log
import com.jhzl.blockdetect.BlockDetectInstall
import com.jhzl.blockdetect.impl.ChoreographerBlockDetect
import com.jhzl.blockdetect.listener.BlockListener

class TestApplication : Application() {
    companion object {
        private const val TAG = "TestApplication"
    }

    override fun onCreate() {
        super.onCreate()
        BlockDetectInstall.install(this, ChoreographerBlockDetect(this)).detectBlock(block = {
            Log.d(TAG, "detect block =>\n$it")
        }).threshHold = 1000
    }
}