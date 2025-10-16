package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Constants;

public class IntakeSubsystem extends SubsystemBase {
    // IM SCARED IM SCARED IM SCARED IM SCAREDDDDDDDDDDDDDDDDDDDDDDDD
    DcMotor intakeMotor;
    DcMotor loadingMotor;
    Servo gate;
    DistanceSensor sensor;
    ElapsedTime motorTimer;
    public IntakeSubsystem(HardwareMap hardwareMap) {
        intakeMotor = hardwareMap.get(DcMotor.class, Constants.Hardware.intakeMotorName);
        loadingMotor = hardwareMap.get(DcMotor.class, Constants.Hardware.intakeLoaderName);
        gate = hardwareMap.get(Servo.class, Constants.Hardware.intakeGateName);
        sensor = hardwareMap.get(DistanceSensor.class, Constants.Hardware.intakeSensorName);
        motorTimer = new ElapsedTime();

    }

    public void intakeOn() {
        intakeMotor.setPower(1);
    }
    public void intakeOff() {
        intakeMotor.setPower(0);
    }

    public void outtake() {
        intakeMotor.setPower(-1);
    }

    public void loadArtefact() {
        gate.setPosition(0);
        while(sensor.getDistance(DistanceUnit.MM) > 10);
        gate.setPosition(1);
        motorTimer.reset();
        while(motorTimer.milliseconds() < 1000);
        loadingMotor.setPower(1);
        while(sensor.getDistance(DistanceUnit.MM) != DistanceSensor.distanceOutOfRange);
        loadingMotor.setPower(0);
    }
}
