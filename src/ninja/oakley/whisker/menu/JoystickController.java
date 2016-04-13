package ninja.oakley.whisker.menu;

import java.util.HashMap;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.wiringpi.Gpio;

public class JoystickController {

    private GpioController gpio = GpioFactory.getInstance();
    private HashMap<Direction, Side> sides = new HashMap<>();
    
    public JoystickController(Pin right, Pin left, Pin top, Pin bottom){
        Gpio.wiringPiSetup();
        initDirection(right, Direction.RIGHT);
        initDirection(left, Direction.LEFT);
        initDirection(top, Direction.TOP);
        initDirection(bottom, Direction.BOTTOM);
    }
    
    public void destroy(){
        for(Side s : sides.values()){
            s.getPin().removeListener(s);
        }
        sides.clear();
    }
    

    private void initDirection(Pin pin, Direction dir){
        GpioPinDigitalInput in = gpio.provisionDigitalInputPin(pin, PinPullResistance.PULL_DOWN);
        Side side = new Side(in, dir);
        
        in.addListener(side);
        sides.put(dir, side);
    }

    private class Side implements GpioPinListenerDigital {

        private Direction dir;
        private GpioPinDigitalInput pin;

        public Side(GpioPinDigitalInput pin, Direction dir){
            this.pin = pin;
            this.dir = dir;
        }

        @Override
        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
            if(event.getState() == PinState.HIGH){
                System.out.println(getDirection());            
            }
        }

        public Direction getDirection(){
            return dir;
        }
        
        public GpioPinDigitalInput getPin(){
            return pin;
        }
    }

    public enum Direction {
        RIGHT,
        LEFT,
        TOP,
        BOTTOM;
    }
}
