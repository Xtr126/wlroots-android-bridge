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

import android.hardware.input.common.Source;
import com.android.server.inputflinger.KeyEventAction;

/**
 * Analogous to Android's native KeyEvent / NotifyKeyArgs.
 * Stores the basic information about Key events.
 */
@RustDerive(Copy=true, Clone=true, Eq=true, PartialEq=true)
parcelable KeyEvent {
    int id;
    int deviceId;
    long downTime;
    long readTime;
    long eventTime;
    Source source;
    int displayId;
    int policyFlags;
    KeyEventAction action;
    int flags;
    int keyCode;
    int scanCode;
    int metaState;
}