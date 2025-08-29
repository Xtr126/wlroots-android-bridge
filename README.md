# Why?
Whilst other projects for running wayland and X11 applications on Android already exist, this has proper GPU acceleration and multi window integration.  
Those projects use CPU write/read usage flags for buffers and workarounds to get around limitations of using proprietary Android drivers.  
[tinywl-ANativeWindow](https://github.com/Xtr126/tinywl-ANativeWindow) keeps the buffer entirely in GPU for the whole process of rendering/compositing and presenting to display, which is possible with open source mesa drivers.  
It uses gralloc handle from minigbm/gbm gralloc headers to extract buffer attributes.  
Proprietary Android drivers may have their own gralloc instead of minigbm/gbm which makes that almost impossible.  
And then those other projects again use OpenGL ES shaders/drawing commands or Vulkan swapchains to render pixels from the buffer onto egl native window surface which submits the buffer to buffer queue of Android Surface.  
This uses new SurfaceControl/SurfaceTransaction APIs (introduced in APIs 29, 33, 34 and 36) to submit GPU memory buffers to SurfaceFlinger for presentation, avoiding use of EGL/Vulkan.      
It uses a zero-copy rendering path - client texture is rendered onto a GPU memory buffer allocated by minigbm/gbm gralloc which is then submitted for presenting to display by SurfaceFlinger, supporting hardware overlays (direct scanout) and GPU sampling by Android.  
GPU acceleration for wlroots gles2/vulkan (either works) renderer is provided by mesa in termux (iris and Intel vulkan drivers are confirmed to work).   
Therefore performance logically should be within margin of error of the performance in the case of wayland applications running natively on desktop Linux on the exact same hardware.  
Furthermore, with this Vulkan 1.4 and desktop OpenGL 4.6 should work flawlessly on supported hardware even though Android itself does not support desktop OpenGL.

# How it works
* A service in the app receives IBinder objects from a bundle when it recieves an intent by Tinywl app_process, then it starts a new Activity and callbacks to TinywlMainService.cpp AIDL service (using binder) as soon as the surface is created. 
* [TermuxAm](https://github.com/termux/TermuxAm/) is modified and used as a module to launch the activity of our app from app_process with intent containing bundle with IBinder AIDL callback object obtained from AIBinder in C++ code.
* `tinywl` module has the code that runs in termux as a cli application in external process (app_process). It has an entry point like a standalone Java application called from cli with args. It takes care of launching the app and receiving binder from NDK AIDL service implementation in native code.
* tinywl.c/libtinywl.so built with the app is just a placeholder, libtinywl.so from https://github.com/Xtr126/tinywl-ANativeWindow is used instead.
* Termux is used since it handles the dependencies of wlroots/mesa/wayland for us.
* It has a wlr_allocator implementation that uses AHardwareBuffers internally, so the tinywl compositor internally uses minigbm gralloc allocated buffers.
* Each wayland client's texture is rendered onto a buffer, which is then presented on-screen using ASurfaceTransaction_setBuffer.

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
- [ ] LXC/chroot integration

# Build 
Must have app installed on-device for linking against shared libraries at build time.

    # Build in termux environment
    git clone https://github.com/Xtr126/tinywl-ANativeWindow
    cd tinywl-ANativeWindow
    make
    
## Building the app

    # Run this before gradle sync
    ./gradlew compileDebugAidl

    ./gradlew assembleDebug

# Usage
Install the Android app (only source code is available in this repo).  
Install wlroots and mesa packages from [Xtr126/termux-packages](https://github.com/Xtr126/termux-packages/releases/tag/wlroots-0.18).  
Then run the following command in tinywl-ANativeWindow directory (after building).

    sh start.sh 

# Acknowledgments

* [Termux App](https://github.com/termux/termux-app/)
* [TermuxAm](https://github.com/termux/TermuxAm/)
* [wlroots and tinywl](https://gitlab.freedesktop.org/wlroots/wlroots)
* https://github.com/termux/termux-packages/pull/19587
* [minigbm(cros_gralloc_handle.h)](http://android.googlesource.com/platform/external/minigbm/) 
* [gralloc_handle from libdrm for gbm gralloc](https://gitlab.freedesktop.org/mesa/libdrm) 

# What works now
28-07-2025: Finally vkcube renders at fluid 60fps without any color/image representation issues, after using `cros_gralloc_handle.h` C++ header from minigbm to extract pixel format, stride, offset, planes and other attributes from allocated AHardwareBuffer.  
29-08-2025: Multiple windows 
![IMG_20250829_122825_395](https://github.com/user-attachments/assets/12ac5d5f-ccee-4bd6-a725-cd28a1948280)
