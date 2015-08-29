package dao;

import java.util.List;

import pojos.MatchedPoolsVO;
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
	public List<MatchedPoolsVO> getmatchedPool(String userId);
	public List<Transactions> getUserPoolRecord(String userId);
	public Pool getPoolDetails(String poolId);
	boolean insertUser(User user);
	public boolean updateUser(User user,boolean changeAddress);
	public User getUserDetailsByEmail(String email);
	public List<PoolRequest> getIncomingPoolRequests(String userId);
	boolean updatePoolRequest(String requestId, int response);
	boolean leavePool(String userId, String poolId);
	public List<String> getOutgoingPoolRequests(String userId);
	public boolean joinPoolRequest(String userId, String poolId,float distance);
	void deleteUser(String userId);			
}
