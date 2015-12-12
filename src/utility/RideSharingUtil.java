package utility;


import java.util.HashMap;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import dao.AuthenticationDaoI;
import dao.AuthenticationDaoImpl;
import dao.DaoI;
import dao.DaoImpl;
import dao.IChat;
import dao.IChatImpl;
import dao.ImageDAOImpl;
import dao.ImageDao;

public class RideSharingUtil {
	private static DaoI dao = null;
	private static SessionFactory sessionFactory = null;
	public static IChat chatDAO = null;
	public static AuthenticationDaoI authenticationDao = null;
	public static ImageDao imageDao = null;
	public static Object mutex = new Object();
	private static java.util.HashMap<String, Integer> otpMap = new HashMap<String, Integer>();
	public static Object mutexDao = new Object();	

	public static DaoI getDaoInstance() {
		synchronized (mutexDao) {
			if (dao == null) {
				dao = new DaoImpl();
			}
		}
		return dao;
	}

	public static IChat getChatInstance() {

		synchronized (mutex) {
			if (chatDAO == null) {
				chatDAO = new IChatImpl();
			}
		}
		return chatDAO;
	}
	public static void updateOTP(String email, int value){
		otpMap.put(email, value);
	}
	public static int getOTP(String email){
		return otpMap.get(email);
	}

	public static AuthenticationDaoI getAuthenticationDaoInstance() {

		synchronized (mutex) {
			if (authenticationDao == null) {
				authenticationDao = new AuthenticationDaoImpl();
			}
		}
		return authenticationDao;
	}

	public static ImageDao getImageDaoInstance() {

		synchronized (mutex) {
			if (imageDao == null) {
				imageDao = new ImageDAOImpl();
			}
		}
		return imageDao;
	}

	public static SessionFactory getSessionFactoryInstance() {

		if (sessionFactory == null) {
			Configuration configuration = new Configuration()
					.configure(GlobalConstants.HIBERNATE_CONFIG_XML);
			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
					.applySettings(configuration.getProperties()).build();
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		}
		return sessionFactory;
	}
	
}
