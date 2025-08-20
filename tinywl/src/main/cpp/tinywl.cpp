#include <jni.h>
// Write C++ code here.
//
// Do not forget to dynamically load the C++ library into your application.
//
// For instance,
//
// In MainActivity.java:
//    static {
//       System.loadLibrary("tinywl");
//    }
//
// Or, in MainActivity.kt:
//    companion object {
//      init {
//         System.loadLibrary("tinywl")
//      }
//    }


extern "C"
JNIEXPORT jobject JNICALL
Java_com_xtr_tinywl_Tinywl_nativeGetInputServiceBinder(JNIEnv *env, jclass clazz) {
}
extern "C"
JNIEXPORT jobject JNICALL
Java_com_xtr_tinywl_Tinywl_nativeGetTinywlServiceBinder(JNIEnv *env, jclass clazz) {
}
extern "C"
JNIEXPORT void JNICALL
Java_com_xtr_tinywl_Tinywl_runTinywlLoop(JNIEnv *env, jclass clazz) {
}