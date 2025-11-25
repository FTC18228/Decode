package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.auto.commands.FollowPathCommand;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;

@Configurable
@Autonomous(name = "PedroTestRedBack | 9 AUTO (New Paths)")
public class PedroTestRedBack9 extends CommandOpMode {

    private Follower follower;

    // Subsystems
    private IntakeSubsystem intakeSubsystem;
    private TurretSubsystem turretSubsystem;
    private int launchTime = 650;
    private int recoveryTime = 999;

    // New starting pose based on your exported path (matches MoveToShoot start)
    public static Pose startPose = new Pose(88.353, 6.346, Math.toRadians(90));

    // New paths
    private PathChain MoveToShoot;
    private PathChain GoToSpike3;
    private PathChain IntakeSpike3;
    private PathChain MoveToShoot2;   // renamed from duplicate "MoveToShoot"
    private PathChain MovetoSpike2;
    private PathChain CollectSpike2;
    private PathChain BackToShoot;

    @Override
    public void initialize() {

        intakeSubsystem = new IntakeSubsystem(hardwareMap, telemetry);
        turretSubsystem = new TurretSubsystem(hardwareMap, telemetry, false);

        intakeSubsystem.register();
        turretSubsystem.register();

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPose);

        // -------------------------
        // NEW PATH DEFINITIONS
        // -------------------------
        MoveToShoot = follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(88.353, 6.346),
                        new Pose(88.353, 11.346)))
                .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(60))
                .build();

        GoToSpike3 = follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(88.353, 11.346),
                        new Pose(108.603, 45.122)))
                .setLinearHeadingInterpolation(Math.toRadians(60), Math.toRadians(0))
                .build();

        IntakeSpike3 = follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(108.603, 45.122),
                        new Pose(145.308, 45.122)))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .build();

        MoveToShoot2 = follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(145.308, 45.122),
                        new Pose(75.661, 53.939),
                        new Pose(88.841, 89.085)))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(45))
                .build();

        MovetoSpike2 = follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(88.841, 89.085),
                        new Pose(101.044, 66.285)))
                .setLinearHeadingInterpolation(Math.toRadians(45), Math.toRadians(0))
                .build();

        CollectSpike2 = follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(101.044, 66.285),
                        new Pose(149.308, 66.285)))
                .setTangentHeadingInterpolation()
                .build();

        BackToShoot = follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(149.308, 66.285),
                        new Pose(120.661, 66.285),
                        new Pose(94.353, 112.085)))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(28))
                .build();

        // -------------------------
        // WAIT FOR START

        while (!isStarted() && !isStopRequested()) {
            telemetry.addLine("PedroTestRed | NEW PATHS Ready");
            telemetry.update();
        }

        // -------------------------
        // AUTO SEQUENCE
        // -------------------------
        CommandScheduler.getInstance().schedule(

                new SequentialCommandGroup(
                        new InstantCommand(() -> turretSubsystem.setTracking(false)),

                        // Preload spin-up
                        new InstantCommand(() -> {
                            intakeSubsystem.intakeOn();
                            turretSubsystem.stopTurret();
                        }),
                        new InstantCommand(() -> {turretSubsystem.setPreset(0.36, 1);}),

                        // ---------------- PRELOAD ----------------
                        new FollowPathCommand(follower, MoveToShoot, true),
                        new WaitCommand(2000),
                        new InstantCommand(() -> intakeSubsystem.visionlessStartWheel()),
                        new WaitCommand(launchTime),
                        new InstantCommand(() -> intakeSubsystem.visionlessStopWheel()),
                        new WaitCommand(recoveryTime),
                        new InstantCommand(() -> intakeSubsystem.visionlessStartWheel()),
                        new WaitCommand(launchTime),
                        new InstantCommand(() -> intakeSubsystem.visionlessStopWheel()),
                        new WaitCommand(recoveryTime),
                        new InstantCommand(() -> intakeSubsystem.visionlessStartWheel()),
                        new WaitCommand(launchTime),
                        new InstantCommand(() -> intakeSubsystem.visionlessStopWheel()),

                        new InstantCommand(() -> turretSubsystem.setTracking(true)),

                        // ---------------- SPIKE 3 ----------------
                        new FollowPathCommand(follower, GoToSpike3, true),
                        new ParallelCommandGroup(
                                new FollowPathCommand(follower, IntakeSpike3, true, 0.75),
                                new InstantCommand(() -> intakeSubsystem.intakeOn())
                        ),
                        new WaitCommand(750),
                        new InstantCommand(() -> {turretSubsystem.setPreset(0.69, .73);}),
                        // ---------------- SHOOT AGAIN ----------------
                        new FollowPathCommand(follower, MoveToShoot2, true),
                        new InstantCommand(() -> intakeSubsystem.visionlessStartWheel()),
                        new WaitCommand(launchTime),
                        new InstantCommand(() -> intakeSubsystem.visionlessStopWheel()),
                        new WaitCommand(recoveryTime),
                        new InstantCommand(() -> intakeSubsystem.visionlessStartWheel()),
                        new WaitCommand(launchTime),
                        new InstantCommand(() -> intakeSubsystem.visionlessStopWheel()),
                        new WaitCommand(recoveryTime),
                        new InstantCommand(() -> intakeSubsystem.visionlessStartWheel()),
                        new WaitCommand(launchTime),
                        new InstantCommand(() -> intakeSubsystem.visionlessStopWheel()),
                        new WaitCommand(recoveryTime),
                        new InstantCommand(() -> intakeSubsystem.visionlessStartWheel()),
                        new WaitCommand(launchTime),
                        new InstantCommand(() -> intakeSubsystem.visionlessStopWheel()),



                        // ---------------- SPIKE 2 ----------------
                        new FollowPathCommand(follower, MovetoSpike2, true),
                        new ParallelCommandGroup(
                                new FollowPathCommand(follower, CollectSpike2, true),
                                new InstantCommand(() -> intakeSubsystem.intakeOn())
                        ),
                        new WaitCommand(700),

                        // ---------------- FINAL SHOOT ----------------
                        new FollowPathCommand(follower, BackToShoot, true),
                        new InstantCommand(() -> intakeSubsystem.visionlessStartWheel()),
                        new WaitCommand(launchTime),
                        new InstantCommand(() -> intakeSubsystem.visionlessStopWheel()),
                        new WaitCommand(recoveryTime),
                        new InstantCommand(() -> intakeSubsystem.visionlessStartWheel()),
                        new WaitCommand(launchTime),
                        new InstantCommand(() -> intakeSubsystem.visionlessStopWheel()),
                        new WaitCommand(recoveryTime),
                        new InstantCommand(() -> intakeSubsystem.visionlessStartWheel()),
                        new WaitCommand(launchTime),
                        new InstantCommand(() -> intakeSubsystem.visionlessStopWheel()),
                        new WaitCommand(recoveryTime),

                        new InstantCommand(() -> intakeSubsystem.intakeOff()),
                        new InstantCommand(() -> turretSubsystem.setPreset(0.75, 0.0)),
                        new WaitCommand(200),

                        new InstantCommand(() -> telemetry.addLine("AUTO COMPLETE"))
                )
        );
    }
}
