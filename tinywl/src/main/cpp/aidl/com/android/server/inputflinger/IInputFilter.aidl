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

import com.android.server.inputflinger.DeviceInfo;
import com.android.server.inputflinger.IInputThread;
import com.android.server.inputflinger.IInputThread.IInputThreadCallback;
import com.android.server.inputflinger.InputFilterConfiguration;
import com.android.server.inputflinger.KeyEvent;

/**
 * A local AIDL interface used as a foreign function interface (ffi) to
 * filter input events.
 *
 * NOTE: Since we use this as a local interface, all processing happens on the
 * calling thread.
 */
interface IInputFilter {

    /** Callbacks for the rust InputFilter to call into C++. */
    interface IInputFilterCallbacks {
        /** Sends back a filtered key event */
        void sendKeyEvent(in KeyEvent event);

        /** Sends back modifier state */
        void onModifierStateChanged(int modifierState, int lockedModifierState);

        /** Creates an Input filter thread */
        IInputThread createInputFilterThread(in IInputThreadCallback callback);
    }

    /** Returns if InputFilter is enabled */
    boolean isEnabled();

    /** Notifies if a key event occurred */
    void notifyKey(in KeyEvent event);

    /** Notifies if any InputDevice list changed and provides the list of connected peripherals */
    void notifyInputDevicesChanged(in DeviceInfo[] deviceInfos);

    /** Notifies when configuration changes */
    void notifyConfigurationChanged(in InputFilterConfiguration config);

    String dumpFilter();
}

