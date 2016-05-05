package ninja.oakley.whisker.stepper;

import com.pi4j.component.motor.impl.GpioStepperMotorComponent;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;

public class StepperMotorController {

    private static final GpioController gpio = GpioFactory.getInstance();
    private final GpioStepperMotorComponent motor;

    public StepperMotorController(GpioPinDigitalOutput[] pins){
        this.motor = new GpioStepperMotorComponent(pins);
        gpio.setShutdownOptions(true, PinState.LOW, pins);
        motor.setStepInterval(2);
        motor.setStepsPerRevolution(2038);
    }

    public void setStepSequence(StepSequence seq){
        motor.setStepSequence(seq.getSequence());
    }


    public enum StepSequence {
        DOUBLE_STEP(new byte[] {(byte) 0b0011, (byte) 0b0110, (byte) 0b1100, (byte) 0b1001}),
        SINGLE_STEP(new byte[] {(byte) 0b0001,(byte) 0b0010,(byte) 0b0100,(byte) 0b1000}),
        HALF_STEP(new byte[] {(byte) 0b0001, (byte) 0b0011, (byte) 0b0010,(byte) 0b0110,         
                (byte) 0b0100, (byte) 0b1100, (byte) 0b1000, (byte) 0b1001});

        private final byte[] seq;

        private StepSequence(byte[] seq){
            this.seq = seq;
        }

        public byte[] getSequence(){
            return seq;
        }
    }

}
