package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

@Autonomous(name = "JuniorAutonomous (Blocks to Java)", preselectTeleOp = "Malteser Junior")
public class JuniorAutonomous extends LinearOpMode {

  private DcMotor FrontRight;
  private DcMotor BackLeft;
  private DcMotor BackRight;
  private DcMotor FrontLeft;

  boolean USE_WEBCAM;
  AprilTagProcessor myAprilTagProcessor;
  VisionPortal myVisionPortal;

  /**
   * This OpMode illustrates the basics of AprilTag recognition and pose estimation.
   *
   * For an introduction to AprilTags, see the FTC-DOCS link below:
   * https://ftc-docs.firstinspires.org/en/latest/apriltag/vision_portal/apriltag_intro/apriltag-intro.html
   *
   * In this sample, any visible tag ID will be detected and
   * displayed, but only tags that are included in the default
   * "TagLibrary" will have their position and orientation information displayed. This default TagLibrary contains
   * the current Season's AprilTags and a small set of "test Tags" in the high number range.
   *
   * When an AprilTag in the TagLibrary is detected, the SDK provides
   * location and orientation of the tag, relative to the camera.
   * This information is provided in the "ftcPose" member of the returned
   * "detection", and is explained in the ftc-docs page linked below.
   * https://ftc-docs.firstinspires.org/apriltag-detection-values
   */
  @Override
  public void runOpMode() {
    FrontRight = hardwareMap.get(DcMotor.class, "FrontRight");
    BackLeft = hardwareMap.get(DcMotor.class, "BackLeft");
    BackRight = hardwareMap.get(DcMotor.class, "BackRight");
    FrontLeft = hardwareMap.get(DcMotor.class, "FrontLeft");

    USE_WEBCAM = true;
    // Initialize AprilTag before waitForStart.
    initAprilTag();
    // Wait for the match to begin.
    telemetry.addData("DS preview on/off", "3 dots, Camera Stream");
    telemetry.addData(">", "Touch START to start OpMode");
    telemetry.update();
    waitForStart();
    if (opModeIsActive()) {
      // Put run blocks here.
      while (opModeIsActive()) {
        // Put loop blocks here.
        telemetryAprilTag();
        // Push telemetry to the Driver Station.
        telemetry.update();
        if (gamepad1.dpad_down) {
          // Temporarily stop the streaming session. This can save CPU
          // resources, with the ability to resume quickly when needed.
          myVisionPortal.stopStreaming();
        } else if (gamepad1.dpad_up) {
          // Resume the streaming session if previously stopped.
          myVisionPortal.resumeStreaming();
        }
        // Share the CPU.
        sleep(20);
      }
    }
  }

  /**
   * Initialize AprilTag Detection.
   */
  private void initAprilTag() {
    AprilTagProcessor.Builder myAprilTagProcessorBuilder;
    VisionPortal.Builder myVisionPortalBuilder;

    // First, create an AprilTagProcessor.Builder.
    myAprilTagProcessorBuilder = new AprilTagProcessor.Builder();
    // Create an AprilTagProcessor by calling build.
    myAprilTagProcessor = myAprilTagProcessorBuilder.build();
    // Next, create a VisionPortal.Builder and set attributes related to the camera.
    myVisionPortalBuilder = new VisionPortal.Builder();
    if (USE_WEBCAM) {
      // Use a webcam.
      myVisionPortalBuilder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
    } else {
      // Use the device's back camera.
      myVisionPortalBuilder.setCamera(BuiltinCameraDirection.FRONT);
    }
    // Add myAprilTagProcessor to the VisionPortal.Builder.
    myVisionPortalBuilder.addProcessor(myAprilTagProcessor);
    // Create a VisionPortal by calling build.
    myVisionPortal = myVisionPortalBuilder.build();
  }

  /**
   * Display info (using telemetry) for a recognized AprilTag.
   */
  private void telemetryAprilTag() {
    List<AprilTagDetection> myAprilTagDetections;
    AprilTagDetection myAprilTagDetection;
    int MotorPower;
    double DistX;
    double DistY;

    // Get a list of AprilTag detections.
    myAprilTagDetections = myAprilTagProcessor.getDetections();
    telemetry.addData("# AprilTags Detected", JavaUtil.listLength(myAprilTagDetections));
    // Iterate through list and call a function to display info for each recognized AprilTag.
    for (AprilTagDetection myAprilTagDetection_item : myAprilTagDetections) {
      myAprilTagDetection = myAprilTagDetection_item;
      // Display info about the detection.
      telemetry.addLine("");
      if (myAprilTagDetection.metadata != null) {
        telemetry.addLine("==== (ID " + myAprilTagDetection.id + ") " + myAprilTagDetection.metadata.name);
        telemetry.addLine("XYZ " + JavaUtil.formatNumber(myAprilTagDetection.ftcPose.x, 6, 1) + " " + JavaUtil.formatNumber(myAprilTagDetection.ftcPose.y, 6, 1) + " " + JavaUtil.formatNumber(myAprilTagDetection.ftcPose.z, 6, 1) + "  (inch)");
        telemetry.addLine("PRY " + JavaUtil.formatNumber(myAprilTagDetection.ftcPose.pitch, 6, 1) + " " + JavaUtil.formatNumber(myAprilTagDetection.ftcPose.roll, 6, 1) + " " + JavaUtil.formatNumber(myAprilTagDetection.ftcPose.yaw, 6, 1) + "  (deg)");
        telemetry.addLine("RBE " + JavaUtil.formatNumber(myAprilTagDetection.ftcPose.range, 6, 1) + " " + JavaUtil.formatNumber(myAprilTagDetection.ftcPose.bearing, 6, 1) + " " + JavaUtil.formatNumber(myAprilTagDetection.ftcPose.elevation, 6, 1) + "  (inch, deg, deg)");
      } else {
        telemetry.addLine("==== (ID " + myAprilTagDetection.id + ") Unknown");
        telemetry.addLine("Center " + JavaUtil.formatNumber(myAprilTagDetection.center.x, 6, 0) + "" + JavaUtil.formatNumber(myAprilTagDetection.center.y, 6, 0) + " (pixels)");
      }
      // Code Written by Me
      // Decimal from -1 to 1
      MotorPower = 1;
      // RED ALLIANCE
      if (myAprilTagDetection.id == 24) {
        // < = Less than
        // <.1
        // > = Greater than
        // >-.1
        while (myAprilTagDetection.ftcPose.y < 0.3 || myAprilTagDetection.ftcPose.y > -0.3) {
          // Get the location of the robot in relation to the April Tag
          DistX = myAprilTagDetection.ftcPose.x;
          DistY = myAprilTagDetection.ftcPose.y;
          // Strafe the robot so that y = <0.1 or -0.1>
          // 0.1
          // Tolerance
          if (DistY < 0.3) {
            FrontRight.setPower(MotorPower);
            BackLeft.setPower(MotorPower);
            BackRight.setPower(-MotorPower);
            FrontLeft.setPower(MotorPower);
          } else {
            FrontRight.setPower(-MotorPower);
            BackLeft.setPower(-MotorPower);
            BackRight.setPower(MotorPower);
            FrontLeft.setPower(-MotorPower);
          }
        }
        FrontRight.setPower(0);
        BackLeft.setPower(0);
        FrontLeft.setPower(0);
        BackRight.setPower(0);
      }
    }
    telemetry.addLine("");
    telemetry.addLine("key:");
    telemetry.addLine("XYZ = X (Right), Y (Forward), Z (Up) dist.");
    telemetry.addLine("PRY = Pitch, Roll & Yaw (XYZ Rotation)");
    telemetry.addLine("RBE = Range, Bearing & Elevation");
  }
}
