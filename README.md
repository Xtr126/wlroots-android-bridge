# Why?
Whilst other projects for running wayland and X11 applications on Android already exist, this is built for efficiency.  
They use CPU write/read usage flags for buffers and workarounds to get around limitations of proprietary Android drivers. This keeps the buffer entirely in GPU for the whole process of rendering/compositing and presenting to display.  
It uses gralloc handle data type from minigbm/gbm gralloc headers to extract buffer attributes. Proprietary Android drivers may have their own gralloc instead of minigbm/gbm.  
And then those other projects again use OpenGL ES shaders/drawing commands or Vulkan swapchains to render pixels from the buffer onto egl native window surface which submits the buffer to buffer queue of Android Surface.  
This uses new special SurfaceControl APIs introduced in APIs 29, 33, 34 and 36 to submit GPU memory buffers to SurfaceFlinger for presentation, avoiding use of EGL/Vulkan.      
It uses a zero-copy rendering path - wlr_scene directly renders onto a GPU memory buffer allocated by minigbm/gbm gralloc which is then submitted for presenting to display by SurfaceFlinger, supporting hardware overlays (direct scanout) and GPU sampling by Android.  
GPU acceleration for wlroots gles2/vulkan renderer is provided by mesa in termux (iris and Intel vulkan drivers are confirmed to work).   
Therefore performance logically should be within margin of error of the performance in the case of wayland applications running natively on desktop Linux on the exact same hardware.  
Furthermore, with this Vulkan 1.4 and desktop OpenGL 4.6 should work flawlessly on supported hardware even though Android itself does not support OpenGL.

# How it works
* The Activity receives IBinder remote callback object from bundle when it is started by Tinywl.java, then it callbacks to Tinywl.java as soon as the surface is created. Then, Tinywl.java calls C/C++ code  in libtinywl.so through JNI (libtinywl is built from [tinywl-ANativeWindow](https://github.com/Xtr126/tinywl-ANativeWindow) which starts the tinywl server with a reference to (java)Surface passed through to native code. It creates NDK ANativeWindow from JNI Surface object.
* [TermuxAm](https://github.com/termux/TermuxAm/) is modified and used as a module to launch the activity of our app from app_process with intent containing bundle with IBinder AIDL callback object
* `tinywl` module has the code that runs in termux as a cli application in external process (app_process). It has an entry point like a standalone Java application called from cli with args. It takes care of launching the activity and passing the Surface to tinywl.
* tinywl.c/libtinywl.so built with the app is just a placeholder, libtinywl.so from https://github.com/Xtr126/tinywl-ANativeWindow is used instead.
* Termux is used since it handles the dependencies of wlroots/mesa/wayland for us.
  
# Build 
    # Build in termux environment
    git clone https://github.com/Xtr126/tinywl-ANativeWindow
    cd tinywl-ANativeWindow
    make
# Usage
Install the Android app (only source code is available in this repo).  
Install wlroots and mesa packages from [Xtr126/termux-packages](https://github.com/Xtr126/termux-packages/releases/tag/wlroots-0.18).  
Then run the following command in tinywl-ANativeWindow directory.

    /system/bin/app_process -Djava.library.path=./:/system/lib64 -Djava.class.path=$(pm path com.xtr.compound  | cut -d ':' -f 2) / com.xtr.compound.Tinywl 

# Acknowledgments

* [Termux App](https://github.com/termux/termux-app/)
* [TermuxAm](https://github.com/termux/TermuxAm/)
* [wlroots and tinywl](https://gitlab.freedesktop.org/wlroots/wlroots)
* https://github.com/termux/termux-packages/pull/19587
* [minigbm(cros_gralloc_handle.h)](http://android.googlesource.com/platform/external/minigbm/) 
* [gralloc_handle from libdrm for gbm gralloc](https://gitlab.freedesktop.org/mesa/libdrm) 

# What works now
28-07-2025: Finally vkcube renders at fluid 60fps without any color/image representation issues, after using `cros_gralloc_handle.h` C++ header from minigbm to extract pixel format, stride, offset, planes and other attributes from allocated AHardwareBuffer.
