package com.jhzl.apm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.big_memory_btn).setOnClickListener {
            createBigArray()
        }
        findViewById<Button>(R.id.anr_btn).setOnClickListener {
            waitLock()
        }

        findViewById<Button>(R.id.block_btn).setOnClickListener {
            blockTest()
        }


    }


    private fun blockTest() {
        Thread.sleep(2000)
    }

    var lock = java.lang.Object()
    private fun waitLock() {
        synchronized(lock) {
            lock.wait()
        }
    }


    private fun createBigArray() {
        val fixedSizeArr = arrayOfNulls<Int>(1000000)
    }

}