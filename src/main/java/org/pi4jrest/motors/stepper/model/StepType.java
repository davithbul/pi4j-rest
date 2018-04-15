package org.pi4jrest.motors.stepper.model;

public enum StepType {

    /**
     * This is the most basic method, turning on a single electromagnet every time.
     * This sequence requires the least amount of energy and generates the smoothest movement.
     */
    SINGLE_STEP {
        @Override
        public byte[] getStepSequence() {
            return new byte[]{
                    (byte) 0b0001,
                    (byte) 0b0010,
                    (byte) 0b0100,
                    (byte) 0b1000
            };
        }
    },

    /**
     * In this method two coils are turned on simultaneously.  This method does not generate
     * a smooth movement as the previous method, and it requires double the current, but as
     * return it generates double the torque.
     */
    DOUBLE_STEP {
        @Override
        public byte[] getStepSequence() {
            return new byte[]{
                    (byte) 0b0011,
                    (byte) 0b0110,
                    (byte) 0b1100,
                    (byte) 0b1001
            };
        }
    },

    /**
     * In this method two coils are turned on simultaneously.  This method does not generate
     * a smooth movement as the previous method, and it requires double the current, but as
     * return it generates double the torque.
     */
    HALF_STEP {
        @Override
        public byte[] getStepSequence() {
            return new byte[]{
                    (byte) 0b0001,
                    (byte) 0b0011,
                    (byte) 0b0010,
                    (byte) 0b0110,
                    (byte) 0b0100,
                    (byte) 0b1100,
                    (byte) 0b1000,
                    (byte) 0b1001
            };
        }
    };

    public abstract byte[] getStepSequence();
}