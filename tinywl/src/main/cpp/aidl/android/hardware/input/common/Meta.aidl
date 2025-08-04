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
 * Meta key / modifier state
 */
//@VintfStability
@Backing(type="int")
enum Meta {
    NONE = 0,
    /**
     * One of the ALT meta keys is pressed.
     */
    ALT_ON = 1 << 1,
    /**
     * The left ALT meta key is pressed.
     */
    ALT_LEFT_ON = 1 << 4,
    /**
     * The right ALT meta key is pressed.
     */
    ALT_RIGHT_ON = 1 << 5,
    /**
     * One of the SHIFT meta keys is pressed.
     */
    SHIFT_ON = 1 << 0,
    /**
     * The left SHIFT meta key is pressed.
     */
    SHIFT_LEFT_ON = 1 << 6,
    /**
     * The right SHIFT meta key is pressed.
     */
    SHIFT_RIGHT_ON = 1 << 7,
    /**
     * The SYM meta key is pressed.
     */
    SYM_ON = 1 << 2,
    /**
     * The FUNCTION meta key is pressed.
     */
    FUNCTION_ON = 1 << 3,
    /**
     * One of the CTRL meta keys is pressed.
     */
    CTRL_ON = 1 << 12,
    /**
     * The left CTRL meta key is pressed.
     */
    CTRL_LEFT_ON = 1 << 13,
    /**
     * The right CTRL meta key is pressed.
     */
    CTRL_RIGHT_ON = 1 << 14,
    /**
     * One of the META meta keys is pressed.
     */
    META_ON = 1 << 16,
    /**
     * The left META meta key is pressed.
     */
    META_LEFT_ON = 1 << 17,
    /**
     * The right META meta key is pressed.
     */
    META_RIGHT_ON = 1 << 18,
    /**
     * The CAPS LOCK meta key is on.
     */
    CAPS_LOCK_ON = 1 << 20,
    /**
     * The NUM LOCK meta key is on.
     */
    NUM_LOCK_ON = 1 << 21,
    /**
     * The SCROLL LOCK meta key is on.
     */
    SCROLL_LOCK_ON = 1 << 22,
}
