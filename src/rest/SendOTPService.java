package rest;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import pojos.OTP;
import pojos.OTPAuthenticationResponse;
import pojos.RestServiceResponse;
import pojos.User;
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
	public RestServiceResponse sendOTPEmail(User user) {
		String userEmail = user.getEmail();
		RestServiceResponse serviceResponse = new RestServiceResponse();
		serviceResponse.setResponse(false);
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
				} else {
					serviceResponse.setResponse(false);
				}
			} else {
				if (dao.insertOTPEmail(userEmail, otp)) {
					SendMail.sendEmail(GlobalConstants.FROM_EMAIL,
							GlobalConstants.PASSWORD_EMAIL, subject, message,
							to);
					serviceResponse.setResponse(true);
				} else {
					serviceResponse.setResponse(false);
				}
			}
		} else {
			serviceResponse.setResponse(false);
		}
		return serviceResponse;
	}

	@POST
	@Path("OTPAuthenticationService")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public OTPAuthenticationResponse OTPAuthentication(OTP otpObj) {		
		OTPAuthenticationResponse otpAuthenticationResponse = new OTPAuthenticationResponse();
		try{
		OTP otpObjectByEmail = new OTP();
		boolean response = false;
		if (null != otpObj && null != otpObj.getEmail()
				&& null != otpObj.getPasscode() && !otpObj.getEmail().isEmpty()
				&& !otpObj.getPasscode().isEmpty()) {
			otpObjectByEmail = dao.getOPTbyEmail(otpObj.getEmail());
			if (null != otpObjectByEmail
					&& otpObjectByEmail.getPasscode() != null
					&& !otpObjectByEmail.getPasscode().isEmpty()) {
				if (getOTPAuthentication(otpObj.getPasscode(), otpObjectByEmail)) {
					response = true;
				}
			}
		}
		User user = null;
		if (response) {
			user = dao.getUserDetailsByEmail(otpObj.getEmail());
			otpAuthenticationResponse.setUser(user);
		}
		otpAuthenticationResponse.setResponse(response);
		if(!response){
			System.out.println("DB otp " + otpObjectByEmail.getPasscode() + "\n User OTP " + otpObj.getPasscode());			
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return otpAuthenticationResponse;
	}

	private boolean getOTPAuthentication(String userOTP, OTP otpObjectByEmail) {
		if (userOTP.equalsIgnoreCase(otpObjectByEmail.getPasscode())) {
			Date currentTime = new Date();
			Date otpCreationTime = otpObjectByEmail.getCreate_time();
			long diffInMinutes = (currentTime.getTime() - otpCreationTime
					.getTime()) / 60000;
			if (diffInMinutes <= GlobalConstants.OTPPermissibleTimeInMinutes) {
				return true;
			}
		}
		return false;
	}

	private int generateOTP() {
		int OTP = (int) (1000 + Math.random() * 8999);
		return OTP;
	}
}
