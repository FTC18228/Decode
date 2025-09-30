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

public class TurretSubsystem extends SubsystemBase {
    DcMotor wheel;
    Servo hood;
    MotorEx spinner;
    double servoRange = 75;

    double degreeCounts;
    public TurretSubsystem(HardwareMap hardwareMap) {
        this.wheel = hardwareMap.get(DcMotor.class, Constants.Hardware.turretWheelName);
        this.hood = hardwareMap.get(Servo.class, Constants.Hardware.turretHoodName);
        this.spinner = new MotorEx(hardwareMap, Constants.Hardware.turretSpinnerName, Motor.GoBILDA.BARE);
        degreeCounts = this.spinner.getCPR() / 360;
    }

    double yPosition(double x, Vector2d speed, double a) {
        double w = Math.pow(Constants.Physics.terminalVelocity, 2) / Constants.Physics.gravity;
        double z = Constants.Physics.mass * (Math.exp(x * Constants.Physics.xCoefficient / Constants.Physics.mass) - 1);
        double denom = Math.cosh(a);
        return w * Math.log(Math.cosh(a - (Constants.Physics.yCoefficient * z / speed.getX())) / denom);
    }
    double yPositionDTheta(double x, Vector2d speed, double a) {
        double w = Math.pow(Constants.Physics.terminalVelocity, 2) / Constants.Physics.gravity;
        double z = Constants.Physics.mass * (Math.exp(x * Constants.Physics.xCoefficient / Constants.Physics.mass) - 1);
        double arg = a - (Constants.Physics.yCoefficient * z / speed.getX());
        double coefficient = Constants.Physics.yCoefficient * w * z * speed.getY();
        return coefficient * Math.tanh(arg) / Math.pow(speed.getX(), 2);
    }

    /** @noinspection SpellCheckingInspection*/
    double atanh(double x) {
        return 0.5 * Math.log((1 + x) / (1 - x));
    }
    double acosh(double x) {return Math.log(x + Math.sqrt(Math.pow(x, 2) - 1));}
    double xEstimate(Vector2d speed, double a) {
        // I call it the definer because it defines the function
        // If the argument of the acosh is > 1, its real valued
        // Else its complex valued
        double definer = acosh(Math.exp(-(Constants.Physics.gravity * Constants.Physics.targetYPosition) / Math.pow(Constants.Physics.terminalVelocity, 2)) * Math.cosh(a));
        double coefficient = Constants.Physics.mass / Constants.Physics.xCoefficient;
        return coefficient * Math.log(Constants.Physics.xCoefficient * speed.getX() * (definer - a) / (-Constants.Physics.yCoefficient * Constants.Physics.mass));
    }

    //TODO: Fix up so it works :3
    double thetaEstimate(double target) {
        Vector2d speed = new Vector2d(Constants.Physics.speed * Math.cos(Constants.Physics.theta0), Constants.Physics.speed * Math.sin(Constants.Physics.theta0));
        double a;
        if (speed.getY() > Constants.Physics.terminalVelocity) a =  Constants.Physics.terminalVelocity - 0.001;
        else a = atanh(speed.getY() / Constants.Physics.terminalVelocity);
        double x0 = xEstimate(speed, a);
        Vector2d dGuess = position(x0, speed, a);
        double thetaGuess = Constants.Physics.theta0;

        while(dGuess.getX() - target > Constants.Physics.toleranceThreshold) {
            // Estimate
            double deltaD = dGuess.getX() - target;
            thetaGuess = thetaGuess - (deltaD / positionDTheta(x0, speed, a).getX());

            // Update
            speed = new Vector2d(Constants.Physics.speed * Math.cos(thetaGuess), Constants.Physics.speed * Math.sin(thetaGuess));
            if (speed.getY() > Constants.Physics.terminalVelocity) a =  Constants.Physics.terminalVelocity - 0.001;
            else a = atanh(speed.getY() / Constants.Physics.terminalVelocity);
            dGuess = position(x0, speed, a);
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
                position(x0, speed, a).getX()
        );
    }

    public void moveHood(double degrees) {
        hood.setPosition(degrees / servoRange);
    }
}
