package org.firstinspires.ftc.teamcode.commands;

import com.acmerobotics.roadrunner.Vector2d;
import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.Vector2DSupplier;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

public class DefaultDrive extends CommandBase implements DriveCommandType{
    DriveSubsystem driveSubsystem;
    Vector2DSupplier leftStick;
    Vector2DSupplier rightStick;
    double speed;
    public DefaultDrive(DriveSubsystem driveSubsystem, DoubleSupplier leftX, DoubleSupplier leftY, DoubleSupplier rightX) {
        this.driveSubsystem = driveSubsystem;
        setJoystickValues(leftX, leftY, rightX, () -> {return 0;});
    }

    @Override
    public void setJoystickValues(DoubleSupplier leftX, DoubleSupplier leftY, DoubleSupplier rightX, DoubleSupplier rightY) {
        this.leftStick = new Vector2DSupplier(leftX, leftY);
        this.rightStick = new Vector2DSupplier(rightX, rightY);
    }
}
