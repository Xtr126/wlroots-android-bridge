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

/**
 * Motion event actions
 */
//@VintfStability
@Backing(type="int")
enum Action {
    /**
     * A pressed gesture has started, the motion contains the initial starting location.
     */
    DOWN = 0,
    /**
     * A pressed gesture has finished, the motion contains the final release location
     * as well as any intermediate points since the last down or move event.
     */
    UP = 1,
    /**
     * A change has happened during a press gesture (between AMOTION_EVENT_ACTION_DOWN and
     * AMOTION_EVENT_ACTION_UP). The motion contains the most recent point.
     */
    MOVE = 2,
    /**
     * The current gesture has been aborted.
     * You will not receive any more points in it. You must treat this as
     * an up event, but not perform any action that you normally would.
     */
    CANCEL = 3,
    /**
     * A movement has happened outside of the normal bounds of the UI element.
     * This does not provide a full gesture, but only the initial location of the movement/touch.
     */
    OUTSIDE = 4,
    /**
     * A non-primary pointer has gone down.
     */
    POINTER_DOWN = 5,
    /**
     * A non-primary pointer has gone up.
     */
    POINTER_UP = 6,
    /**
     * A change happened but the pointer is not down (unlike AMOTION_EVENT_ACTION_MOVE).
     * The motion contains the most recent point, as well as any intermediate points since
     * the last hover move event.
     */
    HOVER_MOVE = 7,
    /**
     * The motion event contains relative vertical and/or horizontal scroll offsets.
     * Use getAxisValue to retrieve the information from AMOTION_EVENT_AXIS_VSCROLL
     * and AMOTION_EVENT_AXIS_HSCROLL.
     * The pointer may or may not be down when this event is dispatched.
     * The framework will always deliver this action to the window under the pointer, which
     * may not be the window currently touched.
     */
    SCROLL = 8,
    /**
     * The pointer is not down but has entered the boundaries of a window or view.
     */
    HOVER_ENTER = 9,
    /**
     * The pointer is not down but has exited the boundaries of a window or view.
     */
    HOVER_EXIT = 10,
    /**
     * One or more buttons have been pressed.
     */
    BUTTON_PRESS = 11,
    /**
     * One or more buttons have been released.
     */
    BUTTON_RELEASE = 12,
}
