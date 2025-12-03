package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

public class ShooterPID {

    private final DcMotorEx shooterMotor;

    // --- PIDF ---
    private double kP = 20.0;
    private double kI = 0;
    private double kD = 2.0;
    private double kF = 14.0;

    // --- RPM config ---
    private static final double TICKS_PER_REV = 28.0;
    private double targetRPM = 3000;

    // --- Boost ---
    private double boostPower = 0.15;     // +15%
    private long boostTime = 120;         // ms
    private long boostStart = 0;
    private double dropThreshold = 250;   // RPM drop to trigger boost

    public ShooterPID(HardwareMap hw) {
        shooterMotor = hw.get(DcMotorEx.class, "Outtake");
        shooterMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        shooterMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        shooterMotor.setPIDFCoefficients(
                DcMotorEx.RunMode.RUN_USING_ENCODER,
                new PIDFCoefficients(kP, kI, kD, kF)
        );
    }

    /** Set desired RPM */
    public void setTargetRPM(double rpm) {
        targetRPM = Math.max(0, Math.min(6000, rpm));
    }

    /** Returns current RPM */
    public double getCurrentRPM() {
        double currentTPS = shooterMotor.getVelocity();  // ticks / second
        return currentTPS / TICKS_PER_REV * 60.0;
    }

    /** Call every loop() */
    public void update(boolean shooterOn) {
        if (!shooterOn) {
            shooterMotor.setPower(0);
            return;
        }

        double currentRPM = getCurrentRPM();
        double targetTPS = targetRPM / 60.0 * TICKS_PER_REV;

        // Detect RPM drop → start boost
        if (targetRPM - currentRPM > dropThreshold) {
            boostStart = System.currentTimeMillis();
        }

        boolean boosting = (System.currentTimeMillis() - boostStart) < boostTime;

        double velocityToSet = targetTPS * (boosting ? 1.0 + boostPower : 1.0);

        shooterMotor.setVelocity(velocityToSet);
    }
}
