package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;
import org.firstinspires.ftc.teamcode.utils.Constants;

public class TurretPose0Command extends CommandBase {
    TurretSubsystem turretSubsystem;
    public TurretPose0Command(TurretSubsystem turretSubsystem) {
        this.turretSubsystem = turretSubsystem;
    }

    @Override
    public void execute() {
        turretSubsystem.setPreset(Constants.TurretPresets.getHoodPose(0), Constants.TurretPresets.getWheelSpeed(0));
    }

    @Override
    public boolean isFinished() {
        return turretSubsystem.isTurretMoving();
    }
}
