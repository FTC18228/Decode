package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.geometry.Vector2d;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.debug.TurretDebug;

import java.util.concurrent.Phaser;

public class TurretSubsystem extends SubsystemBase {
    DcMotor wheel;
    Servo hood;
    MotorEx spinner;
    double servoRange = 75;

    double degreeCounts;
    double o = Constants.Physics.mass / Constants.Physics.xCoefficient;
    double w = Constants.Physics.speed / Constants.Physics.terminalVelocity;
    double z = Math.exp(-(Constants.Physics.gravity * Constants.Physics.targetYPosition / Math.pow(Constants.Physics.terminalVelocity, 2)));
    public TurretSubsystem(HardwareMap hardwareMap) {
        this.wheel = hardwareMap.get(DcMotor.class, Constants.Hardware.turretWheelName);
        this.hood = hardwareMap.get(Servo.class, Constants.Hardware.turretHoodName);
        this.spinner = new MotorEx(hardwareMap, Constants.Hardware.turretSpinnerName, Motor.GoBILDA.BARE);
        degreeCounts = this.spinner.getCPR() / 360;
    }

    double xPosition(double t) {
        double q = -w * Math.cos(t);
        double n = 1 - (Math.pow(w, 2) * Math.pow(Math.sin(t), 2));
        double lhs = acosh(z / Math.sqrt(n));
        double rhs = atanh(w * Math.sin(t));
        return o * Math.log1p(q * (lhs - rhs));
    }
    double xPositionDTheta(double t) {
        double q = -w * Math.cos(t);
        double n = 1 - (Math.pow(w, 2) * Math.pow(Math.sin(t), 2));
        double nlhs = (o * z * Math.pow(w, 2) * Math.sin(2 * t)) / (2 * n * Math.sqrt(Math.pow(z, 2) - n));
        double nrhs = w * o * Math.cos(t) / (-n);
        double dlhs = acosh(z / Math.sqrt(n));
        double drhs = atanh(w * Math.sin(t));
        return (nlhs + nrhs) / (dlhs - drhs + (1 / q));
    }

    /** @noinspection SpellCheckingInspection*/
    double atanh(double x) {
        return 0.5 * Math.log((1 + x) / (1 - x));
    }
    double acosh(double x) {return Math.log(x + Math.sqrt(Math.pow(x, 2) - 1));}
    double getTMeasure(double a) {
        double decider = Math.exp(-Constants.Physics.gravity * Constants.Physics.targetYPosition / Math.pow(Constants.Physics.terminalVelocity, 2));
        return (acosh(decider * Math.cosh(a)) - a) / -Constants.Physics.yCoefficient;
    }
    //TODO: Fix up so it works :3
    double thetaEstimate(double target) {
        Vector2d speed = new Vector2d(Constants.Physics.speed * Math.cos(Constants.Physics.theta0), Constants.Physics.speed * Math.sin(Constants.Physics.theta0));
        double a;
        if (speed.getY() > Constants.Physics.terminalVelocity) a =  Constants.Physics.terminalVelocity - 0.001;
        else a = atanh(speed.getY() / Constants.Physics.terminalVelocity);
        double t0 = getTMeasure(a);
        double xGuess = xPosition(t0);
        double thetaGuess = Constants.Physics.theta0;

        while(xGuess- target > Constants.Physics.toleranceThreshold) {
            // Estimate
            double deltaD = xGuess - target;
            thetaGuess = thetaGuess - (deltaD / xPositionDTheta(t0));

            // Update
            speed = new Vector2d(Constants.Physics.speed * Math.cos(thetaGuess), Constants.Physics.speed * Math.sin(thetaGuess));
            if (speed.getY() > Constants.Physics.terminalVelocity) a =  Constants.Physics.terminalVelocity - 0.001;
            else a = atanh(speed.getY() / Constants.Physics.terminalVelocity);
            t0 = getTMeasure(a);
            xGuess = xPosition(t0);
        }

        return thetaGuess;
    }

    public boolean aimTurret(double target, double bearing) {
        double theta = thetaEstimate(target);
        moveHood(theta);
        spinner.setTargetPosition((int) Math.round(bearing * degreeCounts));
        return true; //TODO: Return some value to ensure its aimed and the target is reachable
    }

    public void fireTurret() {
        wheel.setPower(1);
    }

    public void stopTurret() {
        wheel.setPower(0);
    }

    public boolean isTurretMoving() {
        return wheel.getPower() != 0;
    }

    public TurretDebug getTurretInfo() {
        Vector2d speed = new Vector2d(Constants.Physics.speed * Math.cos(Constants.Physics.theta0), Constants.Physics.speed * Math.sin(Constants.Physics.theta0));
        double a;
        if (speed.getY() > Constants.Physics.terminalVelocity) a =  Constants.Physics.terminalVelocity - 0.001;
        else a = atanh(speed.getY() / Constants.Physics.terminalVelocity);
        double x0 = 2 * a / Constants.Physics.yCoefficient;
        return new TurretDebug(
                hood.getPosition(),
                hood.getPosition() * servoRange,
                0
        );
    }

    public void moveHood(double degrees) {
        hood.setPosition(degrees / servoRange);
    }
}
