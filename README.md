WIP
# Overview
Spawns Activities that,
1. On Activity create, takes ownership of the surface associated with the activity window and setups callbacks for surface created, changed, destroyed etc
2. When the Surface created callback is recieved, it sends the Surface object over AIDL to our code running in app_process in Termux
3. `tinywl` module in `app` contains code that runs in termux as a cli application in external process (app_process)
4. `tinywl` module has java code with entry point like a standalone Java application called from cli with args.
5. [TermuxAm](https://github.com/termux/TermuxAm/) was modifed and used as a module to launch the activity of our app from app_process with intent containing bundle with IBinder AIDL callback object
6. When the Activity recieves callback object, it calls as soon as the surface is created. Then, `tinywl` module calls our C/C++ code through JNI that creates ANativeWindow (NDK) from Surface and starts tinywl server using libtinywl_server.so 
* libtinywl_server.so compiled from tinywl_server_stub.c is just a stub library to satisfy linker without hacks, it should be replaced with libtinywl_server.so built from https://github.com/Xtr126/tinywl-ANativeWindow (build in Termux environment with make)
* It was decided to run part of the code in Termux because Termux takes care of the dependency hell for wlroots/mesa/wayland and provides a terminal to play around with wayland socket
