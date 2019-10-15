package eu.arrowhead.client.skeleton.subscriber.constants;

public class SubscriberConstants {
	//=================================================================================================
	// members
	public static final String PRESET_NOTIFICATION_URI = "preset_notification_uris";
	public static final String $PRESET_NOTIFICATION_URI_WD = "${" + PRESET_NOTIFICATION_URI  + ":" + SubscriberDefaults.DEFAULT_EVENT_NOTIFICATION_URI  + "}";
	public static final String PRESET_EVENT_TYPES = "preset_events";
	public static final String $PRESET_EVENT_TYPES_WD = "${" + PRESET_EVENT_TYPES + ":" + SubscriberDefaults.DEFAULT_PRESET_EVENT_TYPES + "}";
	
	//=================================================================================================
	// assistant methods

	//-------------------------------------------------------------------------------------------------
	private SubscriberConstants() {
		throw new UnsupportedOperationException();
	}
}
