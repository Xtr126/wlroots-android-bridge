/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.hardware.input.common;

/**
 * Constants that identify each individual axis of a motion event.
 * Each value represents a bit position. The user is expected to manually shift
 * to the desired position (e.g., 1 << Axis.X) when reading or writing these
 * from a bitfield manually.
 */
//@VintfStability
@Backing(type="int")
enum Axis {
    /**
     * Axis constant: X axis of a motion event.
     *
     * - For a touch screen, reports the absolute X screen position of the center of
     * the touch contact area.  The units are display pixels.
     * - For a touch pad, reports the absolute X surface position of the center of the touch
     * contact area. The units are device-dependent.
     * - For a mouse, reports the absolute X screen position of the mouse pointer.
     * The units are display pixels.
     * - For a trackball, reports the relative horizontal displacement of the trackball.
     * The value is normalized to a range from -1.0 (left) to 1.0 (right).
     * - For a joystick, reports the absolute X position of the joystick.
     * The value is normalized to a range from -1.0 (left) to 1.0 (right).
     */
    X = 0,
    /**
     * Axis constant: Y axis of a motion event.
     *
     * - For a touch screen, reports the absolute Y screen position of the center of
     * the touch contact area.  The units are display pixels.
     * - For a touch pad, reports the absolute Y surface position of the center of the touch
     * contact area. The units are device-dependent.
     * - For a mouse, reports the absolute Y screen position of the mouse pointer.
     * The units are display pixels.
     * - For a trackball, reports the relative vertical displacement of the trackball.
     * The value is normalized to a range from -1.0 (up) to 1.0 (down).
     * - For a joystick, reports the absolute Y position of the joystick.
     * The value is normalized to a range from -1.0 (up or far) to 1.0 (down or near).
     */
    Y = 1,
    /**
     * Axis constant: Pressure axis of a motion event.
     *
     * - For a touch screen or touch pad, reports the approximate pressure applied to the surface
     * by a finger or other tool.  The value is normalized to a range from
     * 0 (no pressure at all) to 1 (normal pressure), although values higher than 1
     * may be generated depending on the calibration of the input device.
     * - For a trackball, the value is set to 1 if the trackball button is pressed
     * or 0 otherwise.
     * - For a mouse, the value is set to 1 if the primary mouse button is pressed
     * or 0 otherwise.
     */
    PRESSURE = 2,
    /**
     * Axis constant: Size axis of a motion event.
     *
     * - For a touch screen or touch pad, reports the approximate size of the contact area in
     * relation to the maximum detectable size for the device.  The value is normalized
     * to a range from 0 (smallest detectable size) to 1 (largest detectable size),
     * although it is not a linear scale. This value is of limited use.
     * To obtain calibrated size information, see
     * {@link TOUCH_MAJOR} or {@link TOOL_MAJOR}.
     */
    SIZE = 3,
    /**
     * Axis constant: TouchMajor axis of a motion event.
     *
     * - For a touch screen, reports the length of the major axis of an ellipse that
     * represents the touch area at the point of contact.
     * The units are display pixels.
     * - For a touch pad, reports the length of the major axis of an ellipse that
     * represents the touch area at the point of contact.
     * The units are device-dependent.
     */
    TOUCH_MAJOR = 4,
    /**
     * Axis constant: TouchMinor axis of a motion event.
     *
     * - For a touch screen, reports the length of the minor axis of an ellipse that
     * represents the touch area at the point of contact.
     * The units are display pixels.
     * - For a touch pad, reports the length of the minor axis of an ellipse that
     * represents the touch area at the point of contact.
     * The units are device-dependent.
     *
     * When the touch is circular, the major and minor axis lengths will be equal to one another.
     */
    TOUCH_MINOR = 5,
    /**
     * Axis constant: ToolMajor axis of a motion event.
     *
     * - For a touch screen, reports the length of the major axis of an ellipse that
     * represents the size of the approaching finger or tool used to make contact.
     * - For a touch pad, reports the length of the major axis of an ellipse that
     * represents the size of the approaching finger or tool used to make contact.
     * The units are device-dependent.
     *
     * When the touch is circular, the major and minor axis lengths will be equal to one another.
     *
     * The tool size may be larger than the touch size since the tool may not be fully
     * in contact with the touch sensor.
     */
    TOOL_MAJOR = 6,
    /**
     * Axis constant: ToolMinor axis of a motion event.
     *
     * - For a touch screen, reports the length of the minor axis of an ellipse that
     * represents the size of the approaching finger or tool used to make contact.
     * - For a touch pad, reports the length of the minor axis of an ellipse that
     * represents the size of the approaching finger or tool used to make contact.
     * The units are device-dependent.
     *
     * When the touch is circular, the major and minor axis lengths will be equal to one another.
     *
     * The tool size may be larger than the touch size since the tool may not be fully
     * in contact with the touch sensor.
     */
    TOOL_MINOR = 7,
    /**
     * Axis constant: Orientation axis of a motion event.
     *
     * - For a touch screen or touch pad, reports the orientation of the finger
     * or tool in radians relative to the vertical plane of the device.
     * An angle of 0 radians indicates that the major axis of contact is oriented
     * upwards, is perfectly circular or is of unknown orientation.  A positive angle
     * indicates that the major axis of contact is oriented to the right.  A negative angle
     * indicates that the major axis of contact is oriented to the left.
     * The full range is from -PI/2 radians (finger pointing fully left) to PI/2 radians
     * (finger pointing fully right).
     * - For a stylus, the orientation indicates the direction in which the stylus
     * is pointing in relation to the vertical axis of the current orientation of the screen.
     * The range is from -PI radians to PI radians, where 0 is pointing up,
     * -PI/2 radians is pointing left, -PI or PI radians is pointing down, and PI/2 radians
     * is pointing right.  See also {@link TILT}.
     */
    ORIENTATION = 8,
    /**
     * Axis constant: Vertical Scroll axis of a motion event.
     *
     * - For a mouse, reports the relative movement of the vertical scroll wheel.
     * The value is normalized to a range from -1.0 (down) to 1.0 (up).
     *
     * The framework may use this axis to scroll views vertically.
     */
    VSCROLL = 9,
    /**
     * Axis constant: Horizontal Scroll axis of a motion event.
     *
     * - For a mouse, reports the relative movement of the horizontal scroll wheel.
     * The value is normalized to a range from -1.0 (left) to 1.0 (right).
     *
     * The framework may use this axis to scroll views horizontally.
     */
    HSCROLL = 10,
    /**
     * Axis constant: Z axis of a motion event.
     *
     * - For a joystick, reports the absolute Z position of the joystick.
     * The value is normalized to a range from -1.0 (high) to 1.0 (low).
     * <em>On game pads with two analog joysticks, this axis is often reinterpreted
     * to report the absolute X position of the second joystick instead.</em>
     */
    Z = 11,
    /**
     * Axis constant: X Rotation axis of a motion event.
     *
     * - For a joystick, reports the absolute rotation angle about the X axis.
     * The value is normalized to a range from -1.0 (counter-clockwise) to 1.0 (clockwise).
     */
    RX = 12,
    /**
     * Axis constant: Y Rotation axis of a motion event.
     *
     * - For a joystick, reports the absolute rotation angle about the Y axis.
     * The value is normalized to a range from -1.0 (counter-clockwise) to 1.0 (clockwise).
     */
    RY = 13,
    /**
     * Axis constant: Z Rotation axis of a motion event.
     *
     * - For a joystick, reports the absolute rotation angle about the Z axis.
     * The value is normalized to a range from -1.0 (counter-clockwise) to 1.0 (clockwise).
     * On game pads with two analog joysticks, this axis is often reinterpreted
     * to report the absolute Y position of the second joystick instead.
     */
    RZ = 14,
    /**
     * Axis constant: Hat X axis of a motion event.
     *
     * - For a joystick, reports the absolute X position of the directional hat control.
     * The value is normalized to a range from -1.0 (left) to 1.0 (right).
     */
    HAT_X = 15,
    /**
     * Axis constant: Hat Y axis of a motion event.
     *
     * - For a joystick, reports the absolute Y position of the directional hat control.
     * The value is normalized to a range from -1.0 (up) to 1.0 (down).
     */
    HAT_Y = 16,
    /**
     * Axis constant: Left Trigger axis of a motion event.
     *
     * - For a joystick, reports the absolute position of the left trigger control.
     * The value is normalized to a range from 0.0 (released) to 1.0 (fully pressed).
     */
    LTRIGGER = 17,
    /**
     * Axis constant: Right Trigger axis of a motion event.
     *
     * - For a joystick, reports the absolute position of the right trigger control.
     * The value is normalized to a range from 0.0 (released) to 1.0 (fully pressed).
     */
    RTRIGGER = 18,
    /**
     * Axis constant: Throttle axis of a motion event.
     *
     * - For a joystick, reports the absolute position of the throttle control.
     * The value is normalized to a range from 0.0 (fully open) to 1.0 (fully closed).
     */
    THROTTLE = 19,
    /**
     * Axis constant: Rudder axis of a motion event.
     *
     * - For a joystick, reports the absolute position of the rudder control.
     * The value is normalized to a range from -1.0 (turn left) to 1.0 (turn right).
     */
    RUDDER = 20,
    /**
     * Axis constant: Wheel axis of a motion event.
     *
     * - For a joystick, reports the absolute position of the steering wheel control.
     * The value is normalized to a range from -1.0 (turn left) to 1.0 (turn right).
     */
    WHEEL = 21,
    /**
     * Axis constant: Gas axis of a motion event.
     *
     * - For a joystick, reports the absolute position of the gas (accelerator) control.
     * The value is normalized to a range from 0.0 (no acceleration)
     * to 1.0 (maximum acceleration).
     */
    GAS = 22,
    /**
     * Axis constant: Brake axis of a motion event.
     *
     * - For a joystick, reports the absolute position of the brake control.
     * The value is normalized to a range from 0.0 (no braking) to 1.0 (maximum braking).
     */
    BRAKE = 23,
    /**
     * Axis constant: Distance axis of a motion event.
     *
     * - For a stylus, reports the distance of the stylus from the screen.
     * A value of 0.0 indicates direct contact and larger values indicate increasing
     * distance from the surface.
     */
    DISTANCE = 24,
    /**
     * Axis constant: Tilt axis of a motion event.
     *
     * - For a stylus, reports the tilt angle of the stylus in radians where
     * 0 radians indicates that the stylus is being held perpendicular to the
     * surface, and PI/2 radians indicates that the stylus is being held flat
     * against the surface.
     */
    TILT = 25,
    /**
     * Axis constant:  Generic scroll axis of a motion event.
     *
     * - This is used for scroll axis motion events that can't be classified as strictly
     *   vertical or horizontal. The movement of a rotating scroller is an example of this.
     */
    SCROLL = 26,
    /**
     * Axis constant: The movement of x position of a motion event.
     *
     * - For a mouse, reports a difference of x position between the previous position.
     * This is useful when pointer is captured, in that case the mouse pointer doesn't
     * change the location but this axis reports the difference which allows the app
     * to see how the mouse is moved.
     */
    RELATIVE_X = 27,
    /**
     * Axis constant: The movement of y position of a motion event.
     *
     * Same as {@link RELATIVE_X}, but for y position.
     */
    RELATIVE_Y = 28,
    /**
     * Axis constant: Generic 1 axis of a motion event.
     * The interpretation of a generic axis is device-specific.
     */
    GENERIC_1 = 32,
    /**
     * Axis constant: Generic 2 axis of a motion event.
     * The interpretation of a generic axis is device-specific.
     */
    GENERIC_2 = 33,
    /**
     * Axis constant: Generic 3 axis of a motion event.
     * The interpretation of a generic axis is device-specific.
     */
    GENERIC_3 = 34,
    /**
     * Axis constant: Generic 4 axis of a motion event.
     * The interpretation of a generic axis is device-specific.
     */
    GENERIC_4 = 35,
    /**
     * Axis constant: Generic 5 axis of a motion event.
     * The interpretation of a generic axis is device-specific.
     */
    GENERIC_5 = 36,
    /**
     * Axis constant: Generic 6 axis of a motion event.
     * The interpretation of a generic axis is device-specific.
     */
    GENERIC_6 = 37,
    /**
     * Axis constant: Generic 7 axis of a motion event.
     * The interpretation of a generic axis is device-specific.
     */
    GENERIC_7 = 38,
    /**
     * Axis constant: Generic 8 axis of a motion event.
     * The interpretation of a generic axis is device-specific.
     */
    GENERIC_8 = 39,
    /**
     * Axis constant: Generic 9 axis of a motion event.
     * The interpretation of a generic axis is device-specific.
     */
    GENERIC_9 = 40,
    /**
     * Axis constant: Generic 10 axis of a motion event.
     * The interpretation of a generic axis is device-specific.
     */
    GENERIC_10 = 41,
    /**
     * Axis constant: Generic 11 axis of a motion event.
     * The interpretation of a generic axis is device-specific.
     */
    GENERIC_11 = 42,
    /**
     * Axis constant: Generic 12 axis of a motion event.
     * The interpretation of a generic axis is device-specific.
     */
    GENERIC_12 = 43,
    /**
     * Axis constant: Generic 13 axis of a motion event.
     * The interpretation of a generic axis is device-specific.
     */
    GENERIC_13 = 44,
    /**
     * Axis constant: Generic 14 axis of a motion event.
     * The interpretation of a generic axis is device-specific.
     */
    GENERIC_14 = 45,
    /**
     * Axis constant: Generic 15 axis of a motion event.
     * The interpretation of a generic axis is device-specific.
     */
    GENERIC_15 = 46,
    /**
     * Axis constant: Generic 16 axis of a motion event.
     * The interpretation of a generic axis is device-specific.
     */
    GENERIC_16 = 47,
}
