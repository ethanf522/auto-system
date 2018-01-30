package org.usfirst.frc.team86.robot;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class autoDrive {
	private double targetAngle;
	private double gyroAngle;
	private double percentRotation;

	public enum states {
		driveForward12Feet, driveForward5Feet, driveForwardFoot, driveBack5Feet, driveBackFoot, turnLeft90, turnRight90, ejectCube, grabCube, END
	};

	public states currentState;
	// List<ArrayList<LinkedList<states>>> autoPlans = new
	// ArrayList<ArrayList<LinkedList<states>>>();
	// ArrayList<LinkedList<states>> leftStart = new
	// ArrayList<LinkedList<states>>();
	// ArrayList<LinkedList<states>> middleStart = new
	// ArrayList<LinkedList<states>>();
	// ArrayList<LinkedList<states>> rightStart = new
	// ArrayList<LinkedList<states>>();
	// LinkedList<states> leftPath = new LinkedList<states>();
	states[][] autoPlans = new states[][] { 
			{ states.driveForwardFoot, states.turnRight90, states.driveForwardFoot, states.turnRight90, states.END}, // 0000 - Pos 0 - Sw L - Sc L 
			{ states.driveForwardFoot, states.turnRight90, states.driveForwardFoot, states.turnRight90, states.turnRight90, states.driveForwardFoot, states.END },	 // 0001 - Pos 0 - Sw L - Sc R
			{ states.driveForwardFoot, states.driveBackFoot, states.turnRight90 },	 // 0010 - Pos 0 - Sw R - Sc L
			{ states.driveForwardFoot, states.turnLeft90, states.driveForwardFoot }, // 0011 - Pos 0 - Sw R - Sc R
			{ states.driveForwardFoot, states.turnRight90, states.turnRight90 },	 // 0100 - Pos 1 - Sw L - Sc L
			{ states.driveForwardFoot, states.driveBackFoot, states.turnRight90 },   // 0101 - Pos 1 - Sw L - Sc R
			{ states.driveForwardFoot, states.turnRight90, states.turnRight90 },     // 0110 - Pos 1 - Sw R - Sc L
			{ states.driveForwardFoot, states.turnLeft90, states.driveForwardFoot }, // 0111 - Pos 1 - Sw R - Sc R
			{ states.driveForwardFoot, states.turnRight90, states.turnRight90 },     // 1000 - Pos 2 - Sw L - Sc L
			{ states.driveForwardFoot, states.driveBackFoot, states.turnRight90 },   // 1001 - Pos 2 - Sw L - Sc R
			{ states.driveForwardFoot, states.driveBackFoot, states.turnRight90 },   // 1010 - Pos 2 - Sw R - Sc L
			{ states.driveForwardFoot, states.turnLeft90, states.driveForwardFoot }, // 1011 - Pos 2 - Sw R - Sc R
			{ states.driveForwardFoot, states.turnRight90, states.turnRight90 },     // 1100 - Pos 3 - Sw L - Sc L
			{ states.driveForwardFoot, states.driveBackFoot, states.turnRight90 },   // 1101 - Pos 3 - Sw L - Sc R
			{ states.driveForwardFoot, states.driveBackFoot, states.turnRight90 },   // 1110 - Pos 3 - Sw R - Sc L
			{ states.driveForwardFoot, states.turnLeft90, states.driveForwardFoot }, // 1111 - Pos 3 - Sw R - Sc R
	};
	private int stepIndex = 0;
	private int autoStartPos;
	private int autoSwitchPos;
	private int autoScalePos;
	public double currentTime;
	public double timer;
	private TalonSRX left1;
	private TalonSRX left2;
	private TalonSRX right1;
	private TalonSRX right2;
	private NavX TalonGyro;

	// Unused Position Switch Scale
	// 0000000000000000000000000000 00 0 0
	private int autoIndex;

	public autoDrive(TalonSRX left1, TalonSRX left2, TalonSRX right1, TalonSRX right2) {
		this.left1 = left1;
		this.left2 = left2;
		this.right1 = right1;
		this.right2 = right2;

		left2.set(ControlMode.Follower, left1.getDeviceID());
		right2.set(ControlMode.Follower, right1.getDeviceID());

		left1.set(ControlMode.PercentOutput, 0);
		right2.set(ControlMode.PercentOutput, 0);
	}

	public void setAutoStartPos(int autoStartPos) {
		this.autoStartPos = autoStartPos;
		switch (autoStartPos) {
		case 0:
			break;
		case 1:
			autoIndex += 0B0100;
			break;
		case 2:
			autoIndex += 0B1000;
			break;
		case 3:
			autoIndex += 0B1100;
			break;
		}
	}

	public void setAutoSwitchScalePos(String gameData) {
		if (gameData.charAt(0) == 'R') {
			autoIndex += 0B0010;
		}
		if (gameData.charAt(1) == 'R') {
			autoIndex += 0B0001;
		}
	}

	public void autoInit() {
		// autoIndex = autoSwitchPos << 1; // bit shift moves bit to most significant
		// position
		// autoIndex += autoScalePos;
		currentState = autoPlans[autoIndex][stepIndex];
		SmartDashboard.putNumber("autoIndex", autoIndex);	
		timer = System.currentTimeMillis();
	}
	
	public void driveForward() {
		SmartDashboard.putNumber("left", left1.getMotorOutputPercent());
		SmartDashboard.putString("Step", "driveForwardFoot");
		System.out.println("driveForwardFoot_test");
		SmartDashboard.putNumber("Time", System.currentTimeMillis() - timer);
		if(System.currentTimeMillis() - timer > 2000) {
			left1.set(ControlMode.PercentOutput, 0);
			right1.set(ControlMode.PercentOutput, 0);
			stepIndex++;
			//timer = System.currentTimeMillis();
		}else {
			left1.set(ControlMode.PercentOutput, .2);
			right1.set(ControlMode.PercentOutput, -.2);
		}
	}

	public void autoUpdate() {
		SmartDashboard.putNumber("Time", timer);
		switch (currentState) {

		case driveForward12Feet:
			// Actions
			SmartDashboard.putString("Step", "driveForward12Feet");
			System.out.println("driveForward12Feet");
			stepIndex++;
			break;

		case driveForward5Feet:
			// Actions
			SmartDashboard.putString("Step", "driveForward5Feet");
			System.out.println("driveForward5Feet");
			stepIndex++;
			break;

		case driveForwardFoot:
			left1.set(ControlMode.PercentOutput, .2);
			right1.set(ControlMode.PercentOutput, -.2);
			SmartDashboard.putString("Step", "driveForwardFoot");
			System.out.println("driveForwardFoot");
			if(System.currentTimeMillis() - timer > 2000) {
				left1.set(ControlMode.PercentOutput, 0);
				right1.set(ControlMode.PercentOutput, 0);
				stepIndex++;
				timer = System.currentTimeMillis();
			}
			break;

		case driveBack5Feet:
			// Actions
			SmartDashboard.putString("Step", "driveBack5Feet");
			System.out.println("driveBack5Feet");
			stepIndex++;
			break;

		case driveBackFoot:
			// Actions
			SmartDashboard.putString("Step", "driveBackFoot");
			System.out.println("driveBackFoot");
			stepIndex++;
			break;

		case turnLeft90:
			targetAngle = 90;
			gyroAngle = TalonGyro.getNormalizedAngle();

			// percent rotation
			double error = (targetAngle - gyroAngle) * 1 / 180;

			percentRotation = error;

			left1.set(ControlMode.PercentOutput, -percentRotation);
			right1.set(ControlMode.PercentOutput, percentRotation);

			SmartDashboard.putString("Step", "turnLeft90");
			System.out.println("turnLeft90");
			if (error < 10) {
				stepIndex++;
				timer = System.currentTimeMillis();
			}
			break;

		case turnRight90:
			targetAngle = 90;
			gyroAngle = TalonGyro.getNormalizedAngle();

			// percent rotation
			error = (targetAngle - gyroAngle) * 1 / 180;

			percentRotation = error;

			left1.set(ControlMode.PercentOutput, percentRotation);
			right1.set(ControlMode.PercentOutput, -percentRotation);

			SmartDashboard.putString("Step", "turnRight90");
			System.out.println("turnRight90");
			if (error < 10) {
				stepIndex++;
				timer = System.currentTimeMillis();
			}
			break;

		case ejectCube:
			// Actions
			SmartDashboard.putString("Step", "ejectCube");
			System.out.println("ejectCube");
			stepIndex++;
			break;

		case grabCube:
			// Actions
			SmartDashboard.putString("Step", "grabCube");
			System.out.println("grabCube");
			stepIndex++;
			break;
			
		case END:
			left1.set(ControlMode.PercentOutput, 0);
			right1.set(ControlMode.PercentOutput, 0);
			break;
		}

	}
}
