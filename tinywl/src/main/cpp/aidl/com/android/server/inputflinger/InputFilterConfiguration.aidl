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

/**
 * Contains data for the current Input filter configuration
 */
parcelable InputFilterConfiguration {
    // Threshold value for Bounce keys filter (check bounce_keys_filter.rs)
    long bounceKeysThresholdNs;
    // If sticky keys filter is enabled (check sticky_keys_filter.rs)
    boolean stickyKeysEnabled;
    // Threshold value for Slow keys filter (check slow_keys_filter.rs)
    long slowKeysThresholdNs;
}