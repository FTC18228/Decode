package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.VisionSubsystem;

public class TurretShootCommand extends CommandBase {
    TurretSubsystem turretSubsystem;
    VisionSubsystem visionSubsystem;
    boolean hasFailed = false;
    public TurretShootCommand(TurretSubsystem turretSubsystem, VisionSubsystem visionSubsystem) {
        this.turretSubsystem = turretSubsystem;
        this.visionSubsystem = visionSubsystem;
    }

    @Override
    public void execute() {
        double distance = visionSubsystem.getDistanceToTarget();
        double bearing = visionSubsystem.getBearingToTarget();
        if(visionSubsystem.isInvalidDistance(distance)) hasFailed = true;

        boolean aimed = turretSubsystem.aimTurret(distance, bearing);
        if(!aimed) hasFailed = true;
        turretSubsystem.fireTurret();

    }

    @Override
    public boolean isFinished() {
        return turretSubsystem.isTurretMoving() || hasFailed;
    }
}
