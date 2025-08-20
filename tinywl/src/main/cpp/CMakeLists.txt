
# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html.
# For more examples on how to use CMake, see https://github.com/android/ndk-samples.

# Sets the minimum CMake version required for this project.
cmake_minimum_required(VERSION 3.22.1)

# Declares the project name. The project name can be accessed via ${ PROJECT_NAME},
# Since this is the top level CMakeLists.txt, the project name is also accessible
# with ${CMAKE_PROJECT_NAME} (both CMake variables are in-sync within the top level
# build script scope).
project("tinywl")


add_library(aidl_shared SHARED
        # List C/C++ source files with relative paths to this CMakeLists.txt.
        # Generated with
        # cd tinywl/build/generated/aidl_source_output_dir/debug/out/ndk
        # find -name '*cpp' | sed 's|^\./|${aidl_source_output_dir}/|'
        ${aidl_source_output_dir}/com/android/server/inputflinger/KeyEventAction.cpp
        ${aidl_source_output_dir}/com/android/server/inputflinger/IInputFlingerRust.cpp
        ${aidl_source_output_dir}/com/android/server/inputflinger/InputFilterConfiguration.cpp
        ${aidl_source_output_dir}/com/android/server/inputflinger/DeviceInfo.cpp
        ${aidl_source_output_dir}/com/android/server/inputflinger/IInputThread.cpp
        ${aidl_source_output_dir}/com/android/server/inputflinger/IInputFilter.cpp
        ${aidl_source_output_dir}/com/android/server/inputflinger/KeyEvent.cpp
        ${aidl_source_output_dir}/com/xtr/tinywl/XdgTopLevel.cpp
        ${aidl_source_output_dir}/com/xtr/tinywl/ITinywlXdgTopLevel.cpp
        ${aidl_source_output_dir}/com/xtr/tinywl/ITinywlCallback.cpp
        ${aidl_source_output_dir}/android/hardware/input/common/Source.cpp
        ${aidl_source_output_dir}/android/hardware/input/common/Axis.cpp
        ${aidl_source_output_dir}/android/hardware/input/common/Meta.cpp
        ${aidl_source_output_dir}/android/hardware/input/common/ToolType.cpp
        ${aidl_source_output_dir}/android/hardware/input/common/EdgeFlag.cpp
        ${aidl_source_output_dir}/android/hardware/input/common/MotionEvent.cpp
        ${aidl_source_output_dir}/android/hardware/input/common/PointerCoords.cpp
        ${aidl_source_output_dir}/android/hardware/input/common/Flag.cpp
        ${aidl_source_output_dir}/android/hardware/input/common/Classification.cpp
        ${aidl_source_output_dir}/android/hardware/input/common/Action.cpp
        ${aidl_source_output_dir}/android/hardware/input/common/Button.cpp
        ${aidl_source_output_dir}/android/hardware/input/common/PolicyFlag.cpp
        ${aidl_source_output_dir}/android/hardware/input/common/SourceClass.cpp
        ${aidl_source_output_dir}/android/hardware/input/common/VideoFrame.cpp
        ${aidl_source_output_dir}/android/hardware/input/common/PointerProperties.cpp
        ${aidl_source_output_dir}/tinywl/ITinywlInput.cpp
)

target_include_directories(aidl_shared PUBLIC
        ${sdk_optional_libbinder_ndk_cpp}
        ${aidl_source_output_dir})


target_link_libraries(aidl_shared
        binder_ndk
        nativewindow)
# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.
#
# In this top level CMakeLists.txt, ${CMAKE_PROJECT_NAME} is used to define
# the target library name; in the sub-module's CMakeLists.txt, ${PROJECT_NAME}
# is preferred for the same purpose.
#
# In order to load a library into your app from Java/Kotlin, you must call
# System.loadLibrary() and pass the name of the library defined here;
# for GameActivity/NativeActivity derived applications, the same library name must be
# used in the AndroidManifest.xml file.
add_library(${CMAKE_PROJECT_NAME} SHARED
    # List C/C++ source files with relative paths to this CMakeLists.txt.
        tinywl.cpp)

# Specifies libraries CMake should link to your target library. You
# can link libraries from various origins, such as libraries defined in this
# build script, prebuilt third-party libraries, or Android system libraries.
target_link_libraries(${CMAKE_PROJECT_NAME}
    # List libraries link to the target library
    )

add_library(inputqueue SHARED
        # List C/C++ source files with relative paths to this CMakeLists.txt.
        input-queue.cpp
       )

target_link_libraries(inputqueue
        android
        aidl_shared
        )