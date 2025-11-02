package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;
import org.firstinspires.ftc.teamcode.utils.Constants;

public class TurretPose3Command extends CommandBase {
    TurretSubsystem turretSubsystem;
    public TurretPose3Command(TurretSubsystem turretSubsystem) {
        this.turretSubsystem = turretSubsystem;
    }

    @Override
    public void execute() {
        turretSubsystem.setPreset(Constants.TurretPresets.getHoodPose(3), Constants.TurretPresets.getWheelSpeed(3));
    }

    @Override
    public boolean isFinished() {
        return turretSubsystem.isTurretMoving();
    }
}
