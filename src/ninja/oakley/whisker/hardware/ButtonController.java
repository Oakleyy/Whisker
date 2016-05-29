package ninja.oakley.whisker.hardware;

import java.util.ArrayList;
import java.util.List;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;


public class ButtonController {

    private static final GpioController gpio = GpioFactory.getInstance();
    private final List<GpioPinDigitalInput> pins;
    private final List<ButtonListener> buttonListeners;
    private final GpioListener gpioListener;
    
    public ButtonController(Pin pin){
        this.gpioListener = new GpioListener();
        this.pins = new ArrayList<>();
        this.buttonListeners = new ArrayList<>();

        init(pin);

    }
    
    public void registerListener(ButtonListener listener){
        buttonListeners.add(listener);
    }

    public void removeListener(ButtonListener listener){
        buttonListeners.remove(listener);
    }
    
    private void init(Pin pin){
        GpioPinDigitalInput in = gpio.provisionDigitalInputPin(pin, PinPullResistance.PULL_DOWN);
        pins.add(in);
        in.addListener(gpioListener);
        gpioListener.addButton(pin);
    }
    
    public void destroy(){
        for(GpioPinDigitalInput pin : pins){
            pin.removeListener(gpioListener);
        }
        pins.clear();
    }
    
    private class GpioListener implements GpioPinListenerDigital {

        private final List<Pin> list;

        public GpioListener(){
            this.list = new ArrayList<>();
        }

        @Override
        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
            if(event.getState() == PinState.HIGH){

                for(ButtonListener l : buttonListeners){
                    l.execute();
                }
            }
        }

        public void addButton(Pin pin){
            if(!list.contains(pin)){
                list.add(pin);
            }
        }
    }
    
    public interface ButtonListener { 
        public void execute();
    }
}
