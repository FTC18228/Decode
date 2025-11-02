package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.utils.Constants;

public class IntakeSubsystem extends SubsystemBase {
    // IM SCARED IM SCARED IM SCARED IM SCAREDDDDDDDDDDDDDDDDDDDDDDDD
    DcMotor intakeMotor;
    DcMotor loadingMotor;
    Servo gate;

    RevColorSensorV3 sensor1;
    RevColorSensorV3 sensor2;
    ElapsedTime motorTimer;
    ElapsedTime sensorTimer;
    boolean kickReady;

    Telemetry telemetry;
    public IntakeSubsystem(HardwareMap hardwareMap, Telemetry telemetry) {
        intakeMotor = hardwareMap.get(DcMotor.class, Constants.Hardware.intakeMotorName);
        loadingMotor = hardwareMap.get(DcMotor.class, Constants.Hardware.intakeLoaderName);
        gate = hardwareMap.get(Servo.class, Constants.Hardware.intakeGateName);
        sensor1 = hardwareMap.get(RevColorSensorV3.class, Constants.Hardware.intakeSensor1Name);
        sensor2 = hardwareMap.get(RevColorSensorV3.class, Constants.Hardware.intakeSensor2Name);
        motorTimer = new ElapsedTime();
        sensorTimer = new ElapsedTime();
        this.telemetry = telemetry;
    }

    public void intakeOn() {
        intakeMotor.setPower(-1);
    }
    public void intakeOff() {
        intakeMotor.setPower(0);
    }

    public void outtake() {
        intakeMotor.setPower(1);
    }

    public void loadArtefact() {
        /*kickReady = false;
        double light = sensor.getLightDetected();

        gate.setPosition(0);
        while(light < 0.1); //TODO: Make good

        gate.setPosition(1);
        motorTimer.reset();
        while(motorTimer.milliseconds() < 1000);

        loadingMotor.setPower(1);
        while(light >= 0.1);

        loadingMotor.setPower(0);
        kickReady = true;*/
    }

    public boolean isSensor1Active(){
        return sensor1.getDistance(DistanceUnit.MM) < 30;
    }

    public boolean isSensor2Active(){
        return sensor2.getDistance(DistanceUnit.MM) < 30;
    }

    public void closeGate(){
        gate.setPosition(1.0);
    }

    public void openGate(){
        gate.setPosition(0.0);
    }

    /*public Command IntakeArtifact(){
        return new SequentialCommandGroup(

                new ConditionalCommand(
                        new InstantCommand(() -> openGate()),
                        new InstantCommand(() -> closeGate()),
                        this::isGateActive
                ),
                new InstantCommand(() -> intakeOn())

        );
    }*/

    public boolean isGateActive(){
        if(isSensor1Active() || isSensor2Active()){
            return true;
        }
        return false;
    }

    @Override
    public void periodic(){

        if(sensorTimer.milliseconds() > 250) {
            if (isGateActive()) {
                closeGate();
            } else {
                openGate();
            }
            telemetry.addData("S1", sensor1.getDistance(DistanceUnit.MM));
            telemetry.addData("S2", sensor2.getDistance(DistanceUnit.MM));
            telemetry.update();
            sensorTimer.reset();
        }
    }

    public void visionlessStartWheel() {
        loadingMotor.setPower(-1);
    }

    public void visionlessStopWheel() {
        loadingMotor.setPower(0);
    }

    public boolean isKickReady() {return kickReady;}
    public boolean isIntakeOn() {return intakeMotor.getPower() != 0;}
}
