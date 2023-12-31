package frc.robot.commands.lift;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.LiftConstants;
import frc.robot.subsystems.LiftSys;

public class ShelfPickupCmd extends CommandBase {

    private final LiftSys liftSys;

    private final boolean finishInstantly;

    /**
     * Constructs a new ExampleCmd.
     * 
     * <p>ExampleCmd contains the basic framework of a robot command for use in command-based programming.
     * 
     * <p>The command finishes once the isFinished method returns true.
     * 
     * @param exampleSys The required ExampleSys.
     */
    public ShelfPickupCmd(boolean finishInstantly, LiftSys liftSys) {
        this.liftSys = liftSys;
        this.finishInstantly = finishInstantly;

        addRequirements(liftSys);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        liftSys.setArticulationOverride(true);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if(liftSys.getTargetInches() == LiftConstants.shelfPickupInches)
            liftSys.setTarget(LiftConstants.shelfRaiseInches, LiftConstants.shelfPower);
        else
            liftSys.setTarget(LiftConstants.shelfPickupInches, LiftConstants.shelfPower);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        
    }
    
    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return finishInstantly || liftSys.isAtTarget();
    }

    // Whether the command should run when robot is disabled.
    @Override
    public boolean runsWhenDisabled() {
        return false;
    }
}