WIP
# Overview
* The Activity receives IBinder remote callback object from bundle when it is started by Tinywl.java, then it callbacks to Tinywl.java as soon as the surface is created. Then, Tinywl.java calls C/C++ code  in libtinywl.so through JNI built from [tinywl-ANativeWindow](https://github.com/Xtr126/tinywl-ANativeWindow) which starts the tinywl server with a reference to Surface passed through. tinywl-ANativeWindow converts java surface to ANativeWindow.
* [TermuxAm](https://github.com/termux/TermuxAm/) is modified and used as a module to launch the activity of our app from app_process with intent containing bundle with IBinder AIDL callback object
* `tinywl` module has the code that runs in termux as a cli application in external process (app_process). It has an entry point like a standalone Java application called from cli with args. It takes care of launching the activity and passing the Surface to tinywl.
* tinywl.c/libtinywl.so built with the app is just a stub library, libtinywl.so from https://github.com/Xtr126/tinywl-ANativeWindow is used instead.
* Termux is used since it handles the dependencies of wlroots/mesa/wayland for us.
# Build 
    # Build in termux environment
    git clone https://github.com/Xtr126/tinywl-ANativeWindow
    cd tinywl-ANativeWindow
    make
    # Running tinywl
    /system/bin/app_process -Djava.library.path=./:/system/lib64 -Djava.class.path=$(pm path com.xtr.compound  | cut -d ':' -f 2) / com.xtr.compound.Tinywl 