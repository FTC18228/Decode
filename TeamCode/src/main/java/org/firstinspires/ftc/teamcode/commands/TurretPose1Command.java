package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;
import org.firstinspires.ftc.teamcode.utils.Constants;

public class TurretPose1Command extends CommandBase {
    TurretSubsystem turretSubsystem;
    public TurretPose1Command(TurretSubsystem turretSubsystem) {
        this.turretSubsystem = turretSubsystem;
    }

    @Override
    public void execute() {
        turretSubsystem.setPreset(Constants.TurretPresets.getHoodPose(1), Constants.TurretPresets.getWheelSpeed(1));
    }

    @Override
    public boolean isFinished() {
        return turretSubsystem.isTurretMoving();
    }
}
