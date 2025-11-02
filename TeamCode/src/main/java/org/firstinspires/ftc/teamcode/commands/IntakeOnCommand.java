package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;

public class IntakeOnCommand extends CommandBase {
    IntakeSubsystem intakeSubsystem;
    public IntakeOnCommand(IntakeSubsystem intakeSubsystem) {
        this.intakeSubsystem = intakeSubsystem;
    }

    @Override
    public void execute() {
        intakeSubsystem.intakeOn();
    }

    @Override
    public boolean isFinished() {
        return intakeSubsystem.isIntakeOn();
    }
}
