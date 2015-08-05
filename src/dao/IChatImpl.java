package dao;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import pojos.PrivateChat;
import utility.RideSharingUtil;

public class IChatImpl implements IChat {
	SessionFactory sessionFactory = RideSharingUtil.getSessionFactoryInstance();
	Logger logger = Logger.getLogger("debug");
	@Override
	public Collection<PrivateChat> getPrivateChats(String senderId, String receiverId, boolean markAsDelivered) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Criteria criteria = session.createCriteria(PrivateChat.class);
		criteria.add(Restrictions.eq("sender.id", senderId)).add(Restrictions.eq("receiver.id", receiverId));
		Collection<PrivateChat> result = criteria.list();
		if(result != null && result.size() > 0){
			if(markAsDelivered){
				String hql = "UPDATE PrivateChat set isDelivered = true "  + 
			             "WHERE receiver.id = :receiver_id and sender.id= :sender_id";
			Query query = session.createQuery(hql);
			query.setParameter("receiver_id", receiverId);
			query.setParameter("sender_id", senderId);
			int resultcount = query.executeUpdate();
			logger.log( Level.INFO, "updated private chats, marked as read "+resultcount);
			}
		}
		session.close();
		return result;
	}
	@Override
	public boolean saveChat(PrivateChat chat) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try{
		session.save(chat);
		}catch(Exception e){
			return false;
		}
		return chat.getId()!=null ? true:false;
	}

}
