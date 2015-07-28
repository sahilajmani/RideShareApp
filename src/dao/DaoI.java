package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import pojos.OTP;
import pojos.Pool;
import pojos.User;
import pojos.UserMapping;

public interface DaoI{
	public boolean insertOTPEmail(String userEmail, int otp);
	public boolean containsOTPforEmail(String userEmail);
	public boolean updateOTPEmail(String userEmail, int otp);
	public OTP getOPTbyEmail(String userEmail);
	public boolean insertUser(User user);
	public boolean updateUser(User user);
	public User getUserDetails(String userId);
	public List<Pool> matchedPool(String userId);
	public List<pojos.Transaction> getUserPoolRecord(String userId);
	Pool getPoolDetails(String poolId);
	boolean insertUpdateUser(User user);	
	
}
