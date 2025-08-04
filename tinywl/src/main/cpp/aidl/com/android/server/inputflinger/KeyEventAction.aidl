/*
 * Copyright 2023 The Android Open Source Project
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

package com.android.server.inputflinger;

/** Different Key event actions */
enum KeyEventAction {
    /** The key has been pressed down. */
    DOWN = 0,

    /** The key has been released. */
    UP = 1,

    /**
     * Multiple duplicate key events have occurred in a row, or a
     * complex string is being delivered.  The repeat_count property
     * of the key event contains the number of times the given key
     * code should be executed.
     *
     * NOTE: This is deprecated and should never be used. This just
     * for consistency with KeyEvent actions defined in NotifyKeyArgs.
     */
    MULTIPLE = 2
}