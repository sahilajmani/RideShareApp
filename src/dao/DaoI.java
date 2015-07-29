package dao;

import java.util.List;

import pojos.OTP;
import pojos.Pool;
import pojos.PoolRequest;
import pojos.Transactions;
import pojos.User;

public interface DaoI{
	public boolean insertOTPEmail(String userEmail, int otp);
	public boolean containsOTPforEmail(String userEmail);
	public boolean updateOTPEmail(String userEmail, int otp);
	public OTP getOPTbyEmail(String userEmail);
	public User getUserDetails(String userId);
	public List<Pool> matchedPool(String userId);
	public List<Transactions> getUserPoolRecord(String userId);
	public Pool getPoolDetails(String poolId);
	public boolean insertUpdateUser(User user);
	public List<PoolRequest> getPoolRequests(String userId);
	
}
