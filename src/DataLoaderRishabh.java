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
		Session session=sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
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
		
		/*Session session=sessionFactory.openSession();
		String hql = "from OTP";
		Query qry = session.createQuery(hql);
		List<OTP> lst = qry.list();
		for(OTP otp:lst){
			System.out.println("Email : " + otp.getEmail());
			System.out.println("OTP : " + otp.getPasscode());
			System.out.println("Create Time : " + otp.getCreate_time());		
		}
		sessionFactory.close();*/
	   
		Address add1=new Address();
		//add1.setId("1");
		add1.setLattitude(28.70);
		add1.setLongitude(77.16);
		add1.setAddressLine1("ff");
		add1.setAddressLine2("fff");
		add1.setCity("delhi");
		add1.setPincode("110055");
		add1.setState("delhi");
		Address add2=new Address();
		//add2.setId("2");
		add2.setLattitude(28.426852);
		add2.setLongitude(77.031367);
		/*Address add3=new Address();
		add3.setId("3");
		add3.setLattitude(28.673751);
		add3.setLongitude(77.127338);
		Address add4=new Address();
		add4.setId("4");
		add4.setLattitude(28.495781);
		add4.setLongitude(77.08826);*/
		User user1=new User();
		//user1.setId("1001");
		user1.setHomeAddress(add1);
		user1.setOfficeAddress(add2);
		user1.setHasCar(false);
		user1.setDistance(41.5f);
/*		User user2=new User();
		user2.setId("1002");
		user2.setHasCar(false);
		user2.setHomeAddress(add3);
		user2.setOfficeAddress(add2);
		user2.setDistance((float) 36.9);*/
		// Session session = sessionFactory.openSession();
		//	Transaction tx = session.beginTransaction();	
			
			session.save(add1);
			System.out.println("address saved successfully ! ");
			/*session.save(add2);
			session.save(add3);
			session.save(add4);*/
			//session.save(user1);
			//session.save(user2);
		tx.commit();	
		
		session.close();
		
		
	}
	
}
