package eu.arrowhead.client.skeleton.subscriber;

import java.util.Base64;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import eu.arrowhead.client.library.ArrowheadService;
import eu.arrowhead.client.library.util.ClientCommonConstants;
import eu.arrowhead.client.skeleton.subscriber.constants.SubscriberConstants;
import eu.arrowhead.client.skeleton.subscriber.constants.SubscriberDefaults;
import eu.arrowhead.common.CommonConstants;
import eu.arrowhead.common.dto.shared.SubscriptionRequestDTO;
import eu.arrowhead.common.dto.shared.SystemRequestDTO;
import eu.arrowhead.common.exception.InvalidParameterException;

@SpringBootApplication
@ComponentScan(basePackages = {CommonConstants.BASE_PACKAGE}) //TODO: add custom packages if any
public class SubscriberMain implements ApplicationRunner {

    //=================================================================================================
	// members
	
	@Value( SubscriberConstants.$PRESET_EVENT_TYPES_WD )
	private String presetEvents;
	
	@Value(ClientCommonConstants.$CLIENT_SYSTEM_NAME)
	private String clientSystemName;
	
	@Value(ClientCommonConstants.$CLIENT_SERVER_ADDRESS_WD)
	private String clientSystemAddress;
	
	@Value(ClientCommonConstants.$CLIENT_SERVER_PORT_WD)
	private int clientSystemPort;
	
	@Autowired
	ArrowheadService arrowheadService;
	
	private final Logger logger = LogManager.getLogger(SubscriberApplicationInitListener.class);
	
	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	public static void main(final String[] args) {
		SpringApplication.run(SubscriberMain.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {

		subscribeToPresetEvents();
		
	}
	
	//=================================================================================================
	// assistant methods

	//-------------------------------------------------------------------------------------------------
	private void subscribeToPresetEvents() {
		if( presetEvents == null) {
			
			logger.info("No preset events to subscribe.");
		} else {
			
			final String[] eventTypes = presetEvents.split(",");
			
			final SystemRequestDTO subscriber = new SystemRequestDTO();
			subscriber.setSystemName( clientSystemName );
			subscriber.setAddress( clientSystemAddress );
			subscriber.setPort( clientSystemPort );
			subscriber.setAuthenticationInfo( Base64.getEncoder().encodeToString( arrowheadService.getMyPublicKey().getEncoded()) );
			
			
			for ( final String eventType : eventTypes ) {
				
				arrowheadService.unsubscribeFromEventHandler(eventType, clientSystemName, clientSystemAddress, clientSystemPort);
				
			}
			
			for ( final String eventType : eventTypes ) {
				
				final SubscriptionRequestDTO subscription = new SubscriptionRequestDTO(
						eventType.toUpperCase(), 
						subscriber, 
						null, 
						SubscriberDefaults.DEFAULT_EVENT_NOTIFICATION_BASE_URI, 
						false, 
						null, 
						null, 
						null);
				
				try {
					arrowheadService.subscribeToEventHandler( subscription );
				
				} catch ( InvalidParameterException ex) {
					
					if( ex.getMessage().contains( "Subscription violates uniqueConstraint rules" )) {
						
						logger.debug("Subscription is allready in DB");
					}
				} catch ( Exception ex) {
					
					logger.debug("Could not subscribe to EventType: " + subscription.getEventType());
				} 
				
			}
		}
	}
	
}
