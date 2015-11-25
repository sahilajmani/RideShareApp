package dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import pojos.Authentication;
import pojos.AuthenticationUsernamePassword;
import utility.RideSharingUtil;

public class AuthenticationDaoImpl implements AuthenticationDaoI {
	SessionFactory sessionFactory = RideSharingUtil.getSessionFactoryInstance();
	Logger logger = Logger.getLogger("debug");
	
	@Override
	public boolean authenticate(String username, String password) {
		Session session = sessionFactory.openSession();
		try{
		Criteria criteria = session.createCriteria(AuthenticationUsernamePassword.class);
		criteria.add(Restrictions.eq("userName", username));
		criteria.add(Restrictions.eq("password", password));
		if(criteria.list()!=null && criteria.list().size()>0){
			return true;
		}
		}catch(Exception e){
			return false;
		}finally{
		session.close();
		}
		return false;
	}

	@Override
	public void insertToken(String username, Long token) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Authentication authentication = new Authentication();
		authentication.setUserName(username);
		authentication.setTokenId(token);
		try {
			authentication.setCurrentTimestamp(this.getCurrentTime());
			session.save(authentication);
			tx.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			tx.rollback();
			throw new Exception("Error while inserting token");
		} finally{
			session.close();
		}
	}
	
	private Date getCurrentTime() throws ParseException {
		Date date = new Date();
		logger.info("Date" + date);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.parse(sdf.format(date));
	}

}
