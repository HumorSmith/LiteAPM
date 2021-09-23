package com.jhzl.asmblockdetect.visitor

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.commons.AdviceAdapter

class DetectMethodVisitor(
    api: Int, methodVisitor: MethodVisitor?, access: Int, name: String?,
    descriptor: String?
) : AdviceAdapter(
    api,
    methodVisitor, access, name, descriptor
) {
    override fun onMethodEnter() {
        //插入开始点
        mv.visitLdcInsn("method")
        mv.visitLdcInsn("start")
        mv.visitMethodInsn(INVOKESTATIC, "android/util/Log", "d", "(Ljava/lang/String;Ljava/lang/String;)I", false);
    }

    override fun onMethodExit(opcode: Int) {
        //插入结束点
        mv.visitLdcInsn("method")
        mv.visitLdcInsn("end")
        mv.visitMethodInsn(INVOKESTATIC, "android/util/Log", "d", "(Ljava/lang/String;Ljava/lang/String;)I", false);

    }
}