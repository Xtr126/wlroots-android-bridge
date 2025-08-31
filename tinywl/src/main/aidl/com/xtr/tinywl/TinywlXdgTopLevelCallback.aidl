// TinywlXdgTopLevelCallback.aidl
package com.xtr.tinywl;

import com.xtr.tinywl.WlrBox;

interface TinywlXdgTopLevelCallback {
    void addXdgTopLevel(String appId, String title, long nativePtr, in WlrBox geoBox);
    void removeXdgTopLevel(String appId, String title, long nativePtr);
}