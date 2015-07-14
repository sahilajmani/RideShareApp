package dao;

import pojos.OTP;
import pojos.User;

public interface DaoI{
	public boolean insertOTPEmail(String userEmail, int otp);
	public boolean containsOTPforEmail(String userEmail);
	public boolean updateOTPEmail(String userEmail, int otp);
	public OTP getOPTbyEmail(String userEmail);
	public boolean insertUser(User user);
	public boolean updateUser(User user);
	public User getUserDetails(String userId);
}
