
package org.usfirst.frc.team86.robot;

import java.util.LinkedList;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	IO io = new IO();
	autoDrive drive = new autoDrive(io.left1, io.left2, io.right1, io.right2);
	public int robotPos;
	
	@Override
	public void robotInit() {

	}

	@Override
	public void autonomousInit() {
		drive.setAutoSwitchScalePos("LL");
		//SmartDashboard.getNumber("RobotPosition", robotPos);
		robotPos = 0;
		drive.setAutoStartPos(robotPos);
		drive.autoInit();
	}

	@Override
	public void autonomousPeriodic() {
		drive.driveForward();
	}

	@Override
	public void teleopPeriodic() {
	}

	@Override
	public void testPeriodic() {
	}
}
