package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.debug.DriveDebug;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.utils.JoystickValues;
import org.firstinspires.ftc.teamcode.utils.Vector2DSupplier;

import java.util.function.DoubleSupplier;

public class IntakeDebugCommand extends CommandBase{
    Telemetry telemetry;
    IntakeSubsystem intakeSubsystem;
    public IntakeDebugCommand(IntakeSubsystem intakeSubsystem, Telemetry telemetry) {
        addRequirements(intakeSubsystem);
        this.intakeSubsystem = intakeSubsystem;
        this.telemetry = telemetry;
    }

    @Override
    public void execute() {
        intakeSubsystem.getDebug().displayTelemetry(telemetry);
    }
}
