package org.firstinspires.ftc.teamcode.commands;

import java.util.function.DoubleSupplier;

public interface DriveCommandType {
    public void setJoystickValues(DoubleSupplier leftX, DoubleSupplier leftY, DoubleSupplier rightX, DoubleSupplier rightY);
}
