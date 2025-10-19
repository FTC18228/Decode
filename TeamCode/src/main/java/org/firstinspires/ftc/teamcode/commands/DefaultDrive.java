package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.JoystickValues;
import org.firstinspires.ftc.teamcode.util.Vector2DSupplier;
import org.firstinspires.ftc.teamcode.debug.DriveDebug;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;

import java.util.function.DoubleSupplier;

public class DefaultDrive extends CommandBase implements DriveCommandType{
    DriveSubsystem driveSubsystem;
    Vector2DSupplier leftStick;
    Vector2DSupplier rightStick;
    Telemetry telemetry;
    double speed;
    public DefaultDrive(DriveSubsystem driveSubsystem, DoubleSupplier leftX, DoubleSupplier leftY, DoubleSupplier rightX, Telemetry telemetry) {
        this.driveSubsystem = driveSubsystem;
        setJoystickValues(leftX, leftY, rightX, () -> {return 0;});
        addRequirements(driveSubsystem);
        this.telemetry = telemetry;
    }

    @Override
    public void setJoystickValues(DoubleSupplier leftX, DoubleSupplier leftY, DoubleSupplier rightX, DoubleSupplier rightY) {
        this.leftStick = new Vector2DSupplier(leftX, leftY);
        this.rightStick = new Vector2DSupplier(rightX, rightY);
    }

    @Override
    public JoystickValues getJoystickValues() {
        return new JoystickValues(
                leftStick.get().x,
                leftStick.get().y,
                rightStick.get().x,
                rightStick.get().y
        );
    }

    @Override
    public void drive() {
        JoystickValues values = getJoystickValues();
        DriveDebug debug = driveSubsystem.drive(values.leftX, values.leftY, values.rightX);
        debug.displayTelemetry(telemetry);
    }

    @Override
    public void execute() {
        drive();
    }
}
