package ninja.oakley.whisker.hardware;

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
    private final List<JoystickListener> joystickListeners;
    private final GpioListener gpioListener;

    public JoystickController(Pin right, Pin left, Pin top, Pin bottom){
        this.gpioListener = new GpioListener();
        this.pins = new ArrayList<>();
        this.joystickListeners = new ArrayList<>();

        initDirection(right, Direction.RIGHT);
        initDirection(left, Direction.LEFT);
        initDirection(top, Direction.TOP);
        initDirection(bottom, Direction.BOTTOM);
    }

    public void registerListener(JoystickListener listener){
        joystickListeners.add(listener);
    }

    public void removeListener(JoystickListener listener){
        joystickListeners.remove(listener);
    }

    public void destroy(){
        for(GpioPinDigitalInput pin : pins){
            pin.removeListener(gpioListener);
        }
        pins.clear();
    }

    private void initDirection(Pin pin, Direction dir){
        GpioPinDigitalInput in = gpio.provisionDigitalInputPin(pin, PinPullResistance.PULL_DOWN);
        pins.add(in);
        
        in.addListener(gpioListener);
        gpioListener.addDirection(pin, dir);
    }

    private class GpioListener implements GpioPinListenerDigital {

        private final HashMap<Pin, Direction> list;

        public GpioListener(){
            this.list = new HashMap<>();
        }

        @Override
        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
            if(event.getState() == PinState.HIGH){
                Direction dir = getDirection(event.getPin());

                for(JoystickListener l : joystickListeners){
                    l.execute(dir);
                }
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

    public interface JoystickListener { 
        public void execute(Direction dir);
    }

}
