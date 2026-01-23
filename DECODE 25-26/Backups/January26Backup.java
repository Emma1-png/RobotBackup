package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name = "MalteserJunior2 (Blocks to Java)", group = "21126 25-26")
public class MalteserJunior2 extends LinearOpMode {

  private DistanceSensor TwoMeterSensor;
  private Servo IntakeMotor;
  private CRServo FireMotor;
  private DcMotor FrontLeft;
  private DcMotor BackLeft;
  private DcMotor FrontRight;
  private DcMotor BackRight;

  /**
   * This sample contains the bare minimum Blocks for any regular OpMode. The 3 blue
   * Comment Blocks show where to place Initialization code (runs once, after touching the
   * DS INIT button, and before touching the DS Start arrow), Run code (runs once, after
   * touching Start), and Loop code (runs repeatedly while the OpMode is active, namely not
   * Stopped).
   */
  @Override
  public void runOpMode() {
    double Speed;
    String CanLaunch;
    int f;
    float y;
    float x;
    float rx;

    TwoMeterSensor = hardwareMap.get(DistanceSensor.class, "TwoMeterSensor");
    IntakeMotor = hardwareMap.get(Servo.class, "IntakeMotor");
    FireMotor = hardwareMap.get(CRServo.class, "FireMotor");
    FrontLeft = hardwareMap.get(DcMotor.class, "FrontLeft");
    BackLeft = hardwareMap.get(DcMotor.class, "BackLeft");
    FrontRight = hardwareMap.get(DcMotor.class, "FrontRight");
    BackRight = hardwareMap.get(DcMotor.class, "BackRight");

    Speed = 1;
    CanLaunch = "False";
    f = 0;
    waitForStart();
    if (opModeIsActive()) {
      while (opModeIsActive()) {
        if (gamepad1.dpad_up && TwoMeterSensor.getDistance(DistanceUnit.CM) > 25) {
          // Up Position
          IntakeMotor.setPosition(0);
        } else {
          // Down Position
          IntakeMotor.setPosition(1);
        }
        // This is for the safety
        if (gamepad1.left_trigger == 1) {
          CanLaunch = "True";
          Speed = 0.5;
          telemetry.addLine("Safety Off! You Can Fire.");
        } else {
          CanLaunch = "False";
          Speed = 1;
          telemetry.addLine("Safety On! You Can't Fire.");
        }
        // This is for the firing
        if (gamepad1.right_trigger == 1 && CanLaunch.equals("True") && IntakeMotor.getPosition() == 0) {
          telemetry.addLine("Fired!");
          FireMotor.setPower(-1);
        } else if (gamepad1.right_trigger == 1 && CanLaunch.equals("False") && IntakeMotor.getPosition() == 1) {
          telemetry.addLine("Can't Fire. Safety Off. What is the position of safety Bar?");
          FireMotor.setPower(0);
        } else {
          FireMotor.setPower(0);
        }
        // Drive
        // D-Pad - Driving Forward and Back & Rotate, L1&R1, Strafing
        y = -gamepad1.left_stick_y;
        x = gamepad1.left_stick_x;
        rx = gamepad1.right_stick_x;
        if (TwoMeterSensor.getDistance(DistanceUnit.CM) < 25) {
          Speed = 0.2;
          IntakeMotor.setPosition(1);
          telemetry.addLine("<.25m - Powering Motor On");
        } else if (TwoMeterSensor.getDistance(DistanceUnit.CM) < 200) {
          Speed = 0.5;
          IntakeMotor.setPosition(0);
          telemetry.addLine("<2m");
        } else if (TwoMeterSensor.getDistance(DistanceUnit.CM) > 200) {
          Speed = 1;
          IntakeMotor.setPosition(0);
          telemetry.addLine(">2m");
        }
        FrontLeft.setPower((y + x + rx) * Speed);
        // This is inverted for some reason.
        BackLeft.setPower(-(((y - x) + rx) * Speed));
        FrontRight.setPower(((y - x) - rx) * Speed);
        BackRight.setPower(((y + x) - rx) * Speed);
        f = gamepad1.right_bumper;
        telemetry.addLine("" + TwoMeterSensor.getDistance(DistanceUnit.CM) + Speed);
        telemetry.update();
      }
    }
  }
}
