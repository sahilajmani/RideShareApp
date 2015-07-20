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

import pojos.OTP;
import pojos.Pool;
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
		OTP otpObjectByEmail = (OTP) cr.list().get(0);
		session.close();
		return otpObjectByEmail;
	}

	@Override
	public boolean insertUser(User user) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.save(user);
		tx.commit();

		List<UserMapping> userMatch = findMatchedUser(user.getId());
		for (UserMapping userMapping : userMatch) {
			session.save(userMapping);
			tx.commit();
		}
		session.close();
		return true;
	}

	@Override
	public boolean updateUser(User user) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Criteria cr = session.createCriteria(User.class);
		cr.add(Restrictions.eq("id", user.getId()));
		User updateUserVO = (User) cr.list().get(0);
		updateUserVO.setName(user.getName());
		updateUserVO.setEmail(user.getEmail());
		session.update(updateUserVO);
		tx.commit();
		List<UserMapping> userMatch = findMatchedUser(user.getId());
		for (UserMapping userMapping : userMatch) {
			session.save(userMapping);
			tx.commit();
		}
		session.close();
		return true;
	}

	@Override
	public User getUserDetails(String userId) {
		Session session = sessionFactory.openSession();
		Criteria cr = session.createCriteria(User.class);
		cr.add(Restrictions.eq("id", userId));
		User userVO = (User) cr.list().get(0);
		session.close();
		return userVO;
	}

	@Override
	public List<Pool> matchedPool(String userId) {
		return null;
		// List of pools returned for particular user.

	}

	@Override
	public List<UserMapping> findMatchedUser(String userId) {
		Session session = sessionFactory.openSession();
		User currentUser = this.getUserDetails(userId);
		String hql = "from User user where user.id<>'" + userId
				+ "' and (user.homeAddress.lattitude between "
				+ (currentUser.getHomeAddress().getLattitude() - 1) + " and "
				+ (currentUser.getHomeAddress().getLattitude() + 1)
				+ ") and (user.homeAddress.longitude between "
				+ (currentUser.getHomeAddress().getLongitude() - 1) + " and "
				+ (currentUser.getHomeAddress().getLongitude() + 1)
				+ ") and (user.officeAddress.longitude between "
				+ (currentUser.getOfficeAddress().getLongitude() - 1) + " and "
				+ (currentUser.getOfficeAddress().getLongitude() + 1)
				+ ") and (user.officeAddress.lattitude between "
				+ (currentUser.getOfficeAddress().getLattitude() - 1) + " and "
				+ (currentUser.getOfficeAddress().getLattitude() + 1) + ")";

		Query qry = session.createQuery(hql);

		List<User> userList = qry.list();
		System.out.println(userList.size());
		UserMatching userMatch = new UserMatching();
		try {
			return userMatch.getMatchedUsers(userList, currentUser);// will come
																	// from
			// DB
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//
		return null;

	}
}
