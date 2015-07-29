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
import pojos.Transactions;
import pojos.User;
import pojos.UserMapping;
import utility.RideSharingUtil;
import utility.UserMatching;

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

	@Override
	public boolean insertUpdateUser(User user) {
		Session session = sessionFactory.openSession();
		String hql = "select user.id from User user";
		Query qry = session.createQuery(hql);
		List<String> lst = qry.list();
		Transaction tx = session.beginTransaction();
		// Existing User
		if (lst != null && lst.size() > 0 && lst.contains(user.getId())) {
			int flag = 0;
			// if change in address
			if (doFindMatchedUser(user)) {
				List<UserMapping> userMatch = findMatchedUser(user.getId());
				List<String> listUsersMatchId = new ArrayList<String>();
				for (UserMapping uMap : userMatch) {
					listUsersMatchId.add(uMap.getUserB().getId());
				}
				List<String> listUsersMatchIdFromDB = getMatchedUserFromDB(user
						.getId());
				if (listUsersMatchId != null
						&& listUsersMatchIdFromDB != null
						&& listUsersMatchId.size() == listUsersMatchIdFromDB
								.size()) {
					for (String userId : listUsersMatchId) {
						if (listUsersMatchIdFromDB.contains(userId)) {
							continue;
						} else {
							flag = 1;
							break;
						}
					}
				} else {
					flag = 1;
				}

				if (flag == 1) {
					if (deleteMatchedUsers(user.getId())) {
						if (null != userMatch && userMatch.size() > 0) {
							persistUserMatch(userMatch, session);
						}
						// remove from existing pool and make default pool
						// active
					} else {
						return false;
					}
				}
			}
			// update user
			session.saveOrUpdate(user.getId(), user);
			tx.commit();

		} else { // New User
			// insert user
			session.save(user);
			// create pool
			createPool(user, session);
			// persist matched users
			List<UserMapping> userMatch = findMatchedUser(user.getId());
			if (null != userMatch && userMatch.size() > 0) {
				persistUserMatch(userMatch, session);
			}
			tx.commit();
		}
		session.close();
		return true;
	}

	private List<String> getMatchedUserFromDB(String id) {
		Session session = sessionFactory.openSession();
		String hql = "select userB.id from UserMapping where userA.id=?";
		Query qry = session.createQuery(hql);
		qry.setString(0, id);
		List<String> lst = qry.list();
		session.close();
		return lst;
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
	private boolean doFindMatchedUser(User user) {
		Address homeAddressFromDB = getHomeAddressFromDB(user.getId());
		Address officeAddressFromDB = getOfficeAddressFromDB(user.getId());
		Address homeAddress = user.getHomeAddress();
		Address officeAddress = user.getOfficeAddress();
		if (homeAddressFromDB != null && officeAddressFromDB != null
				&& homeAddress != null && officeAddress != null) {
			if (homeAddressFromDB.getLattitude() == homeAddress.getLattitude()
					&& homeAddressFromDB.getLongitude() == homeAddress
							.getLongitude()
					&& officeAddressFromDB.getLattitude() == officeAddress
							.getLattitude()
					&& officeAddressFromDB.getLongitude() == officeAddress
							.getLongitude()) {
				return false;
			}
		}
		return true;
	}

	private Address getOfficeAddressFromDB(String id) {
		Session session = sessionFactory.openSession();
		String hql = "select user.officeAddress from User user where user.id=?";
		Query qry = session.createQuery(hql);
		qry.setString(0, id);
		Address officeAddress = (Address) qry.uniqueResult();
		session.close();
		return officeAddress;
	}

	private Address getHomeAddressFromDB(String id) {
		Session session = sessionFactory.openSession();
		String hql = "select user.homeAddress from User user where user.id=?";
		Query qry = session.createQuery(hql);
		qry.setString(0, id);
		Address officeAddress = (Address) qry.uniqueResult();
		session.close();
		return officeAddress;
	}

	private void createPool(User user, Session session) {
		Pool pool = new Pool();
		pool.setId(user.getId());
		pool.setHostUserId(user.getId());
		pool.setIs_active(true);
		pool.setIsAvailable(true);
		pool.setSourceAddress(user.getHomeAddress());
		pool.setDestinationAddress(user.getOfficeAddress());
		session.save(pool);
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
		for (UserMapping userMatch : userMapping) {
			session.save(userMatch);
		}
	}

public List<Transactions> getUserPoolRecord(String userId) { //pool transaction history of user
		
		Session session = sessionFactory.openSession();
		String hql = "from Transactions where user.id=?";
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

}
