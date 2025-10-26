package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.debug.IntakeDebug;
import org.firstinspires.ftc.teamcode.utils.Constants;

public class IntakeSubsystem extends SubsystemBase {
    // IM SCARED IM SCARED IM SCARED IM SCAREDDDDDDDDDDDDDDDDDDDDDDDD
    DcMotor intakeMotor;
    DcMotor loadingMotor;
    Servo gate;
    OpticalDistanceSensor sensor;
    ElapsedTime motorTimer;
    boolean kickReady;
    public IntakeSubsystem(HardwareMap hardwareMap) {
        intakeMotor = hardwareMap.get(DcMotor.class, Constants.Hardware.intakeMotorName);
        loadingMotor = hardwareMap.get(DcMotor.class, Constants.Hardware.intakeLoaderName);
        gate = hardwareMap.get(Servo.class, Constants.Hardware.intakeGateName);
        sensor = hardwareMap.get(OpticalDistanceSensor.class, Constants.Hardware.intakeSensorName);
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
    public IntakeDebug getDebug() {
        double light = sensor.getLightDetected();
        return new IntakeDebug(
                light
        );
    }

    public void loadArtefact() {
        kickReady = false;
        double light = sensor.getLightDetected();

        gate.setPosition(0);
        while(light < 0.1); //TODO: Make good

        gate.setPosition(1);
        motorTimer.reset();
        while(motorTimer.milliseconds() < 1000);

        loadingMotor.setPower(1);
        while(light >= 0.1);

        loadingMotor.setPower(0);
        kickReady = true;
    }

    public boolean isKickReady() {return kickReady;}
}
