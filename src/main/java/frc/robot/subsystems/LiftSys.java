package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.CANDevices;
import frc.robot.Constants.LiftConstants;
import frc.robot.Constants.PneumaticChannels;

public class LiftSys extends SubsystemBase {

    private final CANSparkMax masterMtr;
    private final CANSparkMax slaveMtr;
 
    private final RelativeEncoder liftEnc;
 
    private final SparkMaxPIDController controller;
 
    private final DoubleSolenoid liftSol;
 
    private double targetInches = 0;

    private boolean isManual = false;

    public LiftSys() {
        masterMtr = new CANSparkMax(CANDevices.masterMtrId, MotorType.kBrushless);
        slaveMtr = new CANSparkMax(CANDevices.slaveMtrId, MotorType.kBrushless);

        masterMtr.setInverted(false);
        slaveMtr.setInverted(true);

        masterMtr.setSmartCurrentLimit(LiftConstants.maxCurrentAmps);

        masterMtr.setIdleMode(IdleMode.kBrake);

        slaveMtr.follow(masterMtr, true);

        liftEnc = masterMtr.getEncoder();

        liftEnc.setPosition(0);

        liftEnc.setPositionConversionFactor(LiftConstants.inchesPerEncRev);
        liftEnc.setVelocityConversionFactor(LiftConstants.feetPerSecondPerRPM);

        controller = masterMtr.getPIDController();

        controller.setP(LiftConstants.kP);
        controller.setD(LiftConstants.kD);

        controller.setOutputRange(LiftConstants.minPower, LiftConstants.maxPower);
        
        controller.setIZone(0);
        
        liftSol = new DoubleSolenoid(PneumaticsModuleType.REVPH, PneumaticChannels.liftSolCh[0], PneumaticChannels.liftSolCh[1]);

    }

    @Override
    public void periodic() {
        if(isManual) {
            controller.setOutputRange(-LiftConstants.manualPower, LiftConstants.manualPower);
        }
        else {
            controller.setOutputRange(LiftConstants.minPower, LiftConstants.maxPower);
            controller.setReference(targetInches, ControlType.kPosition);
        }

        if(targetInches < 0.0) targetInches = 0.0;
        else if (targetInches > LiftConstants.maxHeightInches) targetInches = LiftConstants.maxHeightInches;


        SmartDashboard.putNumber("lift inches", liftEnc.getPosition());
        SmartDashboard.putNumber("lift velocity", liftEnc.getVelocity());
        SmartDashboard.putNumber("lift target", targetInches);
        SmartDashboard.putNumber("lift power", masterMtr.get());
    }

    public void setPower(double power) {
        if(
            (liftEnc.getPosition() <= LiftConstants.manualControlPadding && power < 0.0) ||
            (liftEnc.getPosition() >= LiftConstants.maxHeightInches - LiftConstants.manualControlPadding && power > 0.0)
        ) {
            masterMtr.set(0.0);
        }
        else {
            masterMtr.set(power);
        }
    }

    public void setTarget(double inches) {
        isManual = false;
        if(inches > LiftConstants.maxHeightInches) inches = LiftConstants.maxHeightInches;

        targetInches = inches;
    }

    public void manualControl(double manual) {
        if(manual != 0) {
            isManual = true;
            // targetInches = liftEnc.getPosition() + (manual * LiftConstants.manualControlSensitivity);
            setPower(manual * LiftConstants.manualPower);
        }
        else {
            if(isManual)
                targetInches = liftEnc.getPosition();

            isManual = false;
        }
    }
}