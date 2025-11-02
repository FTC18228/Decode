package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.utils.Constants;
import org.firstinspires.ftc.teamcode.debug.TurretDebug;

public class TurretSubsystem extends SubsystemBase {
    DcMotor wheel;
    Servo hood;

    public TurretSubsystem(HardwareMap hardwareMap) {
        wheel = hardwareMap.get(DcMotor.class, Constants.Hardware.turretWheelName);
        hood = hardwareMap.get(Servo.class, Constants.Hardware.turretHoodName);
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
}
