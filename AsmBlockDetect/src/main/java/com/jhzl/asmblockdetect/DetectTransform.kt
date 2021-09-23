package com.jhzl.asmblockdetect

import com.jhzl.asmblockdetect.visitor.DetectClassVisitor
import com.zsqw123.inject.plugin.BaseTransform
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.io.File
import java.util.jar.JarFile

class DetectTransform : BaseTransform() {
    override fun processDirectory(outputDirFile: File) {
        val needClassesSequence = outputDirFile.walkTopDown().filter { it.name.isNeededClassName() }
            .map { it.readBytes() }
        processSequence(needClassesSequence)
    }


    private fun processSequence(bytesSequence: Sequence<ByteArray>) {
        bytesSequence.forEach {
            val classReader = ClassReader(it)
            val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
            val detectClassVisitor = DetectClassVisitor(classWriter)
            classReader.accept(detectClassVisitor, 0)
        }
    }

    override fun processJar(outputJarFile: File) {
        super.processJar(outputJarFile)
        JarFile(outputJarFile).use { jarFile ->
            val bytesSequence = jarFile.entries().asSequence()
                .filter { it.name.isNeededClassName() }
                .map { jarFile.getInputStream(it).readBytes() }
            processSequence(bytesSequence)
        }

    }

    override fun onTransformed() {
        super.onTransformed()
    }


}