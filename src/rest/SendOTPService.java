package rest;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import utility.GlobalConstants;
import email.SendMail;


@Path("/otp")
public class SendOTPService {
	
	@POST
	@Path("sendOTPService")
	@Produces(MediaType.TEXT_PLAIN)
	public String sendOTPEmail(@FormParam("userEmail") String userEmail){
		int OTP = generateOTP();
		String message = "OTP : " + OTP ;
		String subject = "OTP for Ride Easy" ;
		String[] to = {userEmail};
		SendMail.sendEmail(GlobalConstants.FROM_EMAIL, GlobalConstants.PASSWORD_EMAIL, subject, message, to);
		//Store OTP against email in DB logic to be written 		
		return "mail sent to " + userEmail + " with OTP : " + OTP;
	}
	
	@POST
	@Path("OTPAuthenticationService")
	@Produces(MediaType.TEXT_PLAIN)
	public String OTPAuthentication(@FormParam("userEmail") String userEmail,@FormParam("OTP") int OTP){
		return getOTPAuthentication(userEmail,OTP);		
	}
	private String getOTPAuthentication(String userEmail, int oTP) {
		//fetch from DB the the OTP for the provided email.
		//if that OTP and user entered OTP match, then return true, else return false
		return "true";
	}

	private int generateOTP() {
		int OTP = (int)(Math.random()*10000);
		return OTP;
	}
}
