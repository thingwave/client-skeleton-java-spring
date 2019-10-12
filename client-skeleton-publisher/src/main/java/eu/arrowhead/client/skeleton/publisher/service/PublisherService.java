package eu.arrowhead.client.skeleton.publisher.service;

import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import eu.arrowhead.client.library.ArrowheadService;
import eu.arrowhead.client.library.util.ClientCommonConstants;
import eu.arrowhead.client.skeleton.publisher.PublisherApplicationInitListener;
import eu.arrowhead.client.skeleton.publisher.event.PresetEventType;
import eu.arrowhead.common.Utilities;
import eu.arrowhead.common.dto.shared.EventPublishRequestDTO;
import eu.arrowhead.common.dto.shared.SystemRequestDTO;
import eu.arrowhead.common.exception.ArrowheadException;
import eu.arrowhead.common.exception.AuthException;
import eu.arrowhead.common.exception.BadPayloadException;
import eu.arrowhead.common.exception.InvalidParameterException;
import eu.arrowhead.common.exception.UnavailableServerException;

@Service
public class PublisherService {
    //=================================================================================================
	// members
	
	@Value(ClientCommonConstants.$CLIENT_SYSTEM_NAME)
	private String clientSystemName;
	
	@Value(ClientCommonConstants.$CLIENT_SERVER_ADDRESS_WD)
	private String clientSystemAddress;
	
	@Value(ClientCommonConstants.$CLIENT_SERVER_PORT_WD)
	private int clientSystemPort;
	
	@Autowired
	ArrowheadService arrowheadService;
	
	private final Logger logger = LogManager.getLogger(PublisherApplicationInitListener.class);
	
	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	//Sample implementation of event publishing of preset event types
	/**
	 * Sends a http(s) 'publish' request to Event Handler Core System.
	 * 
	 * @param eventType PresetEventType which represents the required eventType enum
	 * @param metadata Map<String, String> which represents the nullable metadata map
	 * @param payload String which represents the required payload String
	 * @throws AuthException when you are not authorized by Event Handler Core System
	 * @throws BadPayloadException when the payload couldn't be validated by Event Handler Core System 
	 * @throws InvalidParameterException when the payload content couldn't be validated by Event Handler Core System
	 * @throws ArrowheadException when internal server error happened at Event Handler Core System
	 * @throws UnavailableServerException when Event Handler Core System is not available
	 */
	public void publish( final PresetEventType eventType, final Map<String, String> metadata, final String payload ) {
		
		final EventPublishRequestDTO request = getPublishRequest( eventType, metadata, payload );
		arrowheadService.publishToEventHandler( request );
	}
	
	//=================================================================================================
	// assistant methods

	//-------------------------------------------------------------------------------------------------
	private EventPublishRequestDTO getPublishRequest(final PresetEventType eventType, final Map<String, String> metadata, final String payload) {
		logger.debug( "getPublishRequest started..." );
		
		final String timeStamp = Utilities.convertZonedDateTimeToUTCString( ZonedDateTime.now() );
		
		final EventPublishRequestDTO publishRequestDTO = new EventPublishRequestDTO(
				eventType.getEventTypeName(), 
				getSource(), 
				metadata, 
				payload, 
				timeStamp);
		
		return publishRequestDTO;
	}

	//-------------------------------------------------------------------------------------------------
	private SystemRequestDTO getSource() {
		logger.debug( "getSource started..." );
		
		final SystemRequestDTO source = new SystemRequestDTO();
		source.setSystemName( clientSystemName );
		source.setAddress( clientSystemAddress );
		source.setPort( clientSystemPort );
		source.setAuthenticationInfo( Base64.getEncoder().encodeToString( arrowheadService.getMyPublicKey().getEncoded() ) );
		
		return source;

	}
	
	
}
