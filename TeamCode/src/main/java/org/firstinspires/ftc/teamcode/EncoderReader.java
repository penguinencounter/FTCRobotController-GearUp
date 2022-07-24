package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.SharedHardware.*;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.lib.Recorder;

@TeleOp
public class EncoderReader extends LinearOpMode {
//    CPR 8192, 35mm encoder wheels
    double lastUpdated = 0;
    boolean lastA = false;
    double finalTime = 0;
    double POWER = .5;
    Recorder pos1R = new Recorder();
    Recorder pos2R = new Recorder();
    @Override
    public void runOpMode() {
        prepareHardware(hardwareMap);
        FtcDashboard dashboard = FtcDashboard.getInstance();
        telemetry = dashboard.getTelemetry();

        waitForStart();
        runtime.reset();
        double target = MovementLibrary.getTargetEncoder(1.0, POWER);
//        double target = 20000;
        while (opModeIsActive()) {
            int pos2 = -encoderLeft.getCurrentPosition();
            int pos = encoderRight.getCurrentPosition();
            double now = runtime.seconds();
            pos1R.put(now, pos);
            pos2R.put(now, pos2);

            telemetry.addData("pos1R", pos1R.combinedStateString());
            telemetry.addData("pos2R", pos2R.combinedStateString());
            telemetry.addData("pos", pos);
            telemetry.addData("pos2", pos2);
            telemetry.addData("target", target);
            telemetry.addData("time", now);
            if (pos < target) {
                frontRight.setPower(POWER);
                frontLeft.setPower(POWER);
                rearRight.setPower(POWER);
                rearLeft.setPower(POWER);
            } else {
                frontRight.setPower(0.0);
                frontLeft.setPower(0.0);
                rearRight.setPower(0.0);
                rearLeft.setPower(0.0);
                finalTime = now;

                // Prepare to stop
                pos1R.finalize(now);
                pos2R.finalize(now);
                break;
            }
            lastA = gamepad1.a;
            telemetry.update();
        }
        while (opModeIsActive()) {
            int pos2 = -encoderLeft.getCurrentPosition();
            int pos = encoderRight.getCurrentPosition();
            double now = runtime.seconds();
            pos1R.put(now, pos);
            pos2R.put(now, pos2);

            telemetry.addData("pos1R", pos1R.combinedStateString());
            telemetry.addData("pos2R", pos2R.combinedStateString());
            telemetry.addData("pos", pos);
            telemetry.addData("pos2", pos2);
            telemetry.addData("target", target);
            telemetry.addData("time", finalTime);
            telemetry.update();

            pos1R.writeIfClosed(now, "pos1.csv");
            pos2R.writeIfClosed(now, "pos2.csv");
        }
        pos1R.writeOnce("pos1.csv");
        pos2R.writeOnce("pos2.csv");
    }
}
