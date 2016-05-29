package ninja.oakley.whisker.hardware;

import com.pi4j.component.motor.MotorState;
import com.pi4j.component.motor.impl.GpioStepperMotorComponent;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;

public class StepperMotorController {

    private static final GpioController gpio = GpioFactory.getInstance();
    private final GpioStepperMotorComponent motor;

    public StepperMotorController(Pin[] pins){
        this(pins[0], pins[1], pins[2], pins[3]);
    }

    public StepperMotorController(Pin pin1, Pin pin2, Pin pin3, Pin pin4){
        GpioPinDigitalOutput[] gpioPins = new GpioPinDigitalOutput[4];
        gpioPins[0] = gpio.provisionDigitalOutputPin(pin1, "blue/1", PinState.LOW);
        gpioPins[1] = gpio.provisionDigitalOutputPin(pin2, "pink/2", PinState.LOW);
        gpioPins[2] = gpio.provisionDigitalOutputPin(pin3, "yellow/3", PinState.LOW);
        gpioPins[3] = gpio.provisionDigitalOutputPin(pin4, "orange/4", PinState.LOW);

        this.motor = new GpioStepperMotorComponent(gpioPins);
        gpio.setShutdownOptions(true, PinState.LOW, gpioPins);
        motor.setStepInterval(2);
        motor.setStepsPerRevolution(2038);
        motor.setStepSequence(StepSequence.getDefault().getSequence());
    }

    public void setMotorState(MotorState state){
        motor.setState(state);
    }

    public void turn(boolean forward, long millis){
        if(forward){
            motor.forward(millis);
        } else {
            motor.reverse(millis);
        }
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
        
        public static StepSequence getDefault(){
            return SINGLE_STEP;
        }
    }

}
