package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;

public class OuttakeCommand extends CommandBase {
    IntakeSubsystem intakeSubsystem;
    public OuttakeCommand(IntakeSubsystem intakeSubsystem) {
        this.intakeSubsystem = intakeSubsystem;
    }

    @Override
    public void execute() {
        intakeSubsystem.outtake();
    }

    @Override
    public boolean isFinished() {
        return intakeSubsystem.isIntakeOn();
    }
}
