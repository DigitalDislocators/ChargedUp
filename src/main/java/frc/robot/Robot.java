package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class Robot extends TimedRobot {
    
    private RobotContainer robotContainer;

    private Command autonomousCommand;

    private boolean controlsAreBinded = false;

    @Override
    public void robotInit() {

        robotContainer = new RobotContainer();

    }

    @Override
    public void robotPeriodic() {

        CommandScheduler.getInstance().run();

        if(DriverStation.isEnabled() && !controlsAreBinded) {
            robotContainer.configBindings();
            controlsAreBinded = true;
        }

        robotContainer.periodic();
        
    }

    @Override
    public void autonomousInit() {
        
        autonomousCommand = robotContainer.getAutonomousCommand();

        if(autonomousCommand != null) autonomousCommand.schedule();

    }

    @Override
    public void teleopInit() {

        if(autonomousCommand != null) autonomousCommand.cancel();

    }

}
