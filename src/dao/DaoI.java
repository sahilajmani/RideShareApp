package dao;

import java.util.List;

import pojos.ListWalletTransactions;
import pojos.MatchedPoolsVO;
import pojos.OTP;
import pojos.OTPbySMS;
import pojos.Pool;
import pojos.PoolRequest;
import pojos.Transactions;
import pojos.User;
import vo.UserIdPoolIdVO;

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
	public User updateUser(User user,boolean changeAddress);
	public User getUserDetailsByEmail(String email);
	public List<PoolRequest> getIncomingPoolRequests(String userId);
	boolean updatePoolRequest(String requestId, int response);
	boolean leavePool(String userId, String poolId);
	public List<PoolRequest> getOutgoingPoolRequests(String userId);
	public boolean joinPoolRequest(UserIdPoolIdVO userIdPoolIdVO, float distance);
	void deleteUser(String userId);
	public List<User> fetchPoolParticipants(String poolId);
	public PoolRequest getPoolRequestVO(UserIdPoolIdVO userIdPoolIdVO);
	public ListWalletTransactions getWalletTransactionHistory(String userId);
	boolean containsOTPforSMS(String number);
	boolean updateOTPSMS(String number, int otp);
	public boolean insertOTPSMS(String number, int otp);
	public OTPbySMS getOPTbySMS(String number);
	User getUserDetailsByNumber(String number);
}
