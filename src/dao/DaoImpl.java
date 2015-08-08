package dao;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import pojos.Address;
import pojos.OTP;
import pojos.Pool;
import pojos.PoolRequest;
import pojos.Transactions;
import pojos.User;
import pojos.UserMapping;
import utility.RideSharingUtil;
import utility.UserMatching;
import utility.GlobalConstants;

public class DaoImpl implements DaoI {
	SessionFactory sessionFactory = RideSharingUtil.getSessionFactoryInstance();
	Logger logger = Logger.getLogger("debug");

	public boolean insertOTPEmail(String userEmail, int otp) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		OTP otpObject = new OTP();
		otpObject.setEmail(userEmail);
		logger.info("Email : " + userEmail);
		otpObject.setPasscode(otp + "");
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
		if (lst.size() > 0) {
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
		otpObjectByEmail.setPasscode(otp + "");
		logger.info("OTP : " + otp);
		try {
			otpObjectByEmail.setCreate_time(this.getCurrentTime());
			session.update(otpObjectByEmail);
			logger.info("Update Successful");
			tx.commit();
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
		OTP otpObjectByEmail = null;
		if (cr.list().size() == 1) {
			otpObjectByEmail = (OTP) cr.list().get(0);
		}
		session.close();
		return otpObjectByEmail;
	}

	public boolean deleteMatchedUsers(String userId) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String hql = "delete from UserMapping where userA.id=?";
		Query qry = session.createQuery(hql);
		qry.setString(0, userId);
		qry.executeUpdate();
		tx.commit();
		session.close();
		return true;
	}

	private Pool createPool(User user) {
		Pool pool = new Pool();
		pool.setId(user.getId());
		pool.setHostUserId(user.getId());
		pool.setIs_active(true);
		pool.setIsAvailable(true);
		pool.setSourceAddress(user.getHomeAddress());
		pool.setDestinationAddress(user.getOfficeAddress());
		return pool;
	}

	@Override
	public User getUserDetails(String userId) {
		Session session = sessionFactory.openSession();
		Criteria cr = session.createCriteria(User.class);
		cr.add(Restrictions.eq("id", userId));
		User userVO = null;
		if (cr.list() != null && cr.list().size() > 0) {
			userVO = (User) cr.list().get(0);
		}
		session.close();
		return userVO;
	}

	@Override
	public Pool getPoolDetails(String poolId) {
		Session session = sessionFactory.openSession();
		Criteria cr = session.createCriteria(Pool.class);
		cr.add(Restrictions.eq("id", poolId));
		Pool pool = (Pool) cr.list().get(0);
		session.close();
		return pool;
	}

	@Override
	public List<Pool> matchedPool(String userId) {

		// List of pools returned for particular user.
		Session session = sessionFactory.openSession();
		// User currentUser = this.getUserDetails(userId);
		String hql = "select t.pool,min(um.distance) from Transactions t,UserMapping um where um.userA.id='"
				+ userId
				+ "' and t.is_valid="
				+ true
				+ " and t.pool.isAvailable="
				+ true
				+ " and um.userB.id=t.user.id  group by  t.pool "
				+ "order by min(um.distance)";
		Query qry = session.createQuery(hql);
		// @SuppressWarnings("unchecked")
		// List<Pool> userPools = qry.list();
		List<Object[]> result = (List<Object[]>) qry.list();

		for (Object[] results : result) {
			Pool pool = (Pool) results[0];
			Float distance = (Float) results[1];
			System.out.println(pool.getId() + "  " + distance);// able to get
																// pool and
																// distance.put
																// it in java
																// element
		}

		// for(Pool pool:userPools)
		// System.out.println(pool.getId());
		session.close();
		return null;

	}

	private List<UserMapping> findMatchedUser(String userId) {
		Session session = sessionFactory.openSession();
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

			List<User> userList = qry.list();
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

	private void persistUserMatch(List<UserMapping> userMapping, Session session) {
		if (userMapping != null && userMapping.size() > 0) {
			for (UserMapping userMatch : userMapping) {
				session.save(userMatch);
			}
		}
	}

	public List<Transactions> getUserPoolRecord(String userId) { // pool
																	// transaction
																	// history
																	// of user - checked

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
		}
		session.close();
		return userVO;
	}

	@Override
	public List<PoolRequest> getIncomingPoolRequests(String userId) { //CHECKED
		Session session = sessionFactory.openSession();
		String hql = "from PoolRequest where user.id=?";
		Query qry = session.createQuery(hql);
		qry.setString(0, userId);
		List<PoolRequest> userPoolRequest = qry.list();
//		for(PoolRequest individual : userPoolRequest)
//		{
//			System.out.println(individual.getId());
//			System.out.println(individual.getStatus());
//			System.out.println(individual.getPool().getId());
//			System.out.println(individual.getUser().getId());
//			System.out.println(individual.getDescription());
//			
//		}
		
		session.close();
		return userPoolRequest;
	}

	@Override
	public List<PoolRequest> getOutcomingPoolRequests(String userId)
	{
		Session session = sessionFactory.openSession();
		String hql = "from PoolRequest where pool.hostUserId=? and status ="+GlobalConstants.REQUEST_PENDING;
		Query qry = session.createQuery(hql);
		qry.setString(0, userId);
		List<PoolRequest> userPoolRequest = qry.list();
		session.close();
		return userPoolRequest;	
	}
	
	@Override
	public boolean updatePoolRequest(String requestId, int response) {
		boolean result = false;
		Session session = sessionFactory.openSession();
		
		String hql = "from PoolRequest where id=?";
		Query qry = session.createQuery(hql);
		qry.setString(0, requestId);
		PoolRequest poolRequest = (PoolRequest) qry.list().get(0);
		if (response == GlobalConstants.REQUEST_ACCEPTED)
		{	result=addToPool(poolRequest.getUser(), poolRequest.getPool(),session);
		if(!result)
			return result;
		}
		Transaction tx = session.beginTransaction();
		poolRequest.setStatus(response);

		try {
			
			session.update(poolRequest);
			result = true;
			tx.commit();
		} catch (Exception e) {
		} finally {
			session.close();
		}
		return result;
	}

	private boolean addToPool(User user, Pool pool,Session session) {  //add user to pool
		if (!pool.getIsAvailable())
			return false;

	//	Session session = sessionFactory.openSession();
	    Transaction tx = session.beginTransaction();
		pool.getParticipants().add(user);
		int noOfMembers = pool.getNumberOfMembers();
		pool.setNumberOfMembers(noOfMembers + 1);
		if (pool.getMax_members() == noOfMembers + 1)
			pool.setIsAvailable(false);

		session.update(pool);
		
//System.out.println("pool saved oyeah");
		String hql = "from Transactions where user.id='" + user.getId()
				+ "' and is_valid=true";
		Query qry = session.createQuery(hql);
		Transactions oldTransaction = (Transactions) qry.uniqueResult();
		oldTransaction.setIs_valid(false);
		Date currentDateTime = new Date();
		oldTransaction.setValid_to(currentDateTime);
		session.update(oldTransaction);
//System.out.println("old");
	
Transactions newTransaction = new Transactions();
		newTransaction.setIs_valid(true);
		newTransaction.setPool(pool);
		newTransaction.setUser(user);
		newTransaction.setValid_from(currentDateTime);
		newTransaction.setValid_to(new Date(8000, 12, 31, 00, 00, 00));
		session.save(newTransaction);
		tx.commit();
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean leavePool(String userId, String poolId) {
		Session session = sessionFactory.openSession();
		boolean result=false;
		User user=this.getUserDetails(userId);
		Pool pool=this.getPoolDetails(poolId);
		Transaction tx = session.beginTransaction();
		if (!pool.getHostUserId().equals(user.getId())) {
			
			Iterator<User> participants=pool.getParticipants().iterator();
		//participants.remove(user);
			while (participants.hasNext()) {
				User participant=participants.next();
			  if(participant.getId().equals(user.getId()))
		{
		  participants.remove();
	}	
			}	
			int noOfMembers = pool.getNumberOfMembers();
			pool.setNumberOfMembers(noOfMembers - 1);
			pool.setIsAvailable(true);
		//	pool.setParticipants( (List<User>) participants);
			session.saveOrUpdate(pool);

			String hql = "from Transactions where (user.id='" + user.getId()
					+ "' and is_valid=true) or (pool.id='" + user.getId()
					+ "') order by valid_from desc";
			
			Query qry = session.createQuery(hql);
			System.out.println(qry.list().size());
			List<Transactions> allTransactions=(List<Transactions>) qry.list();
			Transactions oldTransaction = allTransactions.get(0);
			oldTransaction.setIs_valid(false);
			Date currentDateTime = new Date();
			oldTransaction.setValid_to(currentDateTime);
			session.update(oldTransaction);

			Transactions homeTransaction = allTransactions.get(1);
			Transactions newTransaction = new Transactions();// primary key??
			newTransaction.setIs_valid(true);
			newTransaction.setPool(homeTransaction.getPool());
			newTransaction.setUser(homeTransaction.getUser());
			newTransaction.setValid_from(currentDateTime);
			newTransaction.setValid_to(new Date(8000, 12, 31, 00, 00, 00));
			session.save(newTransaction);

			result= true;

		} else// if hostuser is leaving the pool
		{
			if(pool.getId().equals(user.getId())) 
			{

			//	Session session = sessionFactory.openSession();
			//	pool.getParticipants().removeAll(c);
			//	int noOfMembers = pool.getNumberOfMembers();
				pool.setNumberOfMembers(1);
				String hostUserId="";
				Collection<User> participants=pool.getParticipants();
				participants.remove(user);
				pool.getParticipants().removeAll(participants);
				for(User participant:participants)
				{
					float dis=0;
					if(participant.getDistance()>dis)
					{	
						dis=participant.getDistance(); 			//new host user
						hostUserId=participant.getId();
					}
				}
				pool.setIsAvailable(true);
				session.saveOrUpdate(pool);

			
	
				String hql = "from Transactions where user.id<>'" + user.getId()
						+ "' and is_valid=true and pool.id='" + user.getId()
						+ "'";
				Query qry = session.createQuery(hql);
				List<Transactions> oldTransactions = (List<Transactions>) qry.list();
				Date currentDateTime = new Date();
				for(Transactions oldTransaction:oldTransactions)
				{
				oldTransaction.setIs_valid(false);
				oldTransaction.setValid_to(currentDateTime);
				}
				session.saveOrUpdate(oldTransactions);
				
				String hql1 = "from Pool where id='" + hostUserId+ "'";
				Query qry1 = session.createQuery(hql1);
				Pool hostUserPool = (Pool) qry.uniqueResult();
				hostUserPool.setParticipants((List<User>) participants);
				session.saveOrUpdate(hostUserPool);
				
				List<Transactions> newTransactions = new ArrayList<Transactions>();
				{
					for(User participant:participants)
					{
						Transactions newTransaction = new Transactions();
						newTransaction.setIs_valid(true);
						newTransaction.setUser(participant);
						newTransaction.setValid_from(currentDateTime);
						newTransaction.setValid_to(new Date(8000, 12, 31, 00, 00, 00));
						newTransaction.setPool(hostUserPool);
						newTransactions.add(newTransaction);
					}
					
				}

				session.save(newTransactions);
							
			}
		}
		tx.commit();
		session.close();
		
		return result;

	}


	@Override
	public boolean insertUser(User user) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.save(user);
		Pool pool = createPool(user);
		user.setPool(pool);
		// insert user
		session.save(user);
		// insert transaction
		insertTransaction(user, session);
		// persist matched users
		List<UserMapping> userMatch = findMatchedUser(user.getId());
		if (null != userMatch && userMatch.size() > 0) {
			persistUserMatch(userMatch, session);
		}
		tx.commit();
		session.close();
		return true;
	}

	private void insertTransaction(User user, Session session) {
		Transactions transaction = new Transactions();
		transaction.setIs_valid(true);
		transaction.setPool(user.getPool());
		transaction.setUser(user);
		Date date = new Date();
		transaction.setValid_from(date);
		session.save(transaction);
	}

	@Override
	public boolean updateUser(User user, boolean changeAddress) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		int flag = 0;
		// if change in address
		if (changeAddress) {
			deleteMatchedUsers(user.getId());
			List<UserMapping> userMatch = findMatchedUser(user.getId());
			persistUserMatch(userMatch, session);
			String poolId = getPoolForUser(user.getId(),session);
			if (poolId!=null && !poolId.equalsIgnoreCase(user.getId())) {
				List<Pool> recommendedPools = recommendedPools(user.getId());
				for (Pool pool : recommendedPools) {
					if (user.getPool().getId().equalsIgnoreCase(pool.getId())) {
						flag = 1;
						break;
					}
				}
				if (flag != 1) {
					return false;
				}
			}
		}
		// update user
		session.update(user.getId(), user);
		tx.commit();
		session.close();
		return true;
	}
	private String getPoolForUser(String userId,Session session) {
		String hql = "select user.pool.id from User user where user.id='"+userId+"'";		
		Query qry = session.createQuery(hql);
		String poolId = (String)qry.uniqueResult();
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
		Query qry = session.createQuery(hql);
		List<Pool> lstPool = qry.list();
		return lstPool;
	}

}