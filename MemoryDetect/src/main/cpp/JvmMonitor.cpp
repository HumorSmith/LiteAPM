//
// Created by eava_wu on 18-10-2.
//

#include <jni.h>
#include "include/jvmti.h"
#include<android/log.h>
#include <string>
#define TAG "JvmMonitor" // 这个是自定义的LOG的标识
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__) // 定义LOGD类型
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__) // 定义LOGI类型
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,TAG ,__VA_ARGS__) // 定义LOGW类型
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__) // 定义LOGE类型
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,TAG ,__VA_ARGS__) // 定义LOGF类型

static jvmtiEnv *localJvmtiEnv;

#ifdef __cplusplus
extern "C" {
#endif
void ObjectFreeCallback(jvmtiEnv *jvmti_env, jlong tag){
    LOGD("==========ObjectFreeCallback=======");
}


void JNICALL callbackMethodExit (jvmtiEnv *jvmti_env,
         JNIEnv* jni_env,
         jthread thread,
         jmethodID method,
         jboolean was_popped_by_exception,
         jvalue return_value){
//    char *name;
//    char *signature;
//    char *generic;
//    (jvmti_env)->GetMethodName(method, &name, &signature, &generic);
//    LOGD("method %s exit",name);
};

void JNICALL
callbackMethodEnter(jvmtiEnv *jvmti, JNIEnv* env,
                   jthread thr, jmethodID method){
//    char *name;
//    char *signature;
//    char *generic;
//    (jvmti)->GetMethodName(method, &name, &signature, &generic);
//    LOGD("method %s enter",name);
}


void ObjectAllocCallback(jvmtiEnv *jvmti, JNIEnv *jni,
                         jthread thread, jobject object,
                         jclass klass, jlong size) {
    jclass cls = jni->FindClass("java/lang/Class");
    jmethodID mid_getName = jni->GetMethodID(cls, "getName", "()Ljava/lang/String;");
    jstring name = static_cast<jstring>(jni->CallObjectMethod(klass, mid_getName));
    const char* mallocClassName=jni->GetStringUTFChars(name, JNI_FALSE);
    if (size>1000*100){
        jvmtiFrameInfo frameInfo[10];
        jint frameCount = 1;
        //获取线程的trace信息
        jvmti->GetStackTrace(thread,0,1,frameInfo,&frameCount);
        if (frameCount>=1){
            char* methodName = "";
            char* sig = "";
            char* generic = "";
            //获取方法名
            jvmti->GetMethodName(frameInfo->method,&methodName,&sig,&generic);
            LOGD("method name = %s",methodName);

            //获取方法对应的类名
            jclass curClass;
            jvmti->GetMethodDeclaringClass(frameInfo->method,&curClass);
            jstring methodClass = static_cast<jstring>(jni->CallObjectMethod(curClass, mid_getName));
            const char* methodClassName = jni->GetStringUTFChars(methodClass,JNI_FALSE);
            LOGD("==========call class = %s  method = %s obj: %s {size:%d} =======", methodClassName,methodName, mallocClassName, size);
        }
    }
    jni->ReleaseStringUTFChars(name, mallocClassName);
}



void GCStartCallback(jvmtiEnv *jvmti) {
//    LOGD("==========触发 GCStart=======");
}

void GCFinishCallback(jvmtiEnv *jvmti) {
//    LOGD("==========触发 GCFinish=======");
}


void SetAllCapabilities(jvmtiEnv *jvmti) {
    jvmtiCapabilities caps;
    jvmtiError error;
    error = jvmti->GetPotentialCapabilities(&caps);
    error = jvmti->AddCapabilities(&caps);
}

jvmtiEnv *CreateJvmtiEnv(JavaVM *vm) {
    jvmtiEnv *jvmti_env;
    jint result = vm->GetEnv((void **) &jvmti_env, JVMTI_VERSION_1_2);
    if (result != JNI_OK) {
        return nullptr;
    }
    return jvmti_env;
}

void SetEventNotification(jvmtiEnv *jvmti, jvmtiEventMode mode,
                          jvmtiEvent event_type) {
    jvmtiError err = jvmti->SetEventNotificationMode(mode, event_type, nullptr);
}


extern "C" JNIEXPORT jint JNICALL Agent_OnAttach(JavaVM *vm, char *options,
                                                 void *reserved) {
    jvmtiEnv *jvmti_env = CreateJvmtiEnv(vm);

    if (jvmti_env == nullptr) {
        return JNI_ERR;
    }
    localJvmtiEnv = jvmti_env;
    SetAllCapabilities(jvmti_env);

    jvmtiEventCallbacks callbacks;
    memset(&callbacks, 0, sizeof(callbacks));
//    callbacks.ClassFileLoadHook = &ClassTransform;
//
    callbacks.VMObjectAlloc = &ObjectAllocCallback;
    callbacks.MethodEntry = &callbackMethodEnter;
    callbacks.MethodExit = &callbackMethodExit;

    callbacks.GarbageCollectionStart = &GCStartCallback;
    callbacks.GarbageCollectionFinish = &GCFinishCallback;
    int error = jvmti_env->SetEventCallbacks(&callbacks, sizeof(callbacks));
//
//    SetEventNotification(jvmti_env, JVMTI_ENABLE,
//                         JVMTI_EVENT_GARBAGE_COLLECTION_START);
//    SetEventNotification(jvmti_env, JVMTI_ENABLE,
//                         JVMTI_EVENT_GARBAGE_COLLECTION_FINISH);
//    SetEventNotification(jvmti_env, JVMTI_ENABLE,
//                         JVMTI_EVENT_NATIVE_METHOD_BIND);
    SetEventNotification(jvmti_env, JVMTI_ENABLE,
                         JVMTI_EVENT_VM_OBJECT_ALLOC);
    SetEventNotification(jvmti_env, JVMTI_ENABLE,
                         JVMTI_EVENT_OBJECT_FREE);
    SetEventNotification(jvmti_env, JVMTI_ENABLE,
                         JVMTI_EVENT_METHOD_ENTRY);
    SetEventNotification(jvmti_env, JVMTI_ENABLE,
                         JVMTI_EVENT_METHOD_EXIT);

//    SetEventNotification(jvmti_env, JVMTI_ENABLE,
//                         JVMTI_EVENT_CLASS_FILE_LOAD_HOOK);
    LOGD("==========Agent_OnAttach=======");
    return JNI_OK;
}


//SO库被加载时调用
jint  JNI_OnLoad(JavaVM* vm, void* reserved){
    JNIEnv* env = NULL;
    //env赋值
    LOGD("GetEnv");
    //获取JniEnv
    if(vm->GetEnv((void**)&env,JNI_VERSION_1_6)!= JNI_OK){
        LOGD("GetEnv Failed");
        return -1;
    }

    return JNI_VERSION_1_6;
}


#ifdef __cplusplus
}
#endif
