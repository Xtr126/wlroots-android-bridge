// ITinywlXdgTopLevel.aidl
package com.xtr.tinywl;

// Declare any non-default types here with import statements
import com.xtr.tinywl.XdgTopLevel;

interface ITinywlXdgTopLevel {
    void addXdgTopLevel(in XdgTopLevel xdgToplevel);
    void removeXdgTopLevel(in XdgTopLevel xdgToplevel);
}