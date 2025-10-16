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
    double servoRange = 220;

    double degreeCounts;
    double o = Constants.Physics.mass / Constants.Physics.xCoefficient;
    double w = Constants.Physics.speed / Constants.Physics.terminalVelocity;
    double z = Math.exp(-(Constants.Physics.gravity * Constants.Physics.targetYPosition / Math.pow(Constants.Physics.terminalVelocity, 2)));
    double lower = Math.asin(Math.sqrt((Math.pow(z, 2) - 1) / -Math.pow(w, 2)));
    double upper;
    double maxx = xPosition(lower);
    double minx;
    double lasttarget;

    public TurretSubsystem(HardwareMap hardwareMap) {
        this.wheel = hardwareMap.get(DcMotor.class, Constants.Hardware.turretWheelName);
        this.hood = hardwareMap.get(Servo.class, Constants.Hardware.turretHoodName);
        this.spinner = new MotorEx(hardwareMap, Constants.Hardware.turretSpinnerName, Motor.GoBILDA.BARE);
        degreeCounts = this.spinner.getCPR() / 360;

        if(0.9999999999999 / w < 1) upper = Math.asin(0.9999999999999 / w);
        else upper = Math.toRadians(89.9);
        minx = xPosition(upper);
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
    public double thetaEstimate(double target) {
        lasttarget = target;
        if(minx > target || maxx < target) return -1;

        double v = Constants.Physics.speed * Math.sin(Constants.Physics.theta0);
        double a;
        if(v > Constants.Physics.terminalVelocity) a = atanh((Constants.Physics.terminalVelocity - 0.001) / Constants.Physics.terminalVelocity);
        else a = atanh(v / Constants.Physics.terminalVelocity);
        double xGuess = xPosition(Constants.Physics.theta0);
        double thetaGuess = Constants.Physics.theta0;
        double thetanm1 = -1;
        double thetanm2 = -1;
        int cons = 0;
        double delta = xGuess - target;

        while(Math.abs(delta) > Constants.Physics.toleranceThreshold) {
            thetanm2 = thetanm1;
            thetanm1 = thetaGuess;
            delta = xGuess - target;
            double alpha = 1;
            double oldGuess = thetaGuess;
            thetaGuess = oldGuess - (delta / xPositionDTheta(oldGuess));
            while(!(lower < thetaGuess) || !(upper > thetaGuess)) {
                alpha /= 2;
                thetaGuess = oldGuess - alpha * (delta / xPositionDTheta(oldGuess));
            }
            if(thetaGuess - thetanm2 > 0.000001) cons += 1;
            if(cons == 2) {
                alpha /= 2;
                thetaGuess = oldGuess - alpha * (delta / xPositionDTheta(oldGuess));
                cons = 0;
            }

            v = Constants.Physics.speed * Math.sin(Constants.Physics.theta0);
            if(v > Constants.Physics.terminalVelocity) a = atanh((Constants.Physics.terminalVelocity - 0.001) / Constants.Physics.terminalVelocity);
            else a = atanh(v / Constants.Physics.terminalVelocity);
            xGuess = xPosition(thetaGuess);
        }

        return Math.toDegrees(thetaGuess);
    }

    public boolean aimTurret(double target, double bearing) {
        double theta = thetaEstimate(target);
        if(theta == -1) return false;
        moveHood(theta);
        //spinner.setTargetPosition((int) Math.round(bearing * degreeCounts));
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
        double q = -w * Math.cos(upper);
        double n = 1 - (Math.pow(w, 2) * Math.pow(Math.sin(upper), 2));
        double lhs = acosh(z / Math.sqrt(n));
        double rhs = atanh(w * Math.sin(upper));
        return new TurretDebug(
                minx,
                maxx,
                lasttarget
        );
    }

    void moveHood(double degrees) {
        double a = 0.000000435526;
        double b = 0.000334834;
        double c = 0.00986936;
        hood.setPosition(a * Math.pow(degrees, 2) + b * degrees + c);
    }

    public double hoodPosition() {
        return hood.getPosition();
    }

    public double hoodDegrees() {
        return hoodPosition() * servoRange;
    }
}
