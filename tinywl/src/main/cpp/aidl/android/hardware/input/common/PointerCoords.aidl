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

import android.hardware.input.common.Axis;

/**
 * Pointer coordinate data. Analogous to Android's PointerCoords.
 */
//@VintfStability
parcelable PointerCoords {
    /**
     * Bitfield of axes that are present in this structure.
     * Each bit position matches an axis defined in Axis.aidl.
     */
    long bits;
    /**
     * The values corresponding to each non-zero axis. This vector only
     * contains non-zero entries. If an axis that is not currently specified
     * in "bits" is requested, a zero value is returned.
     * There are only as many values stored here
     * as there are non-zero bits in the "bits" field.
     * The values are position-packed. So the first non-zero axis will be
     * at position 0, the next non-zero axis will be at position 1, and so on.
     */
    float[] values;
}
