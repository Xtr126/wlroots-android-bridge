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
 * Policy flags.
 * The values 1 << 4 through 1 << 23 are not currently used.
 * When a new value is added, make sure it matches a value defined in Input.h
 * and other relevant files in frameworks/base and frameworks/native.
 */
//@VintfStability
@Backing(type="int")
enum PolicyFlag {
    /**
     * Event should wake the device
     */
    WAKE = 1 << 0,
    /**
     * Key is virtual, and should generate haptic feedback
     */
    VIRTUAL = 1 << 1,
    /**
     * Key is the special function modifier
     */
    FUNCTION = 1 << 2,
    /**
     * Key represents a special gesture that has been detected
     * by the touch firmware or driver.
     */
    GESTURE = 1 << 3,
    /**
     * Event was injected
     */
    INJECTED = 1 << 24,
    /**
     * Event comes from a trusted source, such as a directly attached input
     * device or an application with system-wide event injection permission.
     */
    TRUSTED = 1 << 25,
    /**
     * Event has passed through an input filter.
     */
    FILTERED = 1 << 26,
    /**
     * Disable automatic key repeating behaviour.
     */
    DISABLE_KEY_REPEAT = 1 << 27,
    /**
     * Device was in an interactive state when the event was intercepted
     */
    INTERACTIVE = 1 << 29,
    /**
     * Event should be dispatched to applications
     */
    PASS_TO_USER = 1 << 30,
}
