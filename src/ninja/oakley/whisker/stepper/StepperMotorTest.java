package ninja.oakley.whisker.stepper;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.Gpio;

public class StepperMotorTest {

    private static final int[][] seq = new int[][]{
        {1,0,0,0}, 
        {1,1,0,0}, 
        {0,1,0,0}, 
        {0,1,1,0}, 
        {0,0,1,0}, 
        {0,0,1,1}, 
        {0,0,0,1}, 
        {1,0,0,1}};

        private static final GpioPinDigitalOutput[] pins = new GpioPinDigitalOutput[4];
        //private static final int[] pins = new int[4];

        public static void main(String[] args){ 
            GpioController gpio = GpioFactory.getInstance();

            pins[0] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "orange", PinState.LOW);
            pins[1] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "yellow", PinState.LOW);
            pins[2] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "pink", PinState.LOW);
            pins[3] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "blue", PinState.LOW);

            int step = 0;
            int finalLen = seq.length - 1;
            while(true){
                for(int x = 0; x < 4; x++){

                    /*int back = -1;
                    if(step > 0){
                        back = seq[step - 1][x];
                    }
                     */
                    int status = seq[step][x];

                    //if(back == -1 || back != status){
                    pins[x].setState(status == 1 ? true : false);
                    //Gpio.digitalWrite(pins[x].getPin().getAddress(), status == 1 ? true : false);
                    //x}
                }

                if(step == finalLen){ //Resets seq
                    step = 0;
                    continue;
                }

                step++;
            }
        }

}
