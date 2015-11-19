package utility;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import dao.DaoI;
import dao.DaoImpl;
import dao.IChat;
import dao.IChatImpl;
import pojos.User;

public class RideSharingUtil {
	private static DaoI dao = null;
	private static SessionFactory sessionFactory = null;
	public static IChat chatDAO = null;
	public static Object mutex = new Object();
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
	public static void sendMail(User user, String body){
		
	}
}
