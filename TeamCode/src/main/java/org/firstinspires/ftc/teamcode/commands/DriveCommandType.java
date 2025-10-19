package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.util.JoystickValues;

import java.util.function.DoubleSupplier;

public interface DriveCommandType {
    public void setJoystickValues(DoubleSupplier leftX, DoubleSupplier leftY, DoubleSupplier rightX, DoubleSupplier rightY);
    public JoystickValues getJoystickValues();
    public void drive();
}
