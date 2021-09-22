#include <jni.h>
#include <string>
#include "SignalHandle.h"
#include <pthread.h>
#include <unistd.h>
#include <cstring>
#include <malloc.h>
#include<android/log.h>

#ifdef __cplusplus
extern "C" {
#endif

#define TAG "helloworld-jni" // 这个是自定义的LOG的标识
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__) // 定义LOGD类型

const int TARGET_SIG = SIGQUIT;
static struct StacktraceJNI {
    jclass detectClass;
    jobject detectObject;
    jmethodID detectOnAnrDumped;

} detectStruct;

bool isSignalInstall = false;
JNIEnv *env = NULL;

void signalHandler(int sig, siginfo_t *si, void *uc) {
    if (sig != TARGET_SIG) {
        LOGD("sig is %d not we need", sig);
        return;
    }
    LOGD("sig is = %d",sig);
    env->CallVoidMethod(detectStruct.detectObject, detectStruct.detectOnAnrDumped);
}


void registerSignal() {
    int r;
    sigset_t set;
    struct sigaction act;
    //un-block the SIGQUIT mask for current thread, hope this is the main thread
    //重置set,等待使用
    sigemptyset(&set);
    //添加信号到信号集合
    sigaddset(&set, SIGQUIT);
    LOGD("pthread_sigmask before");
    //SIG_UNBLOCK 清空内存中的，据用户设置的数据，对内核中的数据进行解除阻塞，就是解除内核中set的最,失败直接返回errorNo
    if (0 != (r = pthread_sigmask(SIG_UNBLOCK, &set, NULL))) return;
    //register new signal handler for SIGQUIT(注册新的SIGQUIT捕获函数)
    memset(&act, 0, sizeof(act));
    sigfillset(&act.sa_mask);
    act.sa_sigaction = signalHandler;
    act.sa_flags = SA_RESTART | SA_SIGINFO;
    LOGD("pthread_sigmask after");
    if (0 != sigaction(SIGQUIT, &act, NULL)) {
        //注册SIG_QUIT失败，清空内核中的sigaction
        pthread_sigmask(SIG_SETMASK, NULL, NULL);
        return ;
    }
    LOGD("Signal handler installed.");

}

void initBridge() {
    detectStruct.detectClass = env->FindClass("com/jhzl/anrdetect/AnrDetectInstall");
    if (!detectStruct.detectClass ){
        LOGD("find detect class failed");
        return;
    }
    //获取Filed后面要加分号
    jfieldID instanceFiled= env->GetStaticFieldID( detectStruct.detectClass,"INSTANCE","Lcom/jhzl/anrdetect/AnrDetectInstall;");
    detectStruct.detectObject = env->GetStaticObjectField(detectStruct.detectClass,instanceFiled);
    //设置为全局引用，否则局部引用会被回收
    detectStruct.detectObject =  env->NewGlobalRef(detectStruct.detectObject);
    detectStruct.detectOnAnrDumped = env->GetMethodID(detectStruct.detectClass,"onStackDump","()V");
    LOGD("initBridge success");
}

//回调函数 在这里面注册函数
jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    //env赋值
    LOGD("GetEnv %d", getpid());
    //获取JniEnv
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        LOGD("GetEnv Failed");
        return -1;
    }
    initBridge();
    registerSignal();
    //返回jni 的版本
    return JNI_VERSION_1_6;
}

#ifdef __cplusplus
}
#endif