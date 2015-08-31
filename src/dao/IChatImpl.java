package dao;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
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
		criteria.add(Restrictions.eq("sender.id", senderId))/*.add(Restrictions.eq("receiver.id", receiverId))*/.add(Restrictions.eqOrIsNull("isDelivered", false));
		Collection<PrivateChat> result = criteria.addOrder(Order.desc("createTime")).list();
		if(result != null && result.size() > 0){
			int resultCount = 0;
			if(markAsDelivered){
				for(PrivateChat chat : result){
					chat.setIsDelivered(true);
					session.update(chat);
					resultCount++;
					
				}
			logger.log( Level.INFO, "updated private chats, marked as read "+resultCount);
			}
		}
		tx.commit();
		session.close();
		return result;
	}
	@Override
	public boolean saveChat(PrivateChat chat) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try{
		session.save(chat);
		tx.commit();
		}catch(Exception e){
			return false;
		}
		return chat.getId()!=null ? true:false;
	}

}
