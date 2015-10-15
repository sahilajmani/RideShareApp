package dao;

import java.util.Collection;

import pojos.PrivateChat;

public interface IChat {
	
	public Collection<PrivateChat> getPrivateChats(String receiverId,boolean markAsDelivered);
	public boolean saveChat(PrivateChat chat);
	public PrivateChat getLastPrivateChat(String receiverId);
}
