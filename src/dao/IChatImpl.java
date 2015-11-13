package dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
	public Collection<PrivateChat> getPrivateChats(String receiverId, boolean markAsDelivered) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Criteria criteria = session.createCriteria(PrivateChat.class);
		criteria/*.add(Restrictions.eq("sender.id", senderId))*/.add(Restrictions.eq("receiver.id", receiverId)).addOrder(Order.asc("sender.id")).add(Restrictions.eq("isDelivered", false));
		Collection<PrivateChat> result = criteria.addOrder(Order.asc("createTimeSeconds")).list();
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
		if(result!=null){
		Collection<PrivateChat> copyResult = new ArrayList<PrivateChat>();
		PrivateChat lastChat = null;
		for(PrivateChat chat : result){
			if(lastChat!=null&& (chat.getSender().getId().equals(lastChat.getSender().getId()))){
				lastChat.setMsg(lastChat.getMsg()+" . "+chat.getMsg());
				copyResult.add(chat);
			}else{
				lastChat=chat;
				
			}
		}
		result.removeAll(copyResult);
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
		tx.commit();
		}catch(Exception e){
			return false;
		}
		session.close();
		return chat.getId()!=null ? true:false;
	}
	@Override
	public PrivateChat getLastPrivateChat(String receiverId){
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(PrivateChat.class);
		criteria= criteria.add(Restrictions.eq("receiver.id", receiverId)).add(Restrictions.eq("isDelivered",true));
		criteria= criteria.addOrder(Order.desc("createTimeSeconds"));
		PrivateChat lastChat=null;
		try{
	 lastChat= (PrivateChat)criteria.list().get(0);
		}catch(Exception e){
			
		}finally {
			session.close();
		}
		return lastChat;
	}

}
