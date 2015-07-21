package rest;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import pojos.OTP;
import pojos.RestServiceResponse;
import utility.GlobalConstants;
import utility.RideSharingUtil;
import dao.DaoI;
import email.SendMail;

@Path("/otp")
public class SendOTPService {
	DaoI dao = RideSharingUtil.getDaoInstance();

	@POST
	@Path("sendOTPService")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RestServiceResponse sendOTPEmail(String userEmail) {
		RestServiceResponse serviceResponse = new RestServiceResponse();
		if (!userEmail.isEmpty()) {
			int otp = generateOTP();
			String message = "OTP : " + otp;
			String subject = "OTP for Ride Easy";
			String[] to = { userEmail };
			if (dao.containsOTPforEmail(userEmail)) {
				if (dao.updateOTPEmail(userEmail, otp)) {
					SendMail.sendEmail(GlobalConstants.FROM_EMAIL,
							GlobalConstants.PASSWORD_EMAIL, subject, message,
							to);
					serviceResponse.setResponse(true);
				}
			} else {
				if (dao.insertOTPEmail(userEmail, otp)) {
					SendMail.sendEmail(GlobalConstants.FROM_EMAIL,
							GlobalConstants.PASSWORD_EMAIL, subject, message,
							to);
					serviceResponse.setResponse(true);
				}
			}
			serviceResponse.setResponse(true);
		} else {
			serviceResponse.setResponse(true);
		}
		return serviceResponse;
	}

	@POST
	@Path("OTPAuthenticationService")
	@Produces(MediaType.TEXT_PLAIN)
	public String OTPAuthentication(@FormParam("userEmail") String userEmail,
			@FormParam("OTP") String userOTP) {
		OTP otpObjectByEmail = dao.getOPTbyEmail(userEmail);
		return getOTPAuthentication(userOTP, otpObjectByEmail);
	}

	private String getOTPAuthentication(String userOTP, OTP otpObjectByEmail) {
		if (userOTP.equalsIgnoreCase(otpObjectByEmail.getPasscode())) {
			Date currentTime = new Date();
			Date otpCreationTime = otpObjectByEmail.getCreate_time();
			long diffInMinutes = (currentTime.getTime() - otpCreationTime
					.getTime()) / 60000;
			if (diffInMinutes <= GlobalConstants.OTPPermissibleTimeInMinutes) {
				return "true and difference is : " + diffInMinutes;
			}
		}
		return "false";
	}

	private int generateOTP() {
		int OTP = (int) (Math.random() * 10000);
		return OTP;
	}
}
