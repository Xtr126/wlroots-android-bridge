// TinywlXdgTopLevelCallback.aidl
package com.xtr.tinywl;

import com.xtr.tinywl.WlrBox;
import com.xtr.tinywl.XdgTopLevel;

interface TinywlXdgTopLevelCallback {
    void addXdgTopLevel(in XdgTopLevel xdgTopLevel, in WlrBox geoBox);
    void removeXdgTopLevel(in XdgTopLevel xdgTopLevel);
}