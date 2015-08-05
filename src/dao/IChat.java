package dao;

import java.util.Collection;

import pojos.PrivateChat;

public interface IChat {
	
	public Collection<PrivateChat> getPrivateChats(String sendId,String receiverId,boolean markAsDelivered);
	public boolean saveChat(PrivateChat chat);

}
