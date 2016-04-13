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

		private static final int[][] shortSeq = new int[][]{
			{1,1,0,0}, 
			{0,1,1,0}, 
			{0,0,1,1}, 
			{1,0,0,1}};

			private static final GpioPinDigitalOutput[] pins = new GpioPinDigitalOutput[4];
			private static final int[] wiringPins = {4, 3, 2, 0};


			public static void main(String[] args){ 
				GpioController gpio = GpioFactory.getInstance();

				pins[3] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "orange/1", PinState.LOW);
				pins[2] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "yellow/2", PinState.LOW);
				pins[1] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "pink/3", PinState.LOW);
				pins[0] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "blue/4", PinState.LOW);

				Gpio.wiringPiSetup();


				int step = 0;
				int finalLen = shortSeq.length - 1;
				while(true){
					for(int x = 0; x < 4; x++){
						int status = shortSeq[step][x];
						//pins[x].setState(status == 1 ? true : false);
						Gpio.digitalWrite(wiringPins[x], status == 1 ? true : false);


					}

					if(step == finalLen){ //Resets seq
						step = 0;
						continue;
					}

					step++;
				}
			}

}
