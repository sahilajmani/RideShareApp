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
import utility.SmsSender;
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
			RideSharingUtil.updateOTP(userEmail, otp);
			String message = "Hi "+user.getName()+",\n Please enter - " + otp+"as OTP in "
					+ "Rideasy mobile app to proceed. Thanks,\n Team Rideasy,\n Keep Riding, Keep Sharing !";
			String subject = "Thankyou for downloading Rideasy app ! ";
			String[] to = { userEmail };
			SmsSender.getInstance().sendSms(user.getContact(), "OTP : "+otp+"\n Thanks for"
					+ " registering on our app. Keep Riding, Keep Sharing !");
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
		try {
			OTP otpObjectByEmail = new OTP();
			boolean response = false;
			if(null!= otpObj && RideSharingUtil.getOTP(otpObj.getEmail())> 0){
				if(RideSharingUtil.getOTP(otpObj.getEmail()) == otpObj.getPasscode()){
					System.out.println("Getting OTP   value from cache ");
					response = true;
				}
			}
			else {
               if (null != otpObj && null != otpObj.getEmail()
					&& otpObj.getPasscode() != 0
					&& !otpObj.getEmail().isEmpty()) {
				otpObjectByEmail = dao.getOPTbyEmail(otpObj.getEmail());
				if (null != otpObjectByEmail
						&& otpObjectByEmail.getPasscode() != 0) {
					System.out.println("Comparing passcodes now   "
							+ otpObjectByEmail);
					if (getOTPAuthentication(otpObj.getPasscode(),
							otpObjectByEmail)) {
						response = true;
					}
				}
			}
		}
			User user = null;
			if (response) {
				user = dao.getUserDetailsByEmail(otpObj.getEmail());
				otpAuthenticationResponse.setUser(user);
			}
			otpAuthenticationResponse.setResponse(response);
			if (!response) {
				System.out.println("DB otp " + otpObjectByEmail.getPasscode()
						+ "\n User OTP " + otpObj.getPasscode());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return otpAuthenticationResponse;
	}

	private boolean getOTPAuthentication(int userOTP, OTP otpObjectByEmail) {
		if (userOTP == otpObjectByEmail.getPasscode()) {
			Date currentTime = new Date();
			Date otpCreationTime = otpObjectByEmail.getCreate_time();
			long diffInMinutes = (currentTime.getTime() - otpCreationTime
					.getTime()) / 60000;
			System.out.println("Time Diff is --   " + diffInMinutes);
			if (diffInMinutes <= GlobalConstants.OTPPermissibleTimeInMinutes) {
				return true;
			}
		}
		System.out.println("Returning false in get OTP Authentications");
		return false;
	}

	private int generateOTP() {
		int OTP = (int) (1000 + Math.random() * 8999);
		return OTP;
	}
}
