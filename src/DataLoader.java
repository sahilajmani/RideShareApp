import java.util.Collection;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import pojos.Address;
import pojos.User;
import utility.UserUtil;

public class DataLoader {

public static void main(String[] args) {
Configuration configuration = new Configuration();
   configuration.configure("resources/hibernate.cfg.xml");

   ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
           configuration.getProperties()).build();
   SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
Session session=sessionFactory.openSession();
//Transaction tx = session.beginTransaction();
	
User user= new User();
user.setName("Test2");
/*user.setCompanyName("cisco");
user.setContact("8861622290");
user.setEmail("@cisco.com");
Time startTime = new Time();
//	startTime.setId(1);
startTime.setTime("9:30AM");	
Time endTime = new Time();
//	startTime.setId(2);
startTime.setTime("11:30AM");
user.setStartTime_pickUp(startTime);
user.setStartTime_drop(endTime);
*/
user.setHasCar(true);
user.setDistance(28.0f);
Address address = new Address();
/*address.setState("new Delhi");
address.setAddressLine1("D117, Ajay Enclave");
address.setAddressLine2("");
//	address.setId(1);
user.setHomeAddress(address);
//	user.setId(1);
*/
Address addr= new Address();
addr.setLattitude(28.673751);
addr.setLongitude(77.127338);

Address add2= new Address();
add2.setLattitude(28.495781);
add2.setLongitude(77.08826);
user.setHomeAddress(addr);
user.setOfficeAddress(add2);
//session.save(user);
System.out.println("address saved successfully ! "+user.getId());
//tx.commit();
session.close();
sessionFactory.close();
}
}

	
	
	
