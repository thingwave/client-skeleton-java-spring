package eu.arrowhead.client.rpi.provider.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.arrowhead.common.CommonConstants;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

@RestController
public class ProviderController {
	
	//=================================================================================================
	// members

	//TODO: add your variables here
	private GpioController gpio = null;
        private GpioPinDigitalOutput pin = null;

	public ProviderController() {
		gpio = GpioFactory.getInstance();
                pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "GPIO1", PinState.HIGH);

                pin.setShutdownOptions(true, PinState.LOW);

	}

	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	@GetMapping(path = CommonConstants.ECHO_URI)
	public String echoService() {
		return "Got it!";
	}
	
	//-------------------------------------------------------------------------------------------------
	//TODO: implement here your provider related REST end points
}
