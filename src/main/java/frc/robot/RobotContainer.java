package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.ControllerType;
import frc.robot.Constants.GameElement;
import frc.robot.Constants.ControllerConstants;
import frc.robot.commands.SetElementStatusCmd;
import frc.robot.commands.auto.programs.CenterConeDock;
import frc.robot.commands.auto.programs.CenterConeGrabCubeDock;
import frc.robot.commands.auto.programs.CenterConeMobilityDock;
import frc.robot.commands.auto.programs.Cone;
import frc.robot.commands.auto.programs.Cube;
import frc.robot.commands.auto.programs.LeftConeGrabCube;
import frc.robot.commands.auto.programs.LeftConeGrabCubeDock;
import frc.robot.commands.auto.programs.LeftConeScoreCube;
import frc.robot.commands.auto.programs.LeftConeScoreCubeDock;
import frc.robot.commands.auto.programs.LeftConeScoreCubeGrabCube;
import frc.robot.commands.auto.programs.LeftConeScoreCubeScoreCubeHigh;
import frc.robot.commands.auto.programs.LeftConeScoreCubeScoreCubeMid;
import frc.robot.commands.auto.programs.RightConeGrabCube;
import frc.robot.commands.auto.programs.RightConeGrabCubeDock;
import frc.robot.commands.auto.programs.RightConeScoreCube;
import frc.robot.commands.auto.programs.RightConeScoreCubeGrabCube;
import frc.robot.commands.claw.CloseCmd;
import frc.robot.commands.claw.OpenCmd;
import frc.robot.commands.drivetrain.DefaultSpeedCmd;
import frc.robot.commands.drivetrain.ResetHeadingCmd;
import frc.robot.commands.drivetrain.SetLockedCmd;
import frc.robot.commands.drivetrain.SprintSpeedCmd;
import frc.robot.commands.drivetrain.SwerveDriveCmd;
import frc.robot.commands.drivetrain.TurtleSpeedCmd;
import frc.robot.commands.intake.InCmd;
import frc.robot.commands.intake.OutCmd;
import frc.robot.commands.intake.SetAbsoluteSpeedCmd;
import frc.robot.commands.intake.StopRollersCmd;
import frc.robot.commands.intake.IntakeManualControlCmd;
import frc.robot.commands.lift.DownCmd;
import frc.robot.commands.lift.LiftManualControlCmd;
import frc.robot.commands.lift.Row1Cmd;
import frc.robot.commands.lift.Row2Cmd;
import frc.robot.commands.lift.Row3Cmd;
import frc.robot.commands.vision.RestartLimelightCmd;
import frc.robot.subsystems.ClawSys;
import frc.robot.subsystems.CompressorSys;
import frc.robot.subsystems.IntakeSys;
import frc.robot.subsystems.LiftSys;
import frc.robot.subsystems.LightsSys;
import frc.robot.subsystems.SwerveSys;
import frc.robot.subsystems.VisionSys;

public class RobotContainer {
    
    // Initialize subsystems.
    private final SwerveSys swerveSys = new SwerveSys();
    private final LiftSys liftSys = new LiftSys();
    private final ClawSys clawSys = new ClawSys();
    private final IntakeSys intakeSys = new IntakeSys(() -> swerveSys.getForwardVelocityMetersPerSecond());
    private final VisionSys visionSys = new VisionSys();  
    private final CompressorSys compressorSys = new CompressorSys();  
    private final LightsSys lightsSys = new LightsSys();

    // Initialize joysticks.
    private final XboxController driverController = new XboxController(ControllerConstants.driverGamepadPort);

    private final Joystick driverLeftJoystick = new Joystick(ControllerConstants.driverLeftJoystickPort);
    private final Joystick driverRightJoystick = new Joystick(ControllerConstants.driverRightJoystickPort);

    private final XboxController operatorController = new XboxController(ControllerConstants.operatorGamepadPort);

    private final XboxController hybridController = new XboxController(ControllerConstants.hybridControllerPort);

    // Initialize controller buttons.
    private final JoystickButton driverRightJoystickTriggerBtn = new JoystickButton(driverRightJoystick, 1);
    private final JoystickButton driverRightJoystickThumbBtn = new JoystickButton(driverRightJoystick, 2);

    private final JoystickButton driverABtn = new JoystickButton(driverController, 1);
    private final JoystickButton driverBBtn = new JoystickButton(driverController, 2);
    private final JoystickButton driverLeftBumper = new JoystickButton(driverController, 5);
    private final JoystickButton driverRightBumper = new JoystickButton(driverController, 6);
    private final JoystickButton driverMenuBtn = new JoystickButton(driverController, 8);
    private final Trigger driverRightTriggerBtn =
        new Trigger(() -> driverController.getRightTriggerAxis() > ControllerConstants.triggerPressedDeadband);
    private final Trigger driverLeftTriggerBtn =
        new Trigger(() -> driverController.getLeftTriggerAxis() > ControllerConstants.triggerPressedDeadband);

    private final JoystickButton operatorABtn = new JoystickButton(operatorController, 1);
    private final JoystickButton operatorBBtn = new JoystickButton(operatorController, 2);
    private final JoystickButton operatorXBtn = new JoystickButton(operatorController, 3);
    private final JoystickButton operatorYBtn = new JoystickButton(operatorController, 4);
    private final JoystickButton operatorLeftBumper = new JoystickButton(operatorController, 5);
    private final JoystickButton operatorRightBumper = new JoystickButton(operatorController, 6);
    private final JoystickButton operatorWindowBtn = new JoystickButton(operatorController, 7);
    private final JoystickButton operatorMenuBtn = new JoystickButton(operatorController, 8);
    private final POVButton operatorUpBtn = new POVButton(operatorController, 0);
    private final POVButton operatorDownBtn = new POVButton(operatorController, 180);

    private final JoystickButton hybridABtn = new JoystickButton(hybridController, 1);
    private final JoystickButton hybridBBtn = new JoystickButton(hybridController, 2);
    private final JoystickButton hybridXBtn = new JoystickButton(hybridController, 3);
    private final JoystickButton hybridYBtn = new JoystickButton(hybridController, 4);
    private final JoystickButton hybridLeftBumper = new JoystickButton(hybridController, 5);
    private final JoystickButton hybridRightBumper = new JoystickButton(hybridController, 6);
    private final JoystickButton hybridWindowBtn = new JoystickButton(hybridController, 7);
    private final JoystickButton hybridMenuBtn = new JoystickButton(driverController, 8);

    // Instantiate controller rumble.
    private Rumble matchTimeRumble;
    private Rumble brownOutRumble;
    private Rumble countdown10Rumble;
    private Rumble countdown5Rumble;

    // Initialize auto selector.
    SendableChooser<Command> autoSelector = new SendableChooser<Command>();

    public RobotContainer() {
        SmartDashboard.putData("auto chooser", autoSelector);

        RestartLimelightCmd restartLimelight = new RestartLimelightCmd(visionSys);
        restartLimelight.setName("Restart Limelight");
        SmartDashboard.putData(restartLimelight);

        RunCommand runCompressor = new RunCommand(() -> compressorSys.run(), compressorSys);
        runCompressor.setName("Run Compressor");
        SmartDashboard.putData(runCompressor);

        RunCommand disableCompressor = new RunCommand(() -> compressorSys.disable(), compressorSys);
        disableCompressor.setName("Disable Compressor");
        SmartDashboard.putData(disableCompressor);

        RobotController.setBrownoutVoltage(7.5);

        autoSelector.addOption("Cone", new Cone(liftSys, clawSys, intakeSys));
        autoSelector.addOption("Cube", new Cube(liftSys, clawSys, intakeSys));
        autoSelector.addOption("CenterConeDock", new CenterConeDock(swerveSys, liftSys, clawSys, intakeSys));
        autoSelector.addOption("CenterConeMobilityDock", new CenterConeMobilityDock(swerveSys, liftSys, clawSys, intakeSys));
        autoSelector.addOption("CenterConeGrabCubeDock", new CenterConeGrabCubeDock(swerveSys, liftSys, clawSys, intakeSys));
        autoSelector.addOption("LeftConeGrabCube", new LeftConeGrabCube(swerveSys, liftSys, clawSys, intakeSys));
        autoSelector.addOption("LeftConeGrabCubeDock", new LeftConeGrabCubeDock(swerveSys, liftSys, clawSys, intakeSys));
        autoSelector.addOption("LeftConeScoreCube", new LeftConeScoreCube(swerveSys, liftSys, clawSys, intakeSys));
        autoSelector.addOption("LeftConeScoreCubeDock", new LeftConeScoreCubeDock(swerveSys, liftSys, clawSys, intakeSys));
        autoSelector.addOption("LeftConeScoreCubeGrabCube", new LeftConeScoreCubeGrabCube(swerveSys, liftSys, clawSys, intakeSys));
        autoSelector.addOption("LeftConeScoreCubeScoreCubeMid", new LeftConeScoreCubeScoreCubeMid(swerveSys, liftSys, clawSys, intakeSys));
        autoSelector.addOption("LeftConeScoreCubeScoreCubeHigh", new LeftConeScoreCubeScoreCubeHigh(swerveSys, liftSys, clawSys, intakeSys));
        autoSelector.addOption("RightConeGrabCube", new LeftConeGrabCube(swerveSys, liftSys, clawSys, intakeSys));
        autoSelector.addOption("RightConeGrabCubeDock", new LeftConeGrabCubeDock(swerveSys, liftSys, clawSys, intakeSys));
        autoSelector.addOption("RightConeScoreCube", new LeftConeScoreCube(swerveSys, liftSys, clawSys, intakeSys));
        autoSelector.addOption("RightConeScoreCubeDock", new LeftConeScoreCubeDock(swerveSys, liftSys, clawSys, intakeSys));
        autoSelector.addOption("RightConeScoreCubeGrabCube", new RightConeScoreCubeGrabCube(swerveSys, liftSys, clawSys, intakeSys));
        autoSelector.addOption("RightConeScoreCubeScoreCubeMid", new LeftConeScoreCubeScoreCubeMid(swerveSys, liftSys, clawSys, intakeSys));
        autoSelector.addOption("RightConeScoreCubeScoreCubeHigh", new LeftConeScoreCubeScoreCubeHigh(swerveSys, liftSys, clawSys, intakeSys));
        autoSelector.setDefaultOption("DoNothing", null);
    }

    public void configBindings() {
        if(DriverStation.isJoystickConnected(ControllerConstants.hybridControllerPort)) {
            configHybridBindings();
            SmartDashboard.putString("control type", "hybrid");
        }
        else {
            if(DriverStation.getJoystickIsXbox(ControllerConstants.driverGamepadPort)) {
                configDriverBindings(ControllerType.kGamepad);
                SmartDashboard.putString("control type", "gamepad");
            }
            else if(DriverStation.isJoystickConnected(ControllerConstants.driverRightJoystickPort)) {
                configDriverBindings(ControllerType.kJoystick);
                SmartDashboard.putString("control type", "joysticks");
            }
            else {
                SmartDashboard.putString("control type", "only operator");
                brownOutRumble = new Rumble(RumbleType.kRightRumble, 1.0);
            }

            configOperatorBindings();
        }

        brownOutRumble.rumbleWhen(() -> RobotController.isBrownedOut(), 2.0);
        
        matchTimeRumble.pulseWhen(() -> DriverStation.getMatchTime() <= 60.0 && DriverStation.isTeleop(), 3);
        matchTimeRumble.pulseWhen(() -> DriverStation.getMatchTime() <= 30.0 && DriverStation.isTeleop(), 2);
        matchTimeRumble.pulseWhen(() -> DriverStation.getMatchTime() <= 15.0 && DriverStation.isTeleop(), 1);

        countdown10Rumble.setPulseTime(1.0);
        countdown10Rumble.pulseWhen(() -> DriverStation.getMatchTime() <= 10.0 && DriverStation.isTeleop(), 5);

        countdown5Rumble.setPulseTime(1.0);
        countdown5Rumble.setPulseLength(0.25);
        countdown5Rumble.pulseWhen(() -> DriverStation.getMatchTime() <= 5.0 && DriverStation.isTeleop(), 5);
    }

    public void configDriverBindings(ControllerType driverControllerType) {
        if(driverControllerType.equals(ControllerType.kGamepad)) {
            swerveSys.setDefaultCommand(
                new SwerveDriveCmd(
                    () -> deadband(driverController.getLeftY(), driverControllerType),
                    () -> deadband(driverController.getLeftX(), driverControllerType),
                    () -> deadband(driverController.getRightX(), driverControllerType),
                    true,
                    swerveSys
                )
            );

            driverABtn.onTrue(new RunCommand(() -> {
                if(lightsSys.isPartyMode()) {
                    lightsSys.cancelAnimations();
                }
                else {
                    lightsSys.setPartyMode(true);
                    lightsSys.setWeeWooMode(false);
                }
            }));
            driverBBtn.onTrue(new RunCommand(() -> {
                if(lightsSys.isWeeWooMode()) {
                    lightsSys.cancelAnimations();
                }
                else {
                    lightsSys.setWeeWooMode(true);
                    lightsSys.setPartyMode(false);
                }
            }));
            
            driverLeftBumper.onTrue(new SetLockedCmd(true, swerveSys)).onFalse(new SetLockedCmd(false, swerveSys));
            driverRightBumper.onTrue(new TurtleSpeedCmd(swerveSys)).onFalse(new DefaultSpeedCmd(swerveSys));
            driverMenuBtn.onTrue(new ResetHeadingCmd(swerveSys));

            driverRightTriggerBtn
                .onTrue(new OutCmd(intakeSys))
                .whileTrue(new SetAbsoluteSpeedCmd(intakeSys))
                .onFalse(new InCmd(intakeSys))
                .onFalse(new StopRollersCmd(intakeSys));

            driverLeftTriggerBtn.onTrue(new SprintSpeedCmd(swerveSys)).onFalse(new DefaultSpeedCmd(swerveSys));

            brownOutRumble = new Rumble(RumbleType.kLeftRumble, 1.0, driverController);
            matchTimeRumble = new Rumble(RumbleType.kRightRumble, 1.0, driverController);
            countdown10Rumble = new Rumble(RumbleType.kRightRumble, 1.0, driverController);
            countdown5Rumble = new Rumble(RumbleType.kRightRumble, 1.0, driverController);
        }
        else {
            swerveSys.setDefaultCommand(
                new SwerveDriveCmd(
                    () -> deadband(driverLeftJoystick.getY(), driverControllerType),
                    () -> deadband(driverLeftJoystick.getX(), driverControllerType),
                    () -> deadband(driverRightJoystick.getX(), driverControllerType),
                    true,
                    swerveSys
                )
            );

            driverRightJoystickTriggerBtn
                .onTrue(new OutCmd(intakeSys))
                .whileTrue(new SetAbsoluteSpeedCmd(intakeSys))
                .onFalse(new InCmd(intakeSys))
                .onFalse(new StopRollersCmd(intakeSys));

            driverRightJoystickThumbBtn.onTrue(new ResetHeadingCmd(swerveSys));

            brownOutRumble = new Rumble(RumbleType.kLeftRumble, 1.0);
            matchTimeRumble = new Rumble(RumbleType.kRightRumble, 1.0);
            countdown10Rumble = new Rumble(RumbleType.kRightRumble, 1.0);
            countdown5Rumble = new Rumble(RumbleType.kRightRumble, 1.0);
        }
    }

    public void configOperatorBindings() {
        liftSys.setDefaultCommand(
            new LiftManualControlCmd(
                () -> deadband(operatorController.getRightY(), ControllerType.kGamepad),
                liftSys
            )
        );

        intakeSys.setDefaultCommand(
            new IntakeManualControlCmd(
                () -> deadband(operatorController.getLeftY(), ControllerType.kGamepad),
                () -> deadband(operatorController.getRightTriggerAxis(), ControllerType.kGamepad),
                () -> deadband(operatorController.getLeftTriggerAxis(), ControllerType.kGamepad),
                intakeSys
            )
        );

        operatorABtn.onTrue(new Row1Cmd(true, liftSys));
        operatorBBtn.onTrue(new Row2Cmd(true, liftSys));
        operatorXBtn.onTrue(new DownCmd(true, liftSys));
        operatorYBtn.onTrue(new Row3Cmd(true, liftSys));

        operatorWindowBtn.onTrue(new SetElementStatusCmd(GameElement.kCube, liftSys, visionSys, lightsSys));
        operatorMenuBtn.onTrue(new SetElementStatusCmd(GameElement.kCone, liftSys, visionSys, lightsSys));

        operatorWindowBtn.and(operatorMenuBtn).onTrue(new SetElementStatusCmd(GameElement.kNone, liftSys, visionSys, lightsSys));
        
        operatorLeftBumper.onTrue(new OpenCmd(clawSys));
        operatorRightBumper.onTrue(new CloseCmd(clawSys));

        operatorUpBtn.onTrue(new OutCmd(intakeSys));
        operatorDownBtn.onTrue(new InCmd(intakeSys));

        matchTimeRumble = new Rumble(RumbleType.kRightRumble, 1.0, operatorController);
        countdown10Rumble = new Rumble(RumbleType.kRightRumble, 1.0, operatorController);
        countdown5Rumble = new Rumble(RumbleType.kRightRumble, 1.0, operatorController);
    }

    public void configHybridBindings() {
        swerveSys.setDefaultCommand(
            new SwerveDriveCmd(
                () -> deadband(hybridController.getLeftY(), ControllerType.kGamepad),
                () -> deadband(hybridController.getLeftX(), ControllerType.kGamepad),
                () -> deadband(hybridController.getRightX(), ControllerType.kGamepad),
                true,
                swerveSys
            )
        );

        hybridABtn.onTrue(new Row1Cmd(true, liftSys));
        hybridBBtn.onTrue(new Row2Cmd(true, liftSys));
        hybridXBtn.onTrue(new DownCmd(true, liftSys));
        hybridYBtn.onTrue(new Row3Cmd(true, liftSys));

        hybridMenuBtn.onTrue(new ResetHeadingCmd(swerveSys));
        
        hybridLeftBumper.onTrue(new OpenCmd(clawSys));
        hybridRightBumper.onTrue(new CloseCmd(clawSys));

        brownOutRumble = new Rumble(RumbleType.kLeftRumble, 1.0, hybridController);
        matchTimeRumble = new Rumble(RumbleType.kRightRumble, 1.0, hybridController);
        countdown10Rumble = new Rumble(RumbleType.kRightRumble, 1.0, hybridController);
        countdown5Rumble = new Rumble(RumbleType.kRightRumble, 1.0, hybridController);
    }

    public Command getAutonomousCommand() {
        return autoSelector.getSelected();
    }

    /**
     * Deadbands inputs to eliminate tiny unwanted values from the joysticks or gamepad sticks.
     * <p>If the distance between the input and zero is less than the deadband amount, the output will be zero.
     * Otherwise, the value will not change.
     * 
     * @param input The controller value to deadband.
     * @param controllerType The type of controller, since joysticks, gamepad sticks, and gamepad triggers can
     * have different deadbands.
     * @return The deadbanded controller value.
     */
    public double deadband(double value, ControllerType controllerType) {

        if (Math.abs(value) < (controllerType.equals(ControllerType.kGamepad) ?
                ControllerConstants.gamepadDeadband :
                ControllerConstants.joystickDeadband
            )
        )
            return 0.0;
        
        return value;
    }

    public void updateInterface() {
        // BATTERY
        SmartDashboard.putNumber("battery voltage", RobotController.getBatteryVoltage());

        // SWERVE
        SmartDashboard.putNumber("heading", -swerveSys.getHeading().getDegrees() % 180);

        SmartDashboard.putNumber("speed m/s", swerveSys.getAverageDriveVelocityMetersPerSecond());
        SmartDashboard.putNumber("speed mph", swerveSys.getAverageDriveVelocityMetersPerSecond() * 2.23694);

        SmartDashboard.putBoolean("balanced", Math.abs(swerveSys.getRollDegrees()) < 4.0);

        // COMPRESSOR
        SmartDashboard.putNumber("pressure PSI", compressorSys.getPressurePSI());

        SmartDashboard.putBoolean("compressor running", compressorSys.isRunning());
        SmartDashboard.putNumber("compressor elapsed", compressorSys.getRunTimeSeconds());
        SmartDashboard.putNumber("compressor turn on count", compressorSys.getTurnOnCount());

        // INTAKE
        SmartDashboard.putNumber("intake inches", intakeSys.getCurrentPosition());
        SmartDashboard.putNumber("roller speed m/s", intakeSys.getCurrentSpeedMetersPerSecond());

        // LIFT
        SmartDashboard.putNumber("lift inches", liftSys.getCurrentPosition());
        SmartDashboard.putString("lift actuation", (liftSys.isActuatedDown() ? "down" : "up"));
        SmartDashboard.putNumber("lift target", liftSys.getTargetInches());
        SmartDashboard.putBoolean("lift isManual", liftSys.isManual());

        // CLAW
        SmartDashboard.putBoolean("claw status", clawSys.isOpen());
    }
}
