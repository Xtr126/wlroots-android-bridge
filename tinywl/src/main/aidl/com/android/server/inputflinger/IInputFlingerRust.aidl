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

import com.android.server.inputflinger.IInputFilter;
import com.android.server.inputflinger.IInputFilter.IInputFilterCallbacks;

/**
 * A local AIDL interface used as a foreign function interface (ffi) to
 * communicate with the Rust component of inputflinger.
 *
 * NOTE: Since we use this as a local interface, all processing happens on the
 * calling thread.
 */
interface IInputFlingerRust {

   /**
    * An interface used to get a strong reference to IInputFlingerRust on boot.
    */
    interface IInputFlingerRustBootstrapCallback {
        void onProvideInputFlingerRust(in IInputFlingerRust inputFlingerRust);
    }

    /** Create the rust implementation of InputFilter. */
    IInputFilter createInputFilter(IInputFilterCallbacks callbacks);
}
