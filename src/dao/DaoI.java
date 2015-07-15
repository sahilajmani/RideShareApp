package dao;

import java.util.List;

import pojos.OTP;
import pojos.Pool;
import pojos.User;

public interface DaoI{
	public boolean insertOTPEmail(String userEmail, int otp);
	public boolean containsOTPforEmail(String userEmail);
	public boolean updateOTPEmail(String userEmail, int otp);
	public OTP getOPTbyEmail(String userEmail);
	public boolean insertUser(User user);
	public boolean updateUser(User user);
	public User getUserDetails(String userId);
	public List<Pool> matchedPool(String userId);
	public List<User> findMatchedUser(String userId);//will find users and their distane and will also persist them
	
}
