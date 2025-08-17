// ITinywlXdgTopLevel.aidl
package com.xtr.tinywl;

// Declare any non-default types here with import statements

interface ITinywlXdgTopLevel {
    void addXdgTopLevel(String appId, String title);
    void removeXdgTopLevel(String appId, String title);
}