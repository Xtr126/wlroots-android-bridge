// ITinywlMain.aidl
package com.xtr.tinywl;

import com.xtr.tinywl.TinywlXdgTopLevelCallback;

interface ITinywlMain {
    void registerXdgTopLevelCallback(TinywlXdgTopLevelCallback xdgTopLevelCallback);
}