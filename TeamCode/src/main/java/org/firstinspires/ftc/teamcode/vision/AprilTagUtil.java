package org.firstinspires.ftc.teamcode.vision;

import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

public class AprilTagUtil {
    static AprilTagProcessor.Builder processorBuilder;
    static AprilTagProcessor aprilTagProcessor;
    static VisionPortal.Builder visionBuilder;
    static VisionPortal visionPortal;
    static List<AprilTagDetection> detections;
    public static boolean initialized;

    private static class TagDistance {
        public double range;
        public double bearing;
        public TagDistance(double range, double bearing) {
            this.range = range;
            this.bearing = bearing;
        }
    }

    public static void initialize(HardwareMap hardwareMap) {
        processorBuilder = new AprilTagProcessor.Builder();
        processorBuilder.setDrawCubeProjection(true);
        processorBuilder.setNumThreads(6);
        processorBuilder.setOutputUnits(DistanceUnit.MM, AngleUnit.DEGREES);
        //processorBuilder.setCameraPose() //TODO Change this
        aprilTagProcessor = processorBuilder.build();

        visionBuilder.setCamera(hardwareMap.get(WebcamName.class, Constants.Hardware.webcamName));
        visionBuilder.addProcessor(aprilTagProcessor);
        visionBuilder.setShowStatsOverlay(true); //Change if we would rather hide this
        //visionBuilder.setCameraResolution(new Size(640, 480)) //TODO: Find ideal camera resolution
        visionBuilder.enableLiveView(true);
        visionPortal = visionBuilder.build();

        visionPortal.setProcessorEnabled(aprilTagProcessor, true);

        initialized = true;
    }

    public static boolean isInitialized() {
        return initialized;
    }

    public static void throwIfNotInit() {
        if(!isInitialized()) {
            throw new RuntimeException("Vision is not initialized"); // Does exactly what the method name is
        }
    }

    public static void detectTags() {
        detections = aprilTagProcessor.getDetections();
    }

    public static int[] getDetectedIDs() {
        int[] ret = new int[detections.size()];
        for(AprilTagDetection detection : detections) {
            ret[detections.indexOf(detection)] = detection.id;
        }
        return ret;
    }

    public static TagDistance getTagDistance(int tag) {
        for (AprilTagDetection detection : detections) {
            if(tag != detection.id) continue;
            return new TagDistance(detection.ftcPose.range, detection.ftcPose.bearing);
        }
        return new TagDistance(-1, -1);
    }
}
