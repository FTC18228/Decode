package org.firstinspires.ftc.teamcode.util;
public class Constants {
    /** @noinspection SpellCheckingInspection*/
    public static class Physics {
        // Can be changed!!!
        static final double rotatorEfficiency = 0.25; //TODO: Find an optimal value for this based off tests
        static final double motorRPM = 2800; // Self explanatory
        public static final double toleranceThreshold = 0.05; // Tolerance that the turret will get to(in meters)
        public static final double theta0 = 55; // Initial guess of theta(in degrees)

        // Don't change(unless you know what you're doing)
        static final double latitude = -27.3428554; // Useless V1
        static final int altitude = 10; // Useless V2
        static final int temperature = 20; // Useless V3
        public static final double mass = 0.074842741; // Mass of the ball
        static final double earthRadius = 6371000; // Take a guess as to what this does
        static final double omega = 0.00007292; // I don't know what this does, I just know its needed
        public static final double gravity = (9.80665 - (earthRadius * Math.pow(omega, 2) * Math.pow(Math.cos(Math.toRadians(latitude)), 2))) * Math.pow(earthRadius / (earthRadius + latitude), 2); // Acceleration due to gravity
        static final double R = 287.05; // Specific gas constant for air
        static final double L = -0.0065; // :person_shrugging:
        static final double P = 101325 * Math.pow(1 + -(L * altitude) / 288.15, -gravity * R * L); // Absolute pressure
        public static final double speed = rotatorEfficiency * motorRPM * 2 * 0.076 * Math.PI / 60; // Estimated launch speed of the ball
        static final double surfaceArea = (Math.pow(6.35, 2) * Math.PI) / 10000; // Surface area of the ball
        static final double dragCoefficient = 0.5; // Estimate of the drag coefficient of the ball in air
        static final double T = temperature + 273.15; // Temperature in kelvin... worse then c*lcius
        static final double pressure = P / (R * T); // Air pressure constant
        public static final double terminalVelocity = Math.sqrt((2 * mass * gravity) / (pressure * surfaceArea * dragCoefficient)); // Ball terminal velocity with gravity
        public static final double yCoefficient = gravity / terminalVelocity; // Coefficient of the y position function
        public static final double xCoefficient = mass * gravity / Math.pow(terminalVelocity, 2); // Coefficient of the x position function
    }

    public static class Hardware {
        public static final String leftTurretWheelName = "leftWheel";
        public static final String rightTurretWheelName = "leftWheel";
        public static final String leftTurretPivotName = "leftPivot";
        public static final String rightTurretPivotName = "rightPivot";
        public static final String turretSpinnerName = "turretSpinner";
        public static double turretDistanceFromCamera = 0;
        public static final double turretSpinnerRadius = 3;
        public static final String webcamName = "Webcam 1";
    }
}