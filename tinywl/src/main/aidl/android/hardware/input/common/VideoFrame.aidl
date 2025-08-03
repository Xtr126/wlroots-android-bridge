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
 * Touch heatmap.
 *
 * The array is a 2-D row-major matrix with dimensions (height, width).
 * The heatmap data is rotated when device orientation changes.
 *
 * Example:
 *
 * If the data in the array is:
 * data[i] = i for i in 0 .. 59,
 * then it can be represented as a 10 x 6 matrix:
 *
 *  <--   width   -->
 *   0  1  2  3  4  5   ^
 *   6  7  8  9 10 11   |
 *  12 13 14 15 16 17   |
 *  18    ...      23   |
 *  24    ...      29   | height
 *  30    ...      35   |
 *  36    ...      41   |
 *  42    ...      47   |
 *  48    ...      53   |
 *  54    ...      59   v
 *
 * Looking at the device in standard portrait orientation,
 * the element "0" is the top left of the screen,
 * "5" is at the top right, and "59" is the bottom right.
 * Here height=10 and width=6.
 *
 * If the screen orientation changes to landscape (a 90 degree orientation
 * change), the frame's dimensions will become 6 x 10
 * and the data will look as follows:
 * 54 48 42 36 30 24 18 12  6  0     ^
 * ...                  13  7  1     |
 * ...                  14  8  2     | height
 * ...                  15  9  3     |
 * ...                  16 10  4     |
 * 59 53 47 41 35 29 23 17 11  5     v
 * <--        width          -->
 *
 * Here the element "0" is at the physical top left of the unrotated screen.
 *
 * Since the coordinates of a MotionEvent are also adjusted based on the
 * orientation, the rotation of the video frame data ensures that
 * the axes for MotionEvent and VideoFrame data are consistent.
 */
//@VintfStability
parcelable VideoFrame {
    /**
     * Video frame data.
     * Size of the data is height * width.
     */
    char[] data;
    int height;
    int width;
    /**
     * Time at which the frame was collected, in nanoseconds.
     * Measured with the same clock that is used to populate MotionEvent times.
     */
    long timestamp;
}
