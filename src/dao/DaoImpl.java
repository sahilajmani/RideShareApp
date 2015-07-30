package dao;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
																	// of user

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
	public List<PoolRequest> getPoolRequests(String userId) {
		Session session = sessionFactory.openSession();
		String hql = "from PoolRequest where user.id=?";
		Query qry = session.createQuery(hql);
		qry.setString(0, userId);
		List<PoolRequest> userPoolRequest = qry.list();
		session.close();
		return userPoolRequest;
	}

	@Override
	public boolean updatePoolRequest(PoolRequest request, int response) {
		boolean result = false;
		Session session = sessionFactory.openSession();
		String hql = "from PoolRequest where id=?";
		Query qry = session.createQuery(hql);
		qry.setString(0, request.getId());
		PoolRequest poolRequest = (PoolRequest) qry.list().get(0);
		if (response == GlobalConstants.REQUEST_ACCEPTED)
			addToPool(request.getUser(), request.getPool());
		poolRequest.setStatus(response);

		try {
			session.saveOrUpdate(poolRequest);
			result = true;
		} catch (Exception e) {
		} finally {
			session.close();
		}
		return result;
	}

	private boolean addToPool(User user, Pool pool) {
		if (!pool.getIsAvailable())
			return false;

		Session session = sessionFactory.openSession();
		pool.getParticipants().add(user);
		int noOfMembers = pool.getNumberOfMembers();
		pool.setNumberOfMembers(noOfMembers + 1);
		if (pool.getMax_members() == noOfMembers + 1)
			pool.setIsAvailable(false);

		session.saveOrUpdate(pool);

		String hql = "from Transactions where id='" + user.getId()
				+ "' and is_valid=true";
		Query qry = session.createQuery(hql);
		Transactions oldTransaction = (Transactions) qry.uniqueResult();
		oldTransaction.setIs_valid(false);
		Date currentDateTime = new Date();
		oldTransaction.setValid_to(currentDateTime);
		session.saveOrUpdate(oldTransaction);

		Transactions newTransaction = new Transactions();
		newTransaction.setIs_valid(true);
		newTransaction.setPool(pool);
		newTransaction.setUser(user);
		newTransaction.setValid_from(currentDateTime);
		newTransaction.setValid_to(new Date(9999, 12, 31, 00, 00, 00));
		session.save(newTransaction);
		return true;
	}

	@Override
	public boolean leavePool(User user, Pool pool) {

		if (!pool.getHostUserId().equals(user.getId())) {
			Session session = sessionFactory.openSession();
			pool.getParticipants().remove(user);
			int noOfMembers = pool.getNumberOfMembers();
			pool.setNumberOfMembers(noOfMembers - 1);
			pool.setIsAvailable(false);
			session.saveOrUpdate(pool);

			String hql = "from Transactions where id='" + user.getId()
					+ "' and (is_valid=true or pool.id='" + user.getId()
					+ "') order by valid_from desc";
			Query qry = session.createQuery(hql);
			Transactions oldTransaction = (Transactions) qry.list().get(0);
			oldTransaction.setIs_valid(false);
			Date currentDateTime = new Date();
			oldTransaction.setValid_to(currentDateTime);
			session.saveOrUpdate(oldTransaction);

			Transactions homeTransaction = (Transactions) qry.list().get(1);
			Transactions newTransaction = new Transactions();// primary key??
			newTransaction.setIs_valid(true);
			newTransaction.setPool(homeTransaction.getPool());
			newTransaction.setUser(homeTransaction.getUser());
			newTransaction.setValid_from(currentDateTime);
			newTransaction.setValid_to(new Date(9999, 12, 31, 00, 00, 00));
			session.save(newTransaction);

			return true;

		} else// if hostuser is leaving the pool
		{

			// write tomorrow

		}
		return false;

	}

	@Override
	public boolean insertUser(User user) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Pool pool = createPool(user);
		session.save(pool);
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
			if (!user.getPool().getId().equalsIgnoreCase(user.getId())) {
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