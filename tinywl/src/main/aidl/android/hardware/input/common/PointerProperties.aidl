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

import android.hardware.input.common.ToolType;

/**
 * Properties of a particular pointer. Analogous to Android's PointerProperties.
 */
//@VintfStability
parcelable PointerProperties {
    /**
     * A number identifying a specific pointer. When a pointer is lifted,
     * this value may be reused by another new pointer, even during the
     * same gesture. For example, if there are two pointers touching the screen
     * at the same time, they might have pointer ID's of 0 and 1. If the
     * pointer with id = 0 is lifted, while the pointer with id = 1 remains, and
     * a new pointer is placed on the screen, then the new pointer may receive
     * an id of 0. While a pointer is active, it is guaranteed to keep the same
     * id.
     */
    int id;
    /**
     * Type of tool used to make contact, such as a finger or stylus, if known.
     */
    ToolType toolType;
}
