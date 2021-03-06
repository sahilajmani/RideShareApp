package dao;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import pojos.ListWalletTransactions;
import pojos.MailNotifierThread;
import pojos.MatchedPoolsVO;
import pojos.OTP;
import pojos.Pool;
import pojos.PoolRequest;
import pojos.TransactionType;
import pojos.Transactions;
import pojos.User;
import pojos.UserMapping;
import pojos.WalletTransactions;
import utility.GlobalConstants;
import utility.RideSharingUtil;
import utility.UserMatching;
import utility.WalletUtil;
import vo.UserIdPoolIdVO;
import email.SendMail;

public class DaoImpl implements DaoI {
	SessionFactory sessionFactory = RideSharingUtil.getSessionFactoryInstance();
	Logger logger = Logger.getLogger("debug");

	public boolean insertOTPEmail(String userEmail, int otp) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		OTP otpObject = new OTP();
		otpObject.setEmail(userEmail);
		logger.info("Email : " + userEmail);
		otpObject.setPasscode(otp);
		logger.info("OTP : " + otp);
		try {
			otpObject.setCreate_time(this.getCurrentTime());
			session.save(otpObject);
			logger.info("Commit Successful");
			tx.commit();
			return true;
		} catch (ParseException e) {
			logger.info("Parse Exception Occured " + e.getStackTrace());
			return false;
		} finally {
			session.close();
		}
	}

	private Date getCurrentTime() throws ParseException {
		Date date = new Date();
		System.out.println("Date" + date);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.parse(sdf.format(date));
	}

	@Override
	public boolean containsOTPforEmail(String userEmail) {
		Session session = sessionFactory.openSession();
		String hql = "from OTP otp where otp.email=?";
		Query qry = session.createQuery(hql);
		qry.setString(0, userEmail);
		List<OTP> lst = qry.list();
		session.close();
		if (lst != null && lst.size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean updateOTPEmail(String userEmail, int otp) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Criteria cr = session.createCriteria(OTP.class);
		cr.add(Restrictions.eq("email", userEmail));
		OTP otpObjectByEmail = (OTP) cr.list().get(0);
		otpObjectByEmail.setPasscode(otp);
		logger.info("OTP : " + otp);
		try {
			otpObjectByEmail.setCreate_time(this.getCurrentTime());
			session.update(otpObjectByEmail);
			tx.commit();
			logger.info("Update Successful");
			return true;
		} catch (ParseException e) {
			logger.info("Parse Exception Occured " + e.getStackTrace());
			return false;
		} finally {
			session.close();
		}
	}

	@Override
	public OTP getOPTbyEmail(String userEmail) {
		Session session = sessionFactory.openSession();
		Criteria cr = session.createCriteria(OTP.class);
		cr.add(Restrictions.eq("email", userEmail));
		OTP otpObjectByEmail = new OTP();
		if (cr.list() != null && cr.list().size() > 0) {
			otpObjectByEmail = (OTP) cr.list().get(0);
		}
		if(otpObjectByEmail == null){
			session.clear();
			cr = session.createCriteria(OTP.class);
			cr.add(Restrictions.eq("email", userEmail));
			if (cr.list() != null && cr.list().size() > 0) {
				otpObjectByEmail = (OTP) cr.list().get(0);
			}
		logger.info("OTP OBJECT BY EMAIL is NULL "+otpObjectByEmail);
		}else
		{
			logger.info("OTP OBJECT BY EMAIL is  "+otpObjectByEmail.getEmail() +"\t"+otpObjectByEmail.getPasscode());
		}
		session.close();
		return otpObjectByEmail;
	}

	public boolean deleteUserOnly(String userId) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			String hql = "delete from User where user.id=?";
			Query qry = session.createQuery(hql);
			qry.setString(0, userId);
			qry.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			return false;
		} finally {
			session.close();
		}
		return true;
	}

	
	
	public boolean deleteMatchedUsers(String userId) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			String hql = "delete from UserMapping where userA.id=? or userB.id='"+userId+"'";
			Query qry = session.createQuery(hql);
			qry.setString(0, userId);
			qry.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			return false;
		} finally {
			session.close();
		}
		return true;
	}

	private Pool createPool(User user) {
		Pool pool = new Pool();
		pool.setId(user.getId());
		pool.setReachDestinationTime(user.getReachDestinationTime());
		pool.setReachDestinationTimeInMilliseconds(user
				.getReachDestinationTimeInMilliseconds());
		pool.setLeaveDestinationTime(user.getLeaveDestinationTime());
		pool.setLeaveDestinationTimeInMilliseconds(user
				.getLeaveDestinationTimeInMilliseconds());
		// pool.setHostUserId(user.getId());
		pool.setSourceAddress(user.getHomeAddress());
		pool.setDestinationAddress(user.getOfficeAddress());
		pool.setIs_active(true);
		pool.setIsAvailable(true);
		pool.setNumberOfMembers(1); // to be made dynamic
		if(user.getMembers()==null){
			pool.setMax_members(4);
		}else{
		pool.setMax_members(user.getMembers());
		}
		return pool;
	}

	@Override
	public User getUserDetails(String userId) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Criteria cr = session.createCriteria(User.class);
		cr.add(Restrictions.eq("id", userId));
		User userVO = null;
		if (cr.list() != null && cr.list().size() > 0) {
			userVO = (User) cr.list().get(0);
		}
		tx.commit();
		session.close();
		return userVO;
	}

	@Override
	public Pool getPoolDetails(String poolId) {
		Session session = sessionFactory.openSession();
		Criteria cr = session.createCriteria(Pool.class);
		cr.add(Restrictions.eq("id", poolId));
		Pool poolVO = null;
		if (cr.list() != null && cr.list().size() > 0) {
			poolVO = (Pool) cr.list().get(0);
		}
		session.close();
		return poolVO;
	}

	@Override
	public List<MatchedPoolsVO> getmatchedPool(String userId) {

		// List of pools returned for particular user.
		Session session = sessionFactory.openSession();
		 User currentUser = this.getUserDetails(userId);
		String hql = "select user.pool.id,min(um.distance),user.name,user.poolCost from User user,UserMapping um where um.userA.id='"
				+ userId
				+ "' and um.userB.id=user.id"
				+ " and user.pool.isAvailable="
				+ true
				+ " and user.pool.id<>'"
				+ userId + "' and user.pool.id<>'"
						+ currentUser.getPool().getId()
				+ "'  and user.id = user.pool.id group by  user.pool.id "
				+ "order by min(um.distance)";
		Query qry = session.createQuery(hql);
		// @SuppressWarnings("unchecked")
		// List<Pool> userPools = qry.list();
		List<Object[]> result = (List<Object[]>) qry.list();
		List<MatchedPoolsVO> matchedPools = new ArrayList<MatchedPoolsVO>();
		for (Object[] results : result) {
			String poolId = ((String) results[0]);
			Float distance = (Float) results[1];
			String name = ((String) results[2]);
			Integer poolCost = ((Integer) results[3]);	
			// System.out.println(pool.getId() + "  " + distance);// able to get
			// pool and
			// distance.put
			// it in java
			// element
			Pool newPool = this.getPoolDetails(poolId);
			MatchedPoolsVO matchedPool = new MatchedPoolsVO();
			matchedPool.setPool(newPool);
			matchedPool.setDistance(distance.toString());
			matchedPool.setName(name);
			matchedPool.setPoolCost(poolCost);
			matchedPools.add(matchedPool);
		}

		// for(Pool pool:userPools)
		// System.out.println(pool.getId());
		session.close();
		return matchedPools;

	}

	private List<UserMapping> findMatchedUser(User currentUser) {
		Session session = sessionFactory.openSession();
//		User currentUser = this.getUserDetails(userId);
		System.out.println("home: "+currentUser.getHomeAddress().getLattitude()+"   distance "+currentUser.getDistance()); // remove when goes in production
		if (null != currentUser && null != currentUser.getHomeAddress()
				&& null != currentUser.getOfficeAddress()
				&& currentUser.getHomeAddress().getLattitude() != 0.0
				&& currentUser.getHomeAddress().getLongitude() != 0.0
				&& currentUser.getOfficeAddress().getLattitude() != 0.0
				&& currentUser.getOfficeAddress().getLongitude() != 0.0) {
			String hql = "from User user where user.id<>'" + currentUser.getId()
					+ "' and (user.homeAddress.lattitude between "
					+ (currentUser.getHomeAddress().getLattitude() - 1)
					+ " and "
					+ (currentUser.getHomeAddress().getLattitude() + 1)
					+ ") and (user.homeAddress.longitude between "
					+ (currentUser.getHomeAddress().getLongitude() - 1)
					+ " and "
					+ (currentUser.getHomeAddress().getLongitude() + 1)
					+ ") and (user.officeAddress.longitude between "
					+ (currentUser.getOfficeAddress().getLongitude() - 1)
					+ " and "
					+ (currentUser.getOfficeAddress().getLongitude() + 1)
					+ ") and (user.officeAddress.lattitude between "
					+ (currentUser.getOfficeAddress().getLattitude() - 1)
					+ " and "
					+ (currentUser.getOfficeAddress().getLattitude() + 1) + ")";

			Query qry = session.createQuery(hql);

			List<User> userList = qry.list();
			session.close();
			System.out.println(userList.size());
			UserMatching userMatch = new UserMatching();
			try {
				return userMatch.getMatchedUsers(userList, currentUser);// will
																		// come
																		// from
				// DB
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (ProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;

	}

	private void persistUserMatch(List<UserMapping> userMapping) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			if (userMapping != null && userMapping.size() > 0) {
				for (UserMapping userMatch : userMapping) {
					session.save(userMatch);
				}
			}
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			session.close();
		}
	}

	public List<Transactions> getUserPoolRecord(String userId) { // pool
																	// transaction
																	// history
																	// of user -
																	// checked

		Session session = sessionFactory.openSession();
		String hql = "from Transactions where user.id='" + userId + "'";
		Query qry = session.createQuery(hql);
		List<Transactions> transactionRecord = qry.list();
		session.close();
		return transactionRecord;
	}

	@Override
	public User getUserDetailsByEmail(String email) {
		Session session = sessionFactory.openSession();
		Criteria cr = session.createCriteria(User.class);
		cr.add(Restrictions.eq("email", email));
		User userVO = null;
		if (cr.list() != null && cr.list().size() > 0) {
			userVO = (User) cr.list().get(0);
			try{
			userVO.setReachDestinationTimeInMilliseconds(String.valueOf(userVO
					.getReachDestinationTime().getTime()));
			userVO.setLeaveDestinationTimeInMilliseconds(String.valueOf(userVO
					.getLeaveDestinationTime().getTime()));
			}catch(NullPointerException e){
				return userVO;
			}
		} else {
			userVO = new User();
			userVO.setName("doesntexist");
		}
		session.close();
		return userVO;
	}

	@Override
	public List<PoolRequest> getOutgoingPoolRequests(String userId) { // CHECKED
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String hql = "from PoolRequest where (user.id=? and status<>?) or (pool.id=? and status=?)";
		Query qry = session.createQuery(hql);
		qry.setString(0, userId);
		qry.setInteger(1, GlobalConstants.JOIN_PENDING);
		qry.setString(2, userId);
		qry.setInteger(3, GlobalConstants.JOIN_PENDING);
		List<PoolRequest> userPoolRequest = qry.list();
		// for(PoolRequest individual : userPoolRequest)
		// {
		// System.out.println(individual.getId());
		// System.out.println(individual.getStatus());
		// System.out.println(individual.getPool().getId());
		// System.out.println(individual.getUser().getId());
		// System.out.println(individual.getDescription());
		//
		// }
		tx.commit();
		session.close();
		return userPoolRequest;
	}

	@Override
	public List<PoolRequest> getIncomingPoolRequests(String userId) {
		Session session = sessionFactory.openSession();
		Transaction tx= session.beginTransaction();
		String hql = "from PoolRequest where (pool.id=? and status =?) or (user.id=? and status=?)";
		Query qry = session.createQuery(hql);
		qry.setString(0, userId);
		qry.setInteger(1, GlobalConstants.REQUEST_PENDING);
		qry.setString(2, userId);
		qry.setInteger(3, GlobalConstants.JOIN_PENDING);	
		List<PoolRequest> userPoolRequest = qry.list();
		tx.commit();
		session.close();
		return userPoolRequest;
	}

	@Override
	public boolean joinPoolRequest(UserIdPoolIdVO userIdPoolIdVO, float distance) { //not using distance variable,setting distance from 
		User user = this.getUserDetails(userIdPoolIdVO.getUserId());
		Pool pool = this.getPoolDetails(userIdPoolIdVO.getPoolId());
		List<User> participants = new ArrayList<User>();
		int status;
		if (userIdPoolIdVO.getStatus() != 0) {
			status = userIdPoolIdVO.getStatus();
		} else {
			status = GlobalConstants.REQUEST_PENDING; // why this?? will never go or request should be invalid or error should be thrown
		}
	/*	if (status == GlobalConstants.JOIN_PENDING && !user.isHasCar()
				&& this.getPoolDetails(user.getId()).getNumberOfMembers() >= 1) {
			Session session = sessionFactory.openSession();
			String hql = "from UserMapping um where um.userA.id='"
					+ pool.getId() + "' and um.userB.pool.id='" + user.getId()
					+ "'";
			Query qry = session.createQuery(hql);
			try {
				List<UserMapping> userMap = qry.list();
				for (UserMapping userMatch : userMap) {
					PoolRequest poolRequest = new PoolRequest();
					poolRequest.setStatus(status);
					Date date = new Date();
					Timestamp time = new java.sql.Timestamp(date.getTime());
					poolRequest.setUpdated(getSystemTimeMilisGMT());
					poolRequest.setCreated(getSystemTimeMilisGMT());

					poolRequest.setPool(pool);
					poolRequest.setUser(userMatch.getUserB());
					poolRequest.setDistance(userMatch.getDistance());
					User userPool = this.getUserDetails(pool.getId());
					if(status != 4){
					String message = "Hi "+userPool.getName()+"\n "+user.getName()+
							 " has requested to join your car pool. Please open our app and "
							 + " go to requests page respond to "+user.getName()+"'s request. You can also"
							 		+ " chat with the user. Thanks. \n Team Ride Easy.\n Keep Riding, Keep Sharing !";
					String subject = user.getName()+" wants to join your car pool !";
					String[] to = { userPool.getEmail() };
					
							SendMail.sendEmail(GlobalConstants.FROM_EMAIL,
									GlobalConstants.PASSWORD_EMAIL, subject, message,
									to);
					}else
					{
						String message = "Hi "+user.getName()+"\n "+userPool.getName()+
								 " has sent you an add to pool request. Please open our app and "
								 + " go to requests page respond to "+userPool.getName()+"'s request. You can also"
								 		+ " chat with the user. Thanks. \n Team Ride Easy. Keep Riding, Keep Sharing !";
						String subject = userPool.getName()+" has sent you an add to pool request !";
						String[] to = { user.getEmail() };
						
								SendMail.sendEmail(GlobalConstants.FROM_EMAIL,
										GlobalConstants.PASSWORD_EMAIL, subject, message,
										to);
						
					}
							

				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				session.close();
			}

		} else {
*/
			PoolRequest poolRequest = new PoolRequest();
			poolRequest.setStatus(status);
			Date date = new Date();
			Timestamp time = new java.sql.Timestamp(date.getTime());
			poolRequest.setUpdated(getSystemTimeMilisGMT());
			poolRequest.setCreated(getSystemTimeMilisGMT());

			poolRequest.setPool(pool);
			poolRequest.setUser(user);
			poolRequest.setDistance(userIdPoolIdVO.getDistance());

			Session session = sessionFactory.openSession();
			Transaction tx1 = session.beginTransaction();
			String hql = "delete from PoolRequest where pool.id='"
					+ pool.getId() + "' and  user.id='" + user.getId() + "'";
			Query query = session.createQuery(hql);
			int rows = query.executeUpdate();
			System.out.println("rows updated" + rows);
			tx1.commit();

			Transaction tx = session.beginTransaction();
			session.save(poolRequest);
			tx.commit();
		
			User userPool = this.getUserDetails(pool.getId());
			
			if(status==4)
			{
				String message = "Hi "+user.getName()+"\n "+userPool.getName()+
						 " has sent you a 'Join My Pool' request. Please open our app and "
						 + " go to requests page respond to view details about "+userPool.getName()+"'. You can also"
						 		+ " chat with the user.\n Thanks, \n Team RidEasy\n Keep Riding, Keep Sharing !";
				String subject = userPool.getName()+" has sent you a request a Join My Pool request !";
				String[] to = { user.getEmail() };
				
						SendMail.sendEmail(GlobalConstants.FROM_EMAIL,
								GlobalConstants.PASSWORD_EMAIL, subject, message,
								to);
						logger.info("Join My Pool Request sent  - user notified ! email - "+user.getEmail());	
			}
			else{
			String message = "Hi "+userPool.getName()+"\n "+user.getName()+
					 " has requested to join your car pool. Please open our app and "
					 + " go to requests page respond to "+user.getName()+"/'s request. You can also"
					 		+ " chat with the user.\n Thanks, \n Team RidEasy \n Keep Riding, Keep Sharing !";
			String subject = user.getName()+" wants to join your car pool !";
			String[] to = { userPool.getEmail() };
					SendMail.sendEmail(GlobalConstants.FROM_EMAIL,
							GlobalConstants.PASSWORD_EMAIL, subject, message,
							to);
					logger.info("Request to join Pool request - user notified ! email - "+userPool.getEmail());	
			}
			session.close();
		
		return true;
	}
	@Override
	public boolean updatePoolRequest(String requestId, int response) {
		try{
			logger.info("Update Pool Request call Request id-   - "+requestId+"Status -   "+response);	

		boolean result = false;
		Session session = sessionFactory.openSession();
		session.clear();
		String hql = "from PoolRequest where id=?";
		Query qry = session.createQuery(hql);
		qry.setString(0, requestId);
		
		PoolRequest poolRequest = null;
		if(qry.list() != null && qry.list().size() > 0){
		poolRequest = (PoolRequest) qry.list().get(0);
		String poolId=poolRequest.getPool().getId();
		String userId=poolRequest.getUser().getId();
		String userOldPoolId=poolRequest.getUser().getPool().getId();
		
		if (response == GlobalConstants.REQUEST_ACCEPTED) {
				if(userId!=userOldPoolId)
					{
					this.leavePool(userId,userOldPoolId);
					}
	//			User userToBeAdded=
				session.clear();
			result = addToPool(userId, poolId);
	/*	if(result)
			{
				Session session1= RideSharingUtil.getSessionFactoryInstance().openSession();
				Transaction tx3 = session1.beginTransaction();
				try{
				String hql2 = "delete from PoolRequest where user.id='"+userId+
						"' AND pool.id<>'"+poolId+"' and status="+GlobalConstants.REQUEST_ACCEPTED;
				Query qry1 = session1.createQuery(hql2);
				qry1.executeUpdate();
				tx3.commit();
				}catch(Exception e){
					tx3.rollback();
					e.printStackTrace();
				}
				finally{
					session1.close();
				}
			}*/
			if (!result)
			{
				
				return result;
			}
			
		}
		Transaction tx = session.beginTransaction();
		poolRequest.setStatus(response);

		try {

			session.update(poolRequest);
			result = true;
			tx.commit();
			session.clear();
			if(response == GlobalConstants.REQUEST_ACCEPTED)//this part needs to be revisted once basic functionality is done. rollback statements are required.
			{

				WalletTransactions walletTransaction =new WalletTransactions();
				User poolOwner=this.getUserDetails(poolId); 
		//		walletTransaction.setPoolOwner(poolOwner);
		//		walletTransaction.setPoolParticipant(this.getUserDetails(userId));
				int days=this.dayDate();
				System.out.println(days+" :days");
				walletTransaction.setAmount((int)(poolOwner.getPoolCost()/5.00)*days);//need to set
				session.clear();
				walletTransaction.setType(TransactionType.DEBIT_TO_WALLET);
				WalletUtil.poolRequestAccepted(walletTransaction,poolId,userId);
				
				    Transaction tx1 = session.beginTransaction();	
				    String hql1="Update PoolRequest set status="+GlobalConstants.REQUEST_CANCEL+" where status="+GlobalConstants.REQUEST_PENDING+" and user.id='"+userId+"'";
				    Query query=session.createQuery(hql1);
				    int rows = query.executeUpdate();
				    System.out.println("rows updated"+rows);
				    tx1.commit();
				    
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		
			session.close();
		}
		}
		return result;
	}catch(Exception e){
		e.printStackTrace();
	}
		return false;
	}

	private boolean addToPool(String userId, String poolId) { // add
		Session session = sessionFactory.openSession();																// user
					Pool	pool=this.getPoolDetails(poolId);												// to
					User user=this.getUserDetails(userId);	// pool
		logger.info("Add to Pool call on User  - "+user.getEmail()+"for pool - "+poolId);	

					Pool existingPool = user.getPool();
		if (!pool.getIsAvailable())
			return false;

		Long currentTime=getSystemTimeMilisGMT();
		// Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		// pool.getParticipants().add(user);
		pool.setIs_active(true);
		user.setPool(pool);
		int noOfMembers = pool.getNumberOfMembers();
		pool.setNumberOfMembers(noOfMembers + 1);
		if(pool.getMax_members()!=null){
		if (pool.getMax_members() == noOfMembers + 1)
			pool.setIsAvailable(false);
		}
	
		existingPool.setIsAvailable(false);
		session.update(existingPool);
	
		session.update(pool);
		session.update(user);
		System.out.println("user pool changed successfully !!!");
		// System.out.println("pool saved oyeah");
		String hql = "from Transactions where user.id='" + user.getId()
				+ "' and is_valid=true";
		Query qry = session.createQuery(hql);
		Transactions oldTransaction = new Transactions();
		oldTransaction = (Transactions) qry.uniqueResult();
		oldTransaction.setIs_valid(false);
			oldTransaction.setValid_to(currentTime);
		session.update(oldTransaction);
		// System.out.println("old");

		Transactions newTransaction = new Transactions();
		newTransaction.setIs_valid(true);
		newTransaction.setPool(pool);
		newTransaction.setUser(user);
			newTransaction.setValid_from(currentTime);
		newTransaction.setValid_to(Long.MAX_VALUE);
		session.save(newTransaction);
		tx.commit();
		session.close();
		return true;
	}

	
	private int  dayDate()
	{
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		if(Calendar.DAY_OF_WEEK==Calendar.SATURDAY || Calendar.DAY_OF_WEEK==Calendar.SUNDAY )
		return 5;
		else 
			return 7-Calendar.DAY_OF_WEEK;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean leavePool(String userId, String poolId) {
		
		boolean result = false;
		User user = this.getUserDetails(userId);
		Pool pool = this.getPoolDetails(poolId);
		logger.info("LeavePool call on User  - "+user.getEmail()+"for pool - "+poolId);	
		Session session = sessionFactory.openSession();
		Transaction tx = null;
	
		if(pool.getId().equals(user.getId()) && pool.getNumberOfMembers()==1 )
		{
			
//			tx.commit();
			session.close();

			System.out.println("take no action");
			return false;
		}else
		if (!pool.getId().equals(user.getId())) {

			// Iterator<User> participants = pool.getParticipants().iterator();
			// // participants.remove(user);
			// while (participants.hasNext()) {
			// User participant = participants.next();
			// if (participant.getId().equals(user.getId())) {
			// participants.remove();
			// }

			// }

					
					Transaction tx2 = session.beginTransaction();
					Pool userOriginalPool = this.getPoolDetails(userId);
					userOriginalPool.setIsAvailable(true);
					user.setPool(userOriginalPool);
					pool = this.getPoolDetails(poolId);
					int noOfMembers = pool.getNumberOfMembers();
					pool.setNumberOfMembers((noOfMembers - 1));
					pool.setIsAvailable(true);
					session.update(user);
					session.update(pool);
			
			String hql = "from Transactions where (user.id='" + user.getId()
					+ "' and is_valid=true)";
			Long currentTime=getSystemTimeMilisGMT();
			Query qry = session.createQuery(hql);
			System.out.println(qry.list().size());
			List<Transactions> allTransactions = (List<Transactions>) qry
					.list();
			Transactions oldTransaction = allTransactions.get(0);
			oldTransaction.setIs_valid(false);
			oldTransaction.setValid_to(currentTime);
			session.update(oldTransaction);

			Transactions newTransaction = new Transactions();// primary key??
			newTransaction.setIs_valid(true);
			newTransaction.setPool(userOriginalPool);
			newTransaction.setUser(user);
			newTransaction.setValid_from(currentTime);
			newTransaction.setValid_to(Long.MAX_VALUE);
			session.save(newTransaction);
		//	session.update(user);
			tx2.commit();


		//	User walletDebitUser= this.getUserDetails(userId);
		//	User walletCreditUser=this.getUserDetails(poolId);
			
			Transaction tx3 = session.beginTransaction();
			try{
			String hql2 = "delete from PoolRequest where user.id='"+userId+
					"' AND pool.id='"+poolId+"' and status="+GlobalConstants.REQUEST_ACCEPTED;
			Query qry1 = session.createQuery(hql2);
			qry1.executeUpdate();
			tx3.commit();
			}catch(Exception e){
				tx3.rollback();
				e.printStackTrace();
				return false;
			}
			
			WalletTransactions walletTransaction =new WalletTransactions();
			//		walletTransaction.setId(id); generate this..
					//User poolOwner=this.getUserDetails(pool.getId());
			//		walletTransaction.setPoolOwner(walletCreditUser);
				//	walletTransaction.setPoolParticipant(walletDebitUser);
					WalletUtil.poolLeftByUser(walletTransaction,poolId,userId);
			//		tx = session.beginTransaction();
			//	session.update(walletCreditUser);
			//	session.update(walletDebitUser);
				
				
					
					
			result = true;

		} else// if hostuser is leaving the pool
			//if (pool.getId().equals(user.getId())) 
				{
					// Session session = sessionFactory.openSession();
					// pool.getParticipants().removeAll(c);
					// int noOfMembers = pool.getNumberOfMembers();
					pool.setIs_active(true);
					pool.setIsAvailable(true);
					pool.setNumberOfMembers(1);
					pool.setIsAvailable(true);
					tx= session.beginTransaction();
					session.update(pool);
					String hostUserId = "";
					// Collection<User> participants = pool.getParticipants();
					// participants.remove(user);
					// pool.getParticipants().removeAll(participants);
					List<User> participants = new ArrayList<User>();
					participants = this.getParticipantsExceptHost(poolId, session);
					float dis = 0;
					for (User participant : participants) {
				
						WalletTransactions walletTransaction =new WalletTransactions();
						//		walletTransaction.setId(id); generate this..
								User poolOwner=this.getUserDetails(pool.getId());
								walletTransaction.setPoolOwner(poolOwner);
								walletTransaction.setPoolParticipant(participant);
								WalletUtil.poolLeftByUser(walletTransaction,poolOwner.getId(),participant.getId()); //settling account for every participant in case poolowner leaves
								session.update(walletTransaction.getPoolOwner());
								session.update(walletTransaction.getPoolParticipant());
						
						if(participant.getDistance() > dis) {
							dis = participant.getDistance(); // new host user
							hostUserId = participant.getId();
						}
					}
					
	
					String hql = "from Transactions where user.id!='"
							+ user.getId() + "' and is_valid=true and pool.id='"
							+ user.getId() + "'";
					Query qry = session.createQuery(hql);
					List<Transactions> oldTransactions = (List<Transactions>) qry
							.list();
					Long currentTime=getSystemTimeMilisGMT();
					for (Transactions oldTransaction : oldTransactions) {
						oldTransaction.setIs_valid(false);
						oldTransaction.setValid_to(currentTime);
						session.update(oldTransaction);
					}
	//				session.saveOrUpdate(oldTransactions);
				//	String hql1 = "from Pool where id='" + hostUserId + "'";
				//	Query qry1 = session.createQuery(hql1);
					Pool hostUserPool = this.getPoolDetails(hostUserId);
					// hostUserPool.setParticipants((List<User>) participants);
					hostUserPool.setNumberOfMembers(participants.size());
					session.update(hostUserPool);
	
					for (User participant : participants) {
						participant.setPool(hostUserPool);
						session.update(participant);
	
					}
				//	session.save(participants);
					List<Transactions> newTransactions = new ArrayList<Transactions>();
					{
						for (User participant : participants) {
							Transactions newTransaction = new Transactions();
							newTransaction.setIs_valid(true);
							newTransaction.setUser(participant);
							newTransaction.setValid_from(currentTime);
							newTransaction.setValid_to(Long.MAX_VALUE);
							newTransaction.setPool(hostUserPool);
							session.save(newTransaction);
							newTransactions.add(newTransaction);
						}
	
					}
	
					
	
			result=true;	
			}
		if(tx!=null){
		tx.commit();
		}
		session.close();

		return result;

	}

	private List<User> getParticipants(String poolId, Session session) {
		String hql = "from User where pool.id<>'" + poolId + "'";
		Query qry = session.createQuery(hql);
		List<User> participants = qry.list();
		// TODO Auto-generated method stub
		return participants;
	}
	
	private List<User> getParticipantsExceptHost(String poolId, Session session) {
		String hql = "from User user where pool.id='" + poolId + "' and user.id!='"+poolId+"'";
		Query qry = session.createQuery(hql);
		List<User> participants = qry.list();
		// TODO Auto-generated method stub
		return participants;
	}


	@Override
	public boolean insertUser(User user) {
		try{
		if (user != null) {
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			try {
				user.setIsActive(true);
				if (!user.getLeaveDestinationTimeInMilliseconds().isEmpty()) {
					logger.info("Leave Destination Time : "
							+ user.getLeaveDestinationTimeInMilliseconds());
					user.setLeaveDestinationTime(new Date(Long.parseLong(user
							.getLeaveDestinationTimeInMilliseconds())));
				}
				if (!user.getReachDestinationTimeInMilliseconds().isEmpty()) {
					logger.info("Reach Destination Time : "
							+ user.getReachDestinationTimeInMilliseconds());
					user.setReachDestinationTime(new Date(Long.parseLong(user
							.getReachDestinationTimeInMilliseconds())));
				}
				user.setRegisterDate(this.getCurrentTime());
				// insert user
				session.save(user);
				// persist matched users
				tx.commit();

			} catch (Exception e) {
				tx.rollback();
				return false;
			} finally {
				session.close();
			}
			System.out.println("inserted success1");
			List<UserMapping> userMatch = findMatchedUser(user);
			if (null != userMatch && userMatch.size() > 0) {
				notifySingleUser(userMatch);
				persistUserMatch(userMatch);
			}
			System.out.println("inserted success2");
			List<UserMapping> matchForOneUser = matchForOneUser(user.getId());

			if (null != matchForOneUser && matchForOneUser.size() > 0) {
				notifyUsers(matchForOneUser);
				persistUserMatch(matchForOneUser);
			}
			System.out.println("inserted success2");
			try {
				session = sessionFactory.openSession();
				tx = session.beginTransaction();
				Pool pool = createPool(user);
				user.setPool(pool);
				session.update(user.getId(), user);
				session.save(pool);
				// insert transaction
				insertTransaction(user, session);
				tx.commit();
			} catch (Exception e) {
				tx.rollback();
			this.deleteMatchedUsers(user.getId());
			this.deleteUserOnly(user.getId());
			} finally {
				session.close();
			}
			return true;
		}		
		return false;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	private void notifySingleUser(List<UserMapping> userMatch) {
		// TODO Auto-generated method stub
		User newUser= null;
		User matchingUser = null;
		String to,from,subject,body;
		if(userMatch.size() > 0){
			newUser = userMatch.get(0).getUserA();
			for (UserMapping matchedUser : userMatch){
				matchingUser = matchedUser.getUserA();
				subject = "Congratulations! We already have  recommendations for you !";
				body = "Hi "+newUser.getName()+",\n"+ " Thanks for signing up at RidEasy App. We have good news for you ! \n Please open our  "
						+ "app again and go to recommendations page to view your car pools that match your daily trip. You can see details of each user and even chat with them in app. Don't "
						+ "forget to add money to your RidEasy wallet before sending requests to join pool :) ."
						+ "\n Thanks,\n"
						+ " Team RidEasy, \n"
						+ " Keep Riding, Keep Sharing!";
				new MailNotifierThread(body, matchingUser.getEmail(), subject).start();
				logger.info(" New user notified for existing recommendations   "+newUser.getEmail());	
			break;
			}
		}
	}

	private void insertTransaction(User user, Session session) {
		Transactions transaction = new Transactions();
		transaction.setIs_valid(true);
		transaction.setPool(user.getPool());
		transaction.setUser(user);
		transaction.setValid_from(getSystemTimeMilisGMT());
		transaction.setValid_to(Long.MAX_VALUE);
		session.save(transaction);
	}

	@Override
	public User updateUser(User user, boolean changeAddress) {
		Session session = sessionFactory.openSession();
		Pool curPool=this.getUserDetails(user.getId()).getPool();
		Pool pool=this.getPoolDetails(user.getId());
		/* int flag = 0; */
		// if change in address
			if (changeAddress) {
				System.out.println("in change address"); //remove - only for testing
				deleteMatchedUsers(user.getId());
			   //	List<UserMapping> userMatch = findMatchedUser(user.getId()); //no sense here
			   //	persistUserMatch(userMatch);
					Transaction tx = session.beginTransaction();
					try {
					leavePool(user.getId(),curPool.getId()); // leave his existing pool
					
					pool.setSourceAddress(user.getHomeAddress()); // pool need to be save before matching an
					pool.setDestinationAddress(
								user.getOfficeAddress());
				    user.setPool(pool);
					session.update(pool);
					session.update(user);
					tx.commit();
					List<UserMapping> userMatch = findMatchedUser(user);
					notifySingleUser(userMatch);
					persistUserMatch(userMatch);
					List<UserMapping> matchForOneUser = matchForOneUser(user.getId());

					if (null != matchForOneUser && matchForOneUser.size() > 0) {
						notifyUsers(matchForOneUser);
						persistUserMatch(matchForOneUser);
					}
						}catch (Exception e) {
							logger.info(e.getMessage());
							e.printStackTrace();
							tx.rollback();

							return null;
					}
			} 
			
			// update user
		/*	if (!user.getLeaveDestinationTimeInMilliseconds().isEmpty()) {
				logger.info("Leave Destination Time : "
						+ user.getLeaveDestinationTimeInMilliseconds());
				if (tmpPool != null) {
					tmpPool.setLeaveDestinationTime(new Date(Long
							.parseLong(user
									.getLeaveDestinationTimeInMilliseconds())));
					tmpPool.setLeaveDestinationTimeInMilliseconds(user
							.getLeaveDestinationTimeInMilliseconds());
				}
				user.setLeaveDestinationTime(new Date(Long.parseLong(user
						.getLeaveDestinationTimeInMilliseconds())));
			}
			if (!user.getReachDestinationTimeInMilliseconds().isEmpty()) {
				logger.info("Reach Destination Time : "
						+ user.getReachDestinationTimeInMilliseconds());
				if (tmpPool != null) {
					tmpPool.setReachDestinationTime(new Date(Long
							.parseLong(user
									.getReachDestinationTimeInMilliseconds())));
					tmpPool.setReachDestinationTimeInMilliseconds(user
							.getReachDestinationTimeInMilliseconds());
				}
				user.setReachDestinationTime(new Date(Long.parseLong(user
						.getReachDestinationTimeInMilliseconds())));
			}
			if (tmpPool != null) {
				user.setPool(tmpPool);
			}*/
			Transaction tx = session.beginTransaction();
			try{
		/*	Pool pool=this.getPoolDetails(user.getId());
			System.out.println(pool.getId());
			pool.setReachDestinationTimeInMilliseconds(user.getReachDestinationTimeInMilliseconds());
			System.out.println("here");
			pool.setLeaveDestinationTimeInMilliseconds(user.getLeaveDestinationTimeInMilliseconds());
			System.out.println("here2");
			session.update(pool);
			tx.commit();
			Transaction tx1 = session.beginTransaction();
			*/session.update(user);
			tx.commit();
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
			tx.rollback();

			return null;
		} finally {
			session.close();
		}
		return this.getUserDetails(user.getId());
	}

	private void notifyUsers(List<UserMapping> userMatch) {
		// TODO Auto-generated method stub
		User newUser= null;
		User matchingUser = null;
		String to,from,subject,body;
		if(userMatch.size() > 0){
			newUser = userMatch.get(0).getUserB();
			for (UserMapping matchedUser : userMatch){
				matchingUser = matchedUser.getUserA();
				subject = "Congratulations! you have a new recommendation for your car pool !";
				body = "Hi "+matchingUser.getName()+",\n"+newUser.getName()+ " matches with your"
						+ " current pool details. Please open our app and check the recommendations"
						+ " page to know more. Also, you can chat with the user before you accept the request.\n Thanks,\n Team RidEasy, \n Keep Riding, Keep Sharing!";
				new MailNotifierThread(body, matchingUser.getEmail(), subject).start();
				logger.info(" New Match between user  "+newUser.getName()+"and existing user  - "+matchingUser.getEmail());	
			}
		}
		
	}

	private String getPoolForUser(String userId, Session session) {
		String hql = "select user.pool.id from User user where user.id='"
				+ userId + "'";
		Query qry = session.createQuery(hql);
		String poolId = (String) qry.uniqueResult();
		return poolId;
	}

	private List<Pool> recommendedPools(String userId) {
		Session session = sessionFactory.openSession();
		// User currentUser = this.getUserDetails(userId);
		String hql = "select t.pool from Transactions t,UserMapping um where um.userA.id='"
				+ userId
				+ "' and t.is_valid="
				+ true
				+ " and t.pool.isAvailable="
				+ true
				+ " and um.userB.id=t.user.id";

		// String hql
		// ="select u.pool from User u,UserMapping um where um.userA.id='"
		// + userId
		// + " and u.pool.isAvailable="
		// + true
		// + " and um.userB.id=u.id";
		//
		Query qry = session.createQuery(hql);
		List<Pool> lstPool = qry.list();
		return lstPool;
	}

	@Override
	// dont use
	public void deleteUser(String userId) {// not tested
		Session session = sessionFactory.openSession();
		// Transaction tx = session.beginTransaction();

		// Query q = session.createQuery("delete from User where id = uid");
		// q.setParameter("uid", userId);
		// User user = (User) q.list().get(0);
		// session.delete(user);
		Transaction tx = session.beginTransaction();
		String hql = "delete from User where user.id=?";
		Query qry = session.createQuery(hql);
		qry.setString(0, userId);
		qry.executeUpdate();
		tx.commit();

		session.close();

	}

	public void deleteAddress()// not tested
	{
		Session session = sessionFactory.openSession();
		// Transaction tx = session.beginTransaction();

		// Query q = session.createQuery("delete from User where id = uid");
		// q.setParameter("uid", userId);
		// User user = (User) q.list().get(0);
		// session.delete(user);
		Transaction tx = session.beginTransaction();
		String hql = "from Address address";
		Query qry = session.createQuery(hql);
		// qry.setString(0, "office_add");
		// List<Address> lstPool = qry.list();

		// System.out.println(lstPool.size());
		tx.commit();

		session.close();

		System.out.println("end");
	}
/*
	// made by vidur- helped to put data
	public boolean matchTest(String userId) {
		// session.save(user);
		// Pool pool = createPool(user);
		// user.setPool(pool);
		// insert user
		// session.save(user);
		// insert transaction
		// insertTransaction(user, session);
		// persist matched users
		List<UserMapping> userMatch = findMatchedUser(userId);
		System.out.println(userMatch.size());
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		if (null != userMatch && userMatch.size() > 0) {
			persistUserMatch(userMatch);
		}
		tx.commit();
		session.close();
		return true;
	}*/

	@Override
	public List<User> fetchPoolParticipants(String poolId) {
		Session session = sessionFactory.openSession();
		Criteria cr = session.createCriteria(User.class);
		Pool pool = new Pool();
		pool.setId(poolId);
		cr.add(Restrictions.eq("pool", pool));
		List<User> participants = new ArrayList<User>();
		if (cr.list() != null && cr.list().size() > 0) {
			participants = cr.list();
		}
		/*
		 * for(User user:poolParticipants){ participants.add(user); }
		 */
		session.close();
		return participants;
	}

	private List<UserMapping> matchForOneUser(String userId) {
		Session session = sessionFactory.openSession();
		// Criteria cr = session.createCriteria(User.class);
		// cr.add(Restrictions.neOrIsNotNull("id", userId));
		// List<User> users=cr.list();
		// System.out.println(users.size());
		// session.close();
		User currentUser = this.getUserDetails(userId);
		if (null != currentUser && null != currentUser.getHomeAddress()
				&& null != currentUser.getOfficeAddress()
				&& currentUser.getHomeAddress().getLattitude() != 0.0
				&& currentUser.getHomeAddress().getLongitude() != 0.0
				&& currentUser.getOfficeAddress().getLattitude() != 0.0
				&& currentUser.getOfficeAddress().getLongitude() != 0.0) {
			String hql = "from User user where user.id<>'" + userId
					+ "' and (user.homeAddress.lattitude between "
					+ (currentUser.getHomeAddress().getLattitude() - 1)
					+ " and "
					+ (currentUser.getHomeAddress().getLattitude() + 1)
					+ ") and (user.homeAddress.longitude between "
					+ (currentUser.getHomeAddress().getLongitude() - 1)
					+ " and "
					+ (currentUser.getHomeAddress().getLongitude() + 1)
					+ ") and (user.officeAddress.longitude between "
					+ (currentUser.getOfficeAddress().getLongitude() - 1)
					+ " and "
					+ (currentUser.getOfficeAddress().getLongitude() + 1)
					+ ") and (user.officeAddress.lattitude between "
					+ (currentUser.getOfficeAddress().getLattitude() - 1)
					+ " and "
					+ (currentUser.getOfficeAddress().getLattitude() + 1) + ")";

			Query qry = session.createQuery(hql);
			List<User> userList = new ArrayList<User>();
			userList = qry.list();
			session.close();
			System.out.println(userList.size());
			UserMatching userMatch = new UserMatching();
			try {
				return userMatch
						.checkMatchedUsersForUser(userList, currentUser);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (ProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;

	}

	@Override
	public PoolRequest getPoolRequestVO(UserIdPoolIdVO userIdPoolIdVO) {
		Session session = sessionFactory.openSession();
		Criteria cr = session.createCriteria(PoolRequest.class);
		Pool pool = new Pool();
		pool.setId(userIdPoolIdVO.getPoolId());
		cr.add(Restrictions.eq("pool", pool));
		User user = new User();
		user.setId(userIdPoolIdVO.getUserId());
		cr.add(Restrictions.eq("user", user));

		PoolRequest poolRequest = new PoolRequest();
		try {
			poolRequest = (PoolRequest) cr.uniqueResult();
		} catch (Exception e) {
			poolRequest = null;
		} finally {
			session.close();
		}
		return poolRequest;
	}

	@Override
	public ListWalletTransactions getWalletTransactionHistory(String userId) {
		ListWalletTransactions listWalletTransactions = new ListWalletTransactions();
		Session session = sessionFactory.openSession();
		Criteria cr = session.createCriteria(WalletTransactions.class);
		User user = new User();
		user.setId(userId);
		cr.add(Restrictions.eq("poolParticipant", user));
		cr.addOrder(Order.desc("transaction_timemillis"));
		List<WalletTransactions> walletTransactions = cr.list();
		listWalletTransactions.setWalletTransactions(walletTransactions);
		session.close();
		return listWalletTransactions;
	}
	public static Long getSystemTimeMilisGMT(){
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		long time = cal.getTimeInMillis();
		return time+TimeZone.getDefault().getOffset(time);
	}
}