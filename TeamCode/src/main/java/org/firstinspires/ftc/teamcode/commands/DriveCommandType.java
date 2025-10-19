package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.util.JoystickValues;

import java.util.function.DoubleSupplier;

public interface DriveCommandType {
    void setJoystickValues(DoubleSupplier leftX, DoubleSupplier leftY, DoubleSupplier rightX, DoubleSupplier rightY);
    JoystickValues getJoystickValues();
    void drive();
}
