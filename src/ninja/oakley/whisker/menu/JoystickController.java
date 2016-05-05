package ninja.oakley.whisker.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class JoystickController {

    private static final GpioController gpio = GpioFactory.getInstance();
    private final List<GpioPinDigitalInput> pins;
    private final Listener listener;
    
    public JoystickController(Pin right, Pin left, Pin top, Pin bottom){
        this.listener = new Listener();
        this.pins = new ArrayList<>();
        
        initDirection(right, Direction.RIGHT);
        initDirection(left, Direction.LEFT);
        initDirection(top, Direction.TOP);
        initDirection(bottom, Direction.BOTTOM);
    }
    
    public void destroy(){
        for(GpioPinDigitalInput pin : pins){
            pin.removeListener(listener);
        }
        pins.clear();
    }
    

    private void initDirection(Pin pin, Direction dir){
        GpioPinDigitalInput in = gpio.provisionDigitalInputPin(pin, PinPullResistance.PULL_DOWN);
        
        in.addListener(listener);
        listener.addDirection(pin, dir);
    }

    private class Listener implements GpioPinListenerDigital {

        private final HashMap<Pin, Direction> list;

        public Listener(){
            this.list = new HashMap<>();
        }

        @Override
        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
            if(event.getState() == PinState.HIGH){
                System.out.println(getDirection(event.getPin()));
            }
        }
        
        public void addDirection(Pin pin, Direction dir){
            if(!list.containsValue(dir)){
                list.put(pin, dir);
            }
        }
        
        private Direction getDirection(GpioPin pin){
            return list.get(pin.getPin());
        }
    }


}
