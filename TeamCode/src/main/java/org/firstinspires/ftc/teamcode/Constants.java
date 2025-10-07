package org.firstinspires.ftc.teamcode;
public class Constants {
    public static class Physics {
        // Can be changed!!!
        static double rotatorEfficiency = 0.3; //TODO: Find an optimal value for this based off tests
        static double motorRPM = 6000; // Self explanatory
        public static double toleranceThreshold = 0.000001; // Tolerance that the turret will get to(in meters)
        public static double theta0 = Math.toRadians(20); // Initial guess of theta(in degrees)

        // Don't change(unless you know what you're doing)
        static double latitude = -27.3428554; // Useless V1
        static int altitude = 10; // Useless V2
        static int temperature = 20; // Useless V3
        public static double mass = 0.074842741; // Mass of the ball
        static double earthRadius = 6371000; // Take a guess as to what this does
        static double omega = 0.00007292; // I don't know what this does, I just know its needed
        public static double gravity = (9.80665 - (earthRadius * Math.pow(omega, 2) * Math.pow(Math.cos(Math.toRadians(latitude)), 2))) * Math.pow(earthRadius / (earthRadius + altitude), 2); // Acceleration due to gravity
        static double R = 287.05; // Specific gas constant for air
        static double L = -0.0065; // :person_shrugging:
        static double P = 101325 * Math.pow(1 -(L * altitude) / 288.15, -gravity / (R * L)); // Absolute pressure
        public static double speed = rotatorEfficiency * motorRPM * 2 * 0.076 * Math.PI / 60; // Estimated launch speed of the ball
        static double surfaceArea = (Math.pow(6.35, 2) * Math.PI) / 10000; // Surface area of the ball
        static double dragCoefficient = 0.5; // Estimate of the drag coefficient of the ball in air
        static double T = temperature + 273.15; // Temperature in kelvin... worse then c*lcius
        static double pressure = P / (R * T); // Air pressure constant
        public static double terminalVelocity = Math.sqrt((2 * mass * gravity) / (pressure * surfaceArea * dragCoefficient)); // Ball terminal velocity with gravity
        public static double yCoefficient = gravity / terminalVelocity; // Coefficient of the y position function
        public static double xCoefficient = mass * gravity / Math.pow(terminalVelocity, 2); // Coefficient of the x position function
        public static double targetYPosition = 1.1;
    }

    public static class Hardware {
        public static String turretWheelName = "launch";
        public static String turretSpinnerName = "spinner";
        public static String turretHoodName = "hood";
        public static double turretDistanceFromCamera = 0;
        public static double turretSpinnerRadius = 167.0 / 2;
        public static String webcamName = "Webcam 1";
    }
}