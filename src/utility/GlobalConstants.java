package utility;

public class GlobalConstants {
	public static final String FROM_EMAIL = "no-reply@rideasy.co.in" ;
	public static final String PASSWORD_EMAIL = "Rideeasy@123" ;
	public static final String HIBERNATE_CONFIG_XML = "resources/hibernate.cfg.xml" ;
	public static final long OTPPermissibleTimeInMinutes = 30;
	public static final int REQUEST_ACCEPTED = 1;
	public static final int REQUEST_REJECTED = 0;		
	public static final int REQUEST_PENDING = 2;
	public static final int REQUEST_CANCEL = 3;
	public static final int JOIN_PENDING = 4;
	public static final int INVALID_REQUEST = 5;
	public static final long NOTIFICATION_CHAT_TIMEOUT=120000L;
}
