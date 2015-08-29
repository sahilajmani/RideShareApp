package Test;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import pojos.Pool;
import pojos.User;

public class AsscociationTest {
	public static void main(String[] args) {
//		
//	
//	Configuration configuration = new Configuration();
//	   configuration.configure("resources/hibernate.cfg.xml");
//
//	   ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
//	           configuration.getProperties()).build();
//	   SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
//	Session session=sessionFactory.openSession();
//	Transaction tx = session.beginTransaction();
//	
//	List<User> users;
//	Criteria cr = session.createCriteria(User.class);
//	users = cr.list();
//	System.out.println("no of users returned - "+users.size());
//	System.out.println(users);
//	User hostUser = (User)users.toArray()[0];
//	Pool newPool = new Pool();
////	newPool.setId("");
//	newPool.setHostUserId(hostUser.getId());
//	users.remove(hostUser);
//	//newPool.setParticipants(users);
//	newPool.setId("DefaultPoolId1442553234");
////	session.save(newPool);
//	tx.commit();
//	System.out.println("NewPool saved ! pool id - "+newPool.getId());
//	session.close();
//	sessionFactory.close();
//	
	}
}
