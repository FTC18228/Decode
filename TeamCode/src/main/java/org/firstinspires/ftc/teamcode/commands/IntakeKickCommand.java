package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;

public class IntakeKickCommand extends CommandBase {
    IntakeSubsystem intakeSubsystem;
    boolean finished;
    public IntakeKickCommand(IntakeSubsystem intakeSubsystem) {
        this.intakeSubsystem = intakeSubsystem;
        this.finished = false;
    }

    @Override
    public void execute() {
        intakeSubsystem.loadArtefact();
        finished = true;
    }

    @Override
    public boolean isFinished() {
        return intakeSubsystem.isKickReady();
    }
}
