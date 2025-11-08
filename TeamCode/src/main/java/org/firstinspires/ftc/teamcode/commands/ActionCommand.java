package org.firstinspires.ftc.teamcode.commands;

import android.util.ArraySet;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.Subsystem;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Set;

public class ActionCommand implements Command { // Thanks 14380 for allowing this code to be stolen :) even tho you stole it from rr docs
    private final Action action;
    private final Set<Subsystem> requirements;
    private boolean finished = false;
    Telemetry telemetry;
    public ActionCommand(Action action, Telemetry telemetry) {
        this.action = action;
        this.requirements = new ArraySet<>();
        this.telemetry = telemetry;
    }
    public ActionCommand(Action action, Set<Subsystem> requirements, Telemetry telemetry) {
        this.action = action;
        this.requirements = requirements;
        this.telemetry = telemetry;
    }

    @Override
    public Set<Subsystem> getRequirements() {
        return requirements;
    }

    @Override
    public void execute() {
        telemetry.addLine("Action is running!!!");
        telemetry.update();
        TelemetryPacket packet = new TelemetryPacket();
        action.preview(packet.fieldOverlay());
        finished = !action.run(packet);
        FtcDashboard.getInstance().sendTelemetryPacket(packet);
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
}
