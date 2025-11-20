package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;

@Autonomous(name = "PedroTestRed", group = "Autonomous")
@Configurable
public class PedroTestRed extends OpMode {

    private TelemetryManager panelsTelemetry;
    public Follower follower;
    private int pathState = 0;
    private Paths paths;

    public IntakeSubsystem intakeSubsystem;
    public TurretSubsystem turretSubsystem;

    // Track currently active SequentialCommandGroup
    private SequentialCommandGroup activeCommand;

    @Override
    public void init() {
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(111.051, 134.237, Math.toRadians(0)));

        paths = new Paths(follower);

        intakeSubsystem = new IntakeSubsystem(hardwareMap, telemetry);
        turretSubsystem = new TurretSubsystem(hardwareMap, telemetry, false);

        CommandScheduler.getInstance().reset();

        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.update(telemetry);
    }

    @Override
    public void loop() {
        // Required for FTCLib commands to run
        CommandScheduler.getInstance().run();

        follower.update();
        runStateMachine();

        panelsTelemetry.debug("Path State", pathState);
        panelsTelemetry.debug("X", follower.getPose().getX());
        panelsTelemetry.debug("Y", follower.getPose().getY());
        panelsTelemetry.debug("Heading", follower.getPose().getHeading());
        panelsTelemetry.update(telemetry);
    }

    private void runStateMachine() {
        switch (pathState) {

            case 0:
                turretSubsystem.setPreset(0.65, 0.75);
                intakeSubsystem.intakeOn();
                follower.followPath(paths.MoveToLaunchPreload);
                pathState = 1;
                telemetry.addData("IT MOVEDDDDDDDD", null);
                telemetry.update();
                break;

            case 1:
                if (activeCommand == null) {
                    activeCommand = new SequentialCommandGroup(
                            new InstantCommand(() -> telemetry.addData("WHEELS SHOULD SPIN NOW", null)),
                            new InstantCommand(telemetry::update),
                            new InstantCommand(() -> intakeSubsystem.visionlessStartWheel()),
                            new WaitCommand(750),
                            new InstantCommand(() -> intakeSubsystem.visionlessStopWheel()),
                            new WaitCommand(1250),
                            new InstantCommand(() -> telemetry.addData("WHEELS SHOULD NOT SPIN NOW", null)),
                            new InstantCommand(telemetry::update)
                    );
                    CommandScheduler.getInstance().schedule(activeCommand);
                }

                // Wait until command finishes before advancing state
                if (activeCommand.isFinished()) {
                    activeCommand = null;
                    pathState = 2;
                }
                break;

            case 2:
                if (!follower.isBusy()) {
                    follower.followPath(paths.MoveToIntakeSpikeLine);
                    pathState = 3;
                }
                break;

            case 3:
                if (!follower.isBusy()) {
                    follower.followPath(paths.IntakeSpikeLine);
                    pathState = 4;
                }
                break;

            case 4:
                if (!follower.isBusy()) {
                    follower.followPath(paths.MoveToLaunchSpikeLine);
                    pathState = 5;
                }
                break;

            case 5:
                // Schedule shooting command only once
                if (activeCommand == null) {
                    activeCommand = new SequentialCommandGroup(
                            new InstantCommand(() -> intakeSubsystem.visionlessStartWheel()),
                            new WaitCommand(750),
                            new InstantCommand(() -> intakeSubsystem.visionlessStopWheel()),
                            new WaitCommand(1250)
                    );
                    CommandScheduler.getInstance().schedule(activeCommand);
                }

                if (activeCommand.isFinished()) {
                    activeCommand = null;
                    pathState = 6;
                }
                break;

            case 6:
                // done
                break;
        }
    }

    public static class Paths {

        public final PathChain MoveToLaunchPreload;
        public final PathChain MoveToIntakeSpikeLine;
        public final PathChain IntakeSpikeLine;
        public final PathChain MoveToLaunchSpikeLine;

        public Paths(Follower follower) {

            MoveToLaunchPreload = follower.pathBuilder()
                    .addPath(new BezierLine(
                            new Pose(111.051, 134.237),
                            new Pose(85.000, 134.237)))
                    .setConstantHeadingInterpolation(Math.toRadians(0))
                    .build();

            MoveToIntakeSpikeLine = follower.pathBuilder()
                    .addPath(new BezierLine(
                            new Pose(85.000, 134.237),
                            new Pose(99.580, 84.203)))
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                    .build();

            IntakeSpikeLine = follower.pathBuilder()
                    .addPath(new BezierLine(
                            new Pose(99.580, 84.203),
                            new Pose(128.000, 84.203)))
                    .setTangentHeadingInterpolation()
                    .build();

            MoveToLaunchSpikeLine = follower.pathBuilder()
                    .addPath(new BezierLine(
                            new Pose(128.000, 84.203),
                            new Pose(85.000, 134.237)))
                    .setConstantHeadingInterpolation(Math.toRadians(0))
                    .build();
        }
    }
}
