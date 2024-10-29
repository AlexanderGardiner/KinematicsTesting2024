package frc.robot.subsystems;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;

public class Kinematics {
    private static Rotation2d shoulder;
    private static Rotation2d elbow;
    private static Rotation2d turret;
    private static double distToTarget;
    private static Pose3d armTarget;
    private static double foreArmLen = 1.0; // TODO Set with actual value
    private static double upperArmLen = 1.0; // TODO Set with actual value
    private static double motorUnits = 1.0; // TODO Set with actual value
    
    public static void setTarget(Pose3d target) {
        calcTargetDist();
        if (distToTarget > foreArmLen + upperArmLen) {
            // TODO Add lights integration *RED*
            return;
        }
        armTarget = target;
        calcElbowAngle(); //radians - motor stuff
        calcShoulderAngle(); // radians - motor stuff
        calcTurretAngle();
    }

    private static void calcShoulderAngle() {
        shoulder = new Rotation2d(Math.atan(armTarget.getZ()/distToTarget)-Math.atan((foreArmLen*Math.sin(elbow.getRadians()))/upperArmLen+foreArmLen*Math.cos(elbow.getRadians())));
        elbow.plus(shoulder);
    }

    private static void calcElbowAngle() {
        elbow = new Rotation2d(-Math.acos((Math.pow(distToTarget,2)+Math.pow(armTarget.getZ(),2)-Math.pow(upperArmLen,2)-Math.pow(foreArmLen,2))/2*upperArmLen*foreArmLen));
    }

    private static void calcTurretAngle(){
        turret = new Rotation2d(Math.atan2(armTarget.getY(), armTarget.getX()));
    }

    private static void calcTargetDist(){
        distToTarget = Math.sqrt(Math.pow(armTarget.getX(), 2) + Math.pow(armTarget.getY(), 2));
    }

    public static double getMotor1Target() {
        return shoulder.getRadians() * motorUnits;
    }

    public static double getMotor2Target() {
        return elbow.getRadians() * motorUnits;
    }

    public static double getTurretRotationTarget() {
        return turret.getRadians() * motorUnits;
    }
}
