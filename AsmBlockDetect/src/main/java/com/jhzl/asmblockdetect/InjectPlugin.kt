package com.jhzl.asmblockdetect

import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class InjectPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val androidAppExtension = target.extensions.findByType(AppExtension::class.java)
        val androidLibExtension = target.extensions.findByType(LibraryExtension::class.java)
        if (androidAppExtension != null || androidLibExtension != null) {
            val injectTransform = DetectTransform()
            androidAppExtension?.registerTransform(injectTransform)
            androidLibExtension?.registerTransform(injectTransform)
        }
        println("DetectInject Plugin Loaded!")
    }
}