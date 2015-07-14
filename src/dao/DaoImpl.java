package dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import pojos.User;
import utility.RideSharingUtil;

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
		OTP otpObjectByEmail =(OTP) cr.list().get(0);
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
		OTP otpObjectByEmail =(OTP) cr.list().get(0);
		session.close();
		return otpObjectByEmail;
	}

	@Override
	public boolean insertUser(User user) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.save(user);
		tx.commit();
		session.close();
		return false;
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
}
