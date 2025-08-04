/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.hardware.input.common;

import android.hardware.input.common.Action;
import android.hardware.input.common.Button;
import android.hardware.input.common.EdgeFlag;
import android.hardware.input.common.Flag;
import android.hardware.input.common.Meta;
import android.hardware.input.common.PointerCoords;
import android.hardware.input.common.PointerProperties;
import android.hardware.input.common.PolicyFlag;
import android.hardware.input.common.Source;
import android.hardware.input.common.VideoFrame;

/**
 * Analogous to Android's native MotionEvent / NotifyMotionArgs.
 * Stores the basic information about pointer movements.
 */
//@VintfStability
parcelable MotionEvent {
    /**
     * The id of the device which produced this event.
     */
    int deviceId;
    /**
     * The source type of this event.
     */
    Source source;
    /**
     * The display id associated with this event.
     */
    int displayId;
    /**
     * Time when the initial touch down occurred, in nanoseconds.
     */
    long downTime;
    /**
     * Time when this event occurred, in nanoseconds.
     */
    long eventTime;
    /**
     * The kind of action being performed.
     */
    Action action;
    /**
     * For ACTION_POINTER_DOWN or ACTION_POINTER_UP, this contains the associated pointer index.
     * The index may be used to get information about the pointer that has gone down or up.
     */
    byte actionIndex;
    /**
     * The button that has been modified during a press or release action.
     */
    Button actionButton;
    /**
     * The motion event flags.
     */
    Flag flags;
    /**
     * The motion event policy flags.
     */
    PolicyFlag policyFlags;
    /**
     * The edges, if any, that were touched by this motion event.
     */
    EdgeFlag edgeFlags;
    /**
     * The state of any meta / modifier keys that were in effect when the event was generated.
     */
    Meta metaState;
    /**
     * The state of buttons that are pressed.
     */
    Button buttonState;
    /**
     * The precision of the X coordinate being reported.
     */
    float xPrecision;
    /**
     * The precision of the Y coordinate being reported.
     */
    float yPrecision;
    /**
     * The properties of each pointer present in this motion event.
     */
    PointerProperties[] pointerProperties;
    /**
     * The coordinates of each pointer.
     */
    PointerCoords[] pointerCoords;
    /**
     * Device time at which the event occurred, in microseconds.
     * Will wrap after a little over an hour.
     */
    int deviceTimestamp;
    /**
     * The video frames, if any, associated with the current or previous motion events.
     */
    VideoFrame[] frames;
}
