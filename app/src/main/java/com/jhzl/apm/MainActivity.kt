package com.jhzl.apm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.sample_text).setOnClickListener {
            waitTest()
        }

    }


    val lock = java.lang.Object()
    fun waitTest() {
        synchronized(lock) {
            try {
               Thread.sleep(100000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

}