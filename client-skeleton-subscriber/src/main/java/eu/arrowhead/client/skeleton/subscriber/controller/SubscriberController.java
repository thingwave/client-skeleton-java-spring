package eu.arrowhead.client.skeleton.subscriber.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import eu.arrowhead.client.skeleton.subscriber.constants.SubscriberConstants;
import eu.arrowhead.common.CommonConstants;
import eu.arrowhead.common.dto.shared.EventDTO;

@RestController
public class SubscriberController {
	
	//=================================================================================================
	// members

	private final Logger logger = LogManager.getLogger(SubscriberController.class);
	
	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	@GetMapping(path = CommonConstants.ECHO_URI)
	public String echoService() {
		return "Got it!";
	}
	
	//-------------------------------------------------------------------------------------------------
	@PostMapping(path = SubscriberConstants.EVENT_NOTIFICATION_URI)
	public void receiveEvent(@RequestBody final EventDTO event ) {
		
		logger.debug("Received event");
		if( event.getEventType() == null) {
			
			logger.debug( "EventType is null." );
		}
	}
	
	//-------------------------------------------------------------------------------------------------
	//TODO: implement here your provider related REST end points
}
