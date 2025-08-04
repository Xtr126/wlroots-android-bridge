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
 * Classification of the current gesture, if available.
 */
//@VintfStability
@Backing(type="byte")
enum Classification {
    NONE = 0,
    /**
     * Too early to classify the gesture, need more events.
     */
    AMBIGUOUS_GESTURE = 1,
    /**
     * User is force-pressing the screen.
     */
    DEEP_PRESS = 2,
}
