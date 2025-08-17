#include <jni.h>
#include <android/input.h>
#include <android/binder_parcel.h>
#include <android/binder_parcel_jni.h>
#include <android/binder_ibinder_jni.h>
#include <aidl/tinywl/ITinywlInput.h>
#include <aidl/android/hardware/input/common/MotionEvent.h>
#include <aidl/com/android/server/inputflinger/KeyEvent.h>

using namespace aidl::tinywl;
using namespace aidl::android::hardware::input::common;
using namespace aidl::com::android::server::inputflinger;

struct BitSet64 {
    uint64_t value;

    inline BitSet64() : value(0ULL) { }

    // Gets the value associated with a particular bit index.
    static inline uint64_t valueForBit(uint32_t n) { return 0x8000000000000000ULL >> n; }

    static inline bool hasBit(uint64_t value, uint32_t n) { return value & valueForBit(n); }

    static inline uint32_t getIndexOfBit(uint64_t value, uint32_t n) {
        return static_cast<uint32_t>(__builtin_popcountll(value & ~(0xffffffffffffffffULL >> n)));
    }

    static inline uint32_t count(uint64_t value) {
        return static_cast<uint32_t>(__builtin_popcountll(value));
    }

    static inline void markBit(uint64_t& value, uint32_t n) { value |= valueForBit(n); }
};

extern float PointerCoords_getAxisValue(const PointerCoords& coords, int32_t axis) {
    if (axis < 0 || axis > AMOTION_EVENT_MAXIMUM_VALID_AXIS_VALUE || !BitSet64::hasBit(coords.bits, axis)){
        return 0;
    }
    return coords.values[BitSet64::getIndexOfBit(coords.bits, axis)];
}

static void PointerCoords_setAxisValue(PointerCoords& coords, int32_t axis, float value) {
    if (axis < 0 || axis > AMOTION_EVENT_MAXIMUM_VALID_AXIS_VALUE) {
        return;
    }

    uint32_t index = BitSet64::getIndexOfBit(coords.bits, axis);
    if (!BitSet64::hasBit(coords.bits, axis)) {
        if (value == 0) {
            return; // axes with value 0 do not need to be stored
        }

        uint32_t count = BitSet64::count(coords.bits);
        BitSet64::markBit(reinterpret_cast<uint64_t &>(coords.bits), axis);
        for (uint32_t i = count; i > index; i--) {
            coords.values[i] = coords.values[i - 1];
        }
    }
    coords.values.push_back(value);
}

static PointerCoords getPointerCoordsForPointerIndex(const AInputEvent *event, int pointerIndex) {
    PointerCoords pointerCoords;
    for (int axis = 0; axis <= AMOTION_EVENT_MAXIMUM_VALID_AXIS_VALUE; ++axis) {
        PointerCoords_setAxisValue(pointerCoords, axis,
           AMotionEvent_getAxisValue(event, axis, pointerIndex));
    }
    return pointerCoords;
}

static PointerProperties getPointerPropertiesForPointerIndex(const AInputEvent *event, int pointerIndex) {
    PointerProperties pointerProperties;
    pointerProperties.id = AMotionEvent_getPointerId(event, pointerIndex);
    pointerProperties.toolType = static_cast<ToolType>(AMotionEvent_getToolType(event,
                                                                                pointerIndex));
    return pointerProperties;
}

static MotionEvent MotionEvent_fromAInputEvent(const AInputEvent *event) {
    MotionEvent motionEvent;
    motionEvent.source = static_cast<Source>(AInputEvent_getSource(event));
    motionEvent.deviceId = AInputEvent_getDeviceId(event);
    motionEvent.action = static_cast<Action>(AMotionEvent_getAction(event));
    motionEvent.actionButton = static_cast<Button>(AMotionEvent_getActionButton(event));
    motionEvent.actionIndex = static_cast<int>(motionEvent.action) & AMOTION_EVENT_ACTION_POINTER_INDEX_MASK;
    motionEvent.buttonState = static_cast<Button>(AMotionEvent_getButtonState(event));
    motionEvent.edgeFlags = static_cast<EdgeFlag>(AMotionEvent_getEdgeFlags(event));
    motionEvent.flags = static_cast<Flag>(AMotionEvent_getFlags(event));
    motionEvent.downTime = AMotionEvent_getDownTime(event);
    motionEvent.eventTime = AMotionEvent_getEventTime(event);
    motionEvent.metaState = static_cast<Meta>(AMotionEvent_getMetaState(event));
    motionEvent.xPrecision = AMotionEvent_getXPrecision(event);
    motionEvent.yPrecision = AMotionEvent_getYPrecision(event);

    size_t pointerCount = AMotionEvent_getPointerCount(event);
    for (int pointerIndex = 0; pointerIndex < pointerCount; ++pointerIndex) {
        PointerProperties pointerProperties = getPointerPropertiesForPointerIndex(event, pointerIndex);
        motionEvent.pointerProperties.push_back(pointerProperties);

        PointerCoords pointerCoords = getPointerCoordsForPointerIndex(event, pointerIndex);
        motionEvent.pointerCoords.push_back(pointerCoords);
    }
    return motionEvent;
}

static KeyEvent KeyEvent_fromAInputEvent(AInputEvent *event) {
    KeyEvent keyEvent;
    keyEvent.source = static_cast<Source>(AInputEvent_getSource(event));
    keyEvent.deviceId = AInputEvent_getDeviceId(event);
    keyEvent.metaState = AKeyEvent_getMetaState(event);
    keyEvent.flags = AKeyEvent_getFlags(event);
    keyEvent.eventTime = AKeyEvent_getEventTime(event);
    keyEvent.downTime = AKeyEvent_getDownTime(event);
    keyEvent.keyCode = AKeyEvent_getKeyCode(event);
    keyEvent.action = static_cast<KeyEventAction>(AKeyEvent_getAction(event));
    keyEvent.scanCode = AKeyEvent_getScanCode(event);
    return keyEvent;
}

static std::shared_ptr<ITinywlInput> callback = nullptr;

static jobject jQueueRef = nullptr;

static int ALooper_callback(int fd, int events, void* data){
    auto* inputQueue = (AInputQueue*)data;
    if (callback == nullptr || inputQueue == nullptr) return 0;

    AInputEvent* event = nullptr;

    while (AInputQueue_getEvent(inputQueue, &event) >= 0) {
        if (AInputQueue_preDispatchEvent(inputQueue, event)) {
            continue;
        }
        bool handled = false;

        if (AInputEvent_getType(event) == AINPUT_EVENT_TYPE_MOTION) {
            MotionEvent motionEvent = MotionEvent_fromAInputEvent(event);
            callback->onMotionEvent(motionEvent, &handled);
        }
        else if (AInputEvent_getType(event) == AINPUT_EVENT_TYPE_KEY) {
            KeyEvent keyEvent = KeyEvent_fromAInputEvent(event);
            callback->onKeyEvent(keyEvent, &handled);
        }

        AInputQueue_finishEvent(inputQueue, event, handled);
    }

    return 1;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_xtr_tinywl_SurfaceViewActivity_nativeOnInputQueueCreated(JNIEnv *env, jobject thiz,
                                                                  jobject jQueue) {
    if (jQueueRef != nullptr) env->DeleteGlobalRef(jQueueRef);
    jQueueRef = env->NewGlobalRef(jQueue);

    AInputQueue *inputQueue = AInputQueue_fromJava(env, jQueueRef);
    AInputQueue_attachLooper(inputQueue, ALooper_forThread(), 1, ALooper_callback, inputQueue);
}
\
extern "C"
JNIEXPORT void JNICALL
Java_com_xtr_tinywl_SurfaceServiceKt_nativeInputBinderReceived(JNIEnv *env, jclass clazz,
                                                               jobject binder) {
    AIBinder* pBinder = AIBinder_fromJavaBinder(env, binder);
    const ::ndk::SpAIBinder spBinder(pBinder);
    callback = ITinywlInput::fromBinder(spBinder);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_xtr_tinywl_SurfaceServiceKt_nativeOnInputQueueCreated(JNIEnv *env, jclass clazz,
                                                               jobject jQueue) {
    if (jQueueRef != nullptr) env->DeleteGlobalRef(jQueueRef);
    jQueueRef = env->NewGlobalRef(jQueue);

    AInputQueue *inputQueue = AInputQueue_fromJava(env, jQueueRef);
    AInputQueue_attachLooper(inputQueue, ALooper_forThread(), 1, ALooper_callback, inputQueue);
}