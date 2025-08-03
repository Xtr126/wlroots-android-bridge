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

import android.hardware.input.common.SourceClass;

/**
 * Input sources
 */
//@VintfStability
@Backing(type="int")
enum Source {
    UNKNOWN = 0,
    KEYBOARD = (1 << 8) | SourceClass.BUTTON,
    DPAD = (1 << 9) | SourceClass.BUTTON,
    GAMEPAD = (1 << 10) | SourceClass.BUTTON,
    TOUCHSCREEN = (1 << 12) | SourceClass.POINTER,
    MOUSE = (1 << 13) | SourceClass.POINTER,
    STYLUS = (1 << 14) | SourceClass.POINTER,
    BLUETOOTH_STYLUS = (1 << 15) | STYLUS,
    TRACKBALL = (1 << 16) | SourceClass.NAVIGATION,
    MOUSE_RELATIVE = (1 << 17) | SourceClass.NAVIGATION,
    TOUCHPAD = (1 << 20) | SourceClass.POSITION,
    TOUCH_NAVIGATION = (1 << 21) | SourceClass.NONE,
    ROTARY_ENCODER = (1 << 22) | SourceClass.NONE,
    JOYSTICK = (1 << 24) | SourceClass.JOYSTICK,
    /**
     * The input source is a device connected through HDMI-based bus.
     * The keys come in through HDMI-CEC or MHL signal line.
     */
    HDMI = (1 << 25) | SourceClass.BUTTON,
    SENSOR = (1 << 26) | SourceClass.NONE,
    ANY = 0xFFFFFF00,
}
