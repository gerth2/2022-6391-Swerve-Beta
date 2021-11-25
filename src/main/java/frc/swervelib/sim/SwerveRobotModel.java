package frc.swervelib.sim;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.simulation.PDPSim;
import frc.Constants;

public class SwerveRobotModel {

    SwerveDrivetrainModel dt;

    PDPSim pdp;

    final double QUIESCENT_CURRENT_DRAW_A = 2.0; //Misc electronics
    final double BATTERY_NOMINAL_VOLTAGE = 13.2; //Nicely charged battery
    final double BATTERY_NOMINAL_RESISTANCE = 0.040; //40mOhm - average battery + cabling

        
    double batteryVoltage_V = BATTERY_NOMINAL_VOLTAGE;

    
    boolean isBrownedOut;

    public SwerveRobotModel(){
        dt = new SwerveDrivetrainModel();
        pdp = new PDPSim();
        reset(Constants.DFLT_START_POSE);
    }

    public void reset(Pose2d pose){
        dt.modelReset(pose);
    }

    public void update(boolean isDisabled){

        long numIter = Math.round(Constants.CTRLS_SAMPLE_RATE_SEC / Constants.SIM_SAMPLE_RATE_SEC);

        for(long count = 0; count < numIter; count++){
            //Calculate motor disablement due to either actually being in disabled mode,
            // or due to brownout.
            isBrownedOut = (batteryVoltage_V < 6.5);
            isDisabled |= isBrownedOut;

            dt.update(isDisabled, batteryVoltage_V);

            pdp.setVoltage(batteryVoltage_V);
        }

    }

    public Pose2d getCurActPose(){
        return dt.field.getRobotObject().getPose();
    }

}