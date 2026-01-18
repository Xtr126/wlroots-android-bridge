# Overview
This is an attempt at an wayland compositor for Android with proper GPU acceleration and multi-window integration. Even if other projects for running wayland and X11 applications on Android already exist, they are flawed due to having to support proprietary Android drivers. 
Those projects use CPU write/read usage flags for buffers and workarounds to get around limitations of using proprietary Android drivers.  
We keeps the buffer entirely in GPU for the whole process of rendering/compositing and presenting to display.  
This is made possible by a clever trick I discovered - using gralloc handle from open source minigbm/gbm gralloc headers to extract buffer attributes and import buffer into wlroots.    
This doesn't again use OpenGL ES shaders/drawing commands or Vulkan swapchains to render pixels from the buffer onto egl native window surface which submits the buffer to buffer queue of Android Surface, instead uses new SurfaceControl/SurfaceTransaction APIs (introduced in APIs 29, 33, 34 and 36) to submit memory buffers in GPU directly to SurfaceFlinger for presentation, avoiding use of EGL/Vulkan.      
A zero-copy rendering path - client texture is rendered onto a GPU memory buffer allocated by minigbm/gbm gralloc which is then submitted for presenting to display by SurfaceFlinger, supporting hardware overlays (direct scanout) and GPU compositing/sampling operations by Android.  
GPU acceleration for wlroots gles2/vulkan (either one works) renderer works using mesa open source drivers (currently tested with iris on Intel GPU).   

All in all, this means that performance can be within margin of error as if the same wayland applications ran natively on desktop Linux with similar hardware.  
Notably, Vulkan 1.4 and desktop OpenGL 4.6 works flawlessly (tested on Intel GPUs) even though Android itself does not support desktop OpenGL.

# Client application (Android/Kotlin) and wayland server/compositor architecture
* A service in the app receives IBinder objects from a bundle when it recieves an intent by Tinywl app_process, then it starts a new Activity and callbacks to TinywlMainService C++ AIDL service (running in labwc) using binder IPC, as soon as the surface is created. 
* `tinywl` module has kotlin code that runs in termux env (app_process). It takes care of launching the app and receiving binder from NDK AIDL service implementation in native code.
* Termux handles the huge amount of dependencies of wlroots/mesa/wayland for us.
* It has a wlr_allocator implementation that uses AHardwareBuffers internally, so the tinywl compositor internally uses Android's minigbm gralloc allocated buffers.
* Each wayland client's texture is rendered onto a buffer, which is then presented on-screen using ASurfaceTransaction_setBuffer for each Window.
* [TermuxAm](https://github.com/termux/TermuxAm/) is modified and used as a module to launch the activity of our app from app_process with intent containing bundle with IBinder AIDL callback object obtained from AIBinder in C++ code.

## Roadmap

- [x] Vulkan/OpenGL acceleration for clients in chroot and Termux  
- [x] Display fullscreen output without any glitches
- [x] Display individual wayland apps in resizeable Android windows 
- [x] Send input events from Android client app to wlroots
    - [x] Keyboard+Mouse 
    - [ ] Mouse capture/lock 
    - [ ] Touchscreen
    - [ ] Stylus
    - [ ] Gamepads
- [x] LXC/chroot integration

# Build 
Must have app installed on-device for linking against shared libraries at build time.

    # Build in termux environment
    git clone https://github.com/Xtr126/tinywl-ANativeWindow
    cd tinywl-ANativeWindow
    make
    
## Building the app

    # You might need to run this before gradle sync
    ./gradlew compileDebugAidl

    ./gradlew assembleDebug

# Usage
Install the Android app (only source code is available in this repo).  
Install wlroots and mesa packages from [Xtr126/termux-packages](https://github.com/Xtr126/termux-packages/releases/).  
Then run the following command in labwc-android/src/android directory (after building).

    sh start.sh 

# Acknowledgments

* [Termux App](https://github.com/termux/termux-app/)
* [TermuxAm](https://github.com/termux/TermuxAm/)
* [wlroots and tinywl](https://gitlab.freedesktop.org/wlroots/wlroots)
* https://github.com/termux/termux-packages/pull/19587
* [minigbm(cros_gralloc_handle.h)](http://android.googlesource.com/platform/external/minigbm/) 
* [gralloc_handle from libdrm for gbm gralloc](https://gitlab.freedesktop.org/mesa/libdrm) 

## Copyright and License
The source code is licensed under the GPL v3.   
```
wlroots-android-bridge
Copyright (C) 2025 Xtr126 <k.gunetilleke@gmail.com>

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; version 3.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License 
along with this program. If not, see https://www.gnu.org/licenses/.
```

# What works now
28-07-2025: Finally vkcube renders at fluid 60fps without any color/image representation issues, after using `cros_gralloc_handle.h` C++ header from minigbm to extract pixel format, stride, offset, planes and other attributes from allocated AHardwareBuffer.  
29-08-2025: Multiple windows 
![IMG_20250829_122825_395](https://github.com/user-attachments/assets/12ac5d5f-ccee-4bd6-a725-cd28a1948280)
