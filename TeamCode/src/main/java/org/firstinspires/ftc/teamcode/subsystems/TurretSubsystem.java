package org.firstinspires.ftc.teamcode.subsystems;

import android.util.Size;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.utils.Constants;
import org.firstinspires.ftc.teamcode.debug.TurretDebug;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

public class TurretSubsystem extends SubsystemBase {
    DcMotor wheel;
    DcMotor turret;
    Servo hood;

    Telemetry telemetry;

    private AprilTagProcessor aprilTag;
    private VisionPortal visionPortal;

    private PIDController turretController;

    private boolean useRedTag = true;

    public TurretSubsystem(HardwareMap hardwareMap, Telemetry telemetry, boolean useRedTag) {
        wheel = hardwareMap.get(DcMotor.class, Constants.Hardware.turretWheelName);
        hood = hardwareMap.get(Servo.class, Constants.Hardware.turretHoodName);
        turret = hardwareMap.get(DcMotor.class, Constants.Hardware.turretMotorName);
        turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.telemetry = telemetry;

        this.useRedTag =useRedTag;

        aprilTag = new AprilTagProcessor.Builder()
                .build();

        VisionPortal.Builder builder = new VisionPortal.Builder();
        builder.setCameraResolution(new Size(640,480));
        builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
        builder.addProcessor(aprilTag);
        visionPortal = builder.build();

        turretController = new PIDController(0.03,0.001,0.001);
        turretController.setTolerance(0);
        turretController.setSetPoint(0);


    }

    public void setPreset(double servoPosition, double speed) {
        this.hood.setPosition(servoPosition);
        this.wheel.setPower(speed);
    }

    public void stopTurret() {
        wheel.setPower(0);
    }

    public void reverseTurret() {
        wheel.setPower(-0.2);
    }

    public boolean isTurretMoving() {
        return wheel.getPower() != 0;
    }

    public double hoodPosition() {
        return hood.getPosition();
    }

    private void processAprilTags(){
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();

        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) {

                if (!detection.metadata.name.contains("Obelisk")) {
                    if(useRedTag){
                        if(detection.id != 24){ //red not found
                            break;
                        }
                    }else{
                        if(detection.id != 20){ //blue not found
                            break;
                        }
                    }
                    telemetry.addLine(String.format("X %6.1f",
                            detection.center.x));

                    telemetry.addLine(String.format("X %6.1f",
                            detection.ftcPose.bearing));

                    telemetry.addData("Turret", turret.getCurrentPosition());


                    double power = turretController.calculate(detection.ftcPose.bearing);
                    telemetry.addData("power",power);
                    telemetry.update();
                    turret.setPower(power);

                }
            }
        }
        if(currentDetections.isEmpty()){
            turret.setPower(0);
        }
    }

    @Override
    public void periodic(){

        processAprilTags();
    }
}
