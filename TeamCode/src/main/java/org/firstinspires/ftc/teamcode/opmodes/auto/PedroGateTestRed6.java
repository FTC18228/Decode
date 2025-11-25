package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.arcrobotics.ftclib.command.*;
import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.*;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.auto.commands.FollowPathCommand;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;

@Configurable
@Autonomous(name = "PedroGateTestRed | 6")
public class PedroGateTestRed6 extends CommandOpMode {

    private Follower follower;

    // Subsystems
    private IntakeSubsystem intakeSubsystem;
    private TurretSubsystem turretSubsystem;

    private int launchTime = 699;
    private int recoveryTime = 999;

    // Starting pose (same as your paths)
    public static Pose startPose = new Pose(111.051, 134.237, Math.toRadians(0));

    // ---- NEW PATHS BASED ON YOUR NEW ORDER ----
    private PathChain moveToLaunchPreload;
    private PathChain moveToIntakeSpikeLine;
    private PathChain intakeSpikeLine;
    private PathChain funPath;
    private PathChain backToShoot;

    @Override
    public void initialize() {

        // ---- Subsystems ----
        intakeSubsystem = new IntakeSubsystem(hardwareMap, telemetry);
        turretSubsystem = new TurretSubsystem(hardwareMap, telemetry, false);

        intakeSubsystem.register();
        turretSubsystem.register();

        // ---- Follower ----
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPose);


        // ---- PATH DEFINITIONS (MATCHING YOUR PROVIDED PATH FILE) ----

        moveToLaunchPreload = follower
                .pathBuilder()
                .addPath(new BezierLine(
                        new Pose(111.051, 134.237),
                        new Pose(85.000, 134.237)))
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();

        moveToIntakeSpikeLine = follower
                .pathBuilder()
                .addPath(new BezierLine(
                        new Pose(85.000, 134.237),
                        new Pose(99.580, 86.000)))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .build();

        intakeSpikeLine = follower
                .pathBuilder()
                .addPath(new BezierLine(
                        new Pose(99.580, 86.000),
                        new Pose(128.000, 86.000)))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .build();

        funPath = follower
                .pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(128.000, 86.000),
                        new Pose(73.220, 67.851),
                        new Pose(125, 76.88135593220339)))
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();

        backToShoot = follower
                .pathBuilder()
                .addPath(new BezierLine(
                        new Pose(125, 76.88135593220339),
                        new Pose(84.936, 134.237)))
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();


        // ---- WAIT FOR START ----
        while (!isStarted() && !isStopRequested()) {
            telemetry.addLine("PedroTestRed Ready");
            telemetry.update();
        }


        // ---- MAIN AUTO (NOW EXECUTES THE NEW PATH ORDER) ----
        CommandScheduler.getInstance().schedule(

                new SequentialCommandGroup(

                        // PRELOAD SHOOTING PREP
                        new InstantCommand(() -> {
                            turretSubsystem.setPreset(0.77, 0.71);
                            intakeSubsystem.intakeOn();
                        }),

                        // #1 MoveToLaunchPreload
                        new FollowPathCommand(follower, moveToLaunchPreload, true),

                        new WaitCommand(1000),
                        new InstantCommand(intakeSubsystem::visionlessStartWheel),
                        new WaitCommand(launchTime),
                        new InstantCommand(intakeSubsystem::visionlessStopWheel),
                        new WaitCommand(recoveryTime),

                        new InstantCommand(intakeSubsystem::visionlessStartWheel),
                        new WaitCommand(launchTime),
                        new InstantCommand(intakeSubsystem::visionlessStopWheel),
                        new WaitCommand(recoveryTime),

                        new InstantCommand(intakeSubsystem::visionlessStartWheel),
                        new WaitCommand(launchTime),
                        new InstantCommand(intakeSubsystem::visionlessStopWheel),
                        new WaitCommand(recoveryTime),


                        // #2 MoveToIntakeSpikeLine
                        new FollowPathCommand(follower, moveToIntakeSpikeLine, true),

                        // #3 IntakeSpikeLine
                        new ParallelCommandGroup(
                                new FollowPathCommand(follower, intakeSpikeLine, true, 0.75),
                                new InstantCommand(intakeSubsystem::intakeOn)
                        ),
                        new WaitCommand(750),


                        // #4 Fun (Bezier curve)
                        new ParallelCommandGroup(
                                new FollowPathCommand(follower, funPath, true, 0.7),
                                new InstantCommand(intakeSubsystem::intakeOn)
                        ),
                        new WaitCommand(1500),


                        // #5 BackToShoot
                        new FollowPathCommand(follower, backToShoot, true, 0.9),


                        // FINAL SHOOTING CYCLE
                        new InstantCommand(intakeSubsystem::intakeOn),
                        new WaitCommand(3000),
                        new WaitCommand(1000),
                        new InstantCommand(intakeSubsystem::visionlessStartWheel),
                        new WaitCommand(launchTime),
                        new InstantCommand(intakeSubsystem::visionlessStopWheel),
                        new WaitCommand(recoveryTime),

                        new InstantCommand(intakeSubsystem::visionlessStartWheel),
                        new WaitCommand(launchTime),
                        new InstantCommand(intakeSubsystem::visionlessStopWheel),
                        new WaitCommand(recoveryTime),

                        new InstantCommand(intakeSubsystem::visionlessStartWheel),
                        new WaitCommand(launchTime),
                        new InstantCommand(intakeSubsystem::visionlessStopWheel),

                        new WaitCommand(1500),

                        new InstantCommand(intakeSubsystem::intakeOff),
                        new InstantCommand(() -> turretSubsystem.setPreset(0.75, 0.0)),

                        new WaitCommand(200),
                        new InstantCommand(() -> telemetry.addLine("Auto Complete"))
                )
        );
    }
}
