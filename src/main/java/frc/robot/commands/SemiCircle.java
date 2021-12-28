package frc.robot.commands;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants.AUTO;
import frc.robot.Constants.DRIVE;
import frc.robot.lib.SwerveDriveKinematicsConstraint6391;
import frc.robot.lib.TrajectoryConfig6391;
import frc.robot.lib.TrajectoryGenerator6391;
import frc.robot.subsystems.DrivetrainSubsystem;

public class SemiCircle extends SequentialCommandGroup {
  SwerveDriveKinematicsConstraint6391 constraint = new SwerveDriveKinematicsConstraint6391(DRIVE.KINEMATICS, DRIVE.MAX_FWD_REV_SPEED_MPS);
  TrajectoryConfig6391 config =
        new TrajectoryConfig6391(
                AUTO.kMaxSpeedMetersPerSecond,
                AUTO.kMaxAccelerationMetersPerSecondSquared)
            // Add kinematics to ensure max speed is actually obeyed
            .setKinematics(DRIVE.KINEMATICS)
            .addConstraint(constraint);
  ArrayList<Rotation2d> headings = new ArrayList<Rotation2d>(
        List.of(
          Rotation2d.fromDegrees(180),
          Rotation2d.fromDegrees(90),
          Rotation2d.fromDegrees(0)));
  Trajectory forward =
        TrajectoryGenerator6391.generateTrajectory(
          List.of(
            // Start at the origin facing the +X direction
            new Pose2d(1, 1, Rotation2d.fromDegrees(45)),
            // Dummy interior waypoint
            new Pose2d(4, 4, Rotation2d.fromDegrees(0)),
            // End 3 meters straight ahead of where we started, facing forward
            new Pose2d(7, 1, Rotation2d.fromDegrees(-45))),
            headings,
            config);
  public SemiCircle(DrivetrainSubsystem m_drive) {
    addCommands(
      m_drive.dt.createCommandForTrajectory(forward, m_drive)
    );
  }
}