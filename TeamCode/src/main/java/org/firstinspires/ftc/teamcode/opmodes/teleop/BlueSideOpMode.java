package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.roadrunner.Pose2d;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.DefaultDrive;
import org.firstinspires.ftc.teamcode.commands.IntakeKickCommand;
import org.firstinspires.ftc.teamcode.commands.IntakeOffCommand;
import org.firstinspires.ftc.teamcode.commands.IntakeOnCommand;
import org.firstinspires.ftc.teamcode.commands.OuttakeCommand;
import org.firstinspires.ftc.teamcode.utils.TeleOpCommon;

@TeleOp(name="Blue side OpMode")
public class BlueSideOpMode extends CommandOpMode {
    @Override
    public void initialize() {
        TeleOpCommon.hardResetInstance();
        TeleOpCommon common = TeleOpCommon.getInstance(hardwareMap, new Pose2d(0, 0, 0), gamepad1, telemetry);
        common.resetInstanceState(); //Ensure its reset - sometimes the instance is preserved between running the the init function
    }


}
