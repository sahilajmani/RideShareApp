import java.util.Collection;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;


import pojos.Address;
import pojos.OTP;
import pojos.Time;
import pojos.User;
import utility.UserUtil;

public class DataLoaderRishabh {

	public static void main(String[] args) {
		Configuration configuration = new Configuration();
	    configuration.configure("resources/hibernate.cfg.xml");

	    ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
	            configuration.getProperties()).build();
	    SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		//Session session=sessionFactory.openSession();
		//Transaction tx = session.beginTransaction();
		
	/*	User user= new User();
		user.setName("Sahil Ajmani");
		user.setCompanyName("cisco");
		user.setContact("8861622290");
		user.setEmail("saajmani@cisco.com");
		Time startTime = new Time();
//		startTime.setId(1);
		startTime.setTime("9:30AM");
		Time endTime = new Time();
//		startTime.setId(2);
		startTime.setTime("11:30AM");
		user.setStartTime_pickUp(startTime);
		user.setStartTime_drop(endTime);
		Address address = new Address();
		address.setState("new Delhi");
		address.setAddressLine1("D117, Ajay Enclave");
		address.setAddressLine2("");
//		address.setId(1);
		user.setHomeAddress(address);
//		user.setId(1);
		session.save(user);
		System.out.println("user saved successfully ! "+user.getId());
		tx.commit();
		session.close();
		session=sessionFactory.openSession();
		Collection<User> result = UserUtil.getByName(session, "Sahil Ajmani");
		System.out.println(result);
		sessionFactory.close();*/
		
		Session session=sessionFactory.openSession();
		String hql = "from OTP";
		Query qry = session.createQuery(hql);
		List<OTP> lst = qry.list();
		for(OTP otp:lst){
			System.out.println("Email : " + otp.getEmail());
			System.out.println("OTP : " + otp.getPasscode());
			System.out.println("Create Time : " + otp.getCreate_time());		
		}
		sessionFactory.close();
		
		
	}
	
}
