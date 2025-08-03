#include <jni.h>
#include <android/input.h>
#include <android/binder_parcel.h>
#include <android/binder_parcel_jni.h>
#include <android/binder_ibinder_jni.h>
#include <aidl/com/xtr/compound/ITinywlCallback.h>
//#include <aidl/android/hardware/input/common/MotionEvent.h>

using namespace aidl::com::xtr::compound;
//using namespace aidl::android::hardware::input::common;

extern "C"
JNIEXPORT void JNICALL
Java_com_xtr_compound_MainActivity_nativeOnInputQueueCreated(JNIEnv *env, jobject thiz,
                                                             jobject queue, jobject binder) {
    AIBinder* pBinder = AIBinder_fromJavaBinder(env, binder);
    const ::ndk::SpAIBinder spBinder(pBinder);
    std::shared_ptr<ITinywlCallback> callback = ITinywlCallback::fromBinder(spBinder);

    AInputQueue *inputQueue = AInputQueue_fromJava(env, queue);
    AInputEvent* event = nullptr;

    while (AInputQueue_getEvent(inputQueue, &event) >= 0) {
        if (AInputQueue_preDispatchEvent(inputQueue, event)) {
            continue;
        }
        int32_t handled = 0;
        if (AInputEvent_getType(event) == AINPUT_EVENT_TYPE_MOTION) {

        }
        else if (AInputEvent_getType(event) == AINPUT_EVENT_TYPE_KEY) {

        }

        AInputQueue_finishEvent(inputQueue, event, handled);
    }

    AIBinder_decStrong(pBinder);
}