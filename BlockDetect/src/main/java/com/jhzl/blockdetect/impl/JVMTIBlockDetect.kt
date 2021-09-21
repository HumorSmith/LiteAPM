package com.jhzl.blockdetect.impl

import android.content.Context
import android.os.Build
import android.os.Debug
import androidx.annotation.RequiresApi
import com.jhzl.blockdetect.BlockDetectApi

class JVMTIBlockDetect(context: Context): BlockDetectApi(context) {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun start() {
        Debug.attachJvmtiAgent("","",null)
    }
}