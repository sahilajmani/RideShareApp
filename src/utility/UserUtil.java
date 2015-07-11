package utility;

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import pojos.User;
@SuppressWarnings("unchecked")
public class UserUtil {

	public static Collection<User> getByName(Session session,String name){
		Criteria cr = session.createCriteria(User.class);
		cr.add(Restrictions.eq("name", name));
		Collection<User> result =(Collection<User>) cr.list();
		return result;
		
	}
}
