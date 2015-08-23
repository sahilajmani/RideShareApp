package rest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import dao.DaoI;
import dao.IChat;
import RestResponse.GetChatResult;
import pojos.PrivateChat;
import pojos.User;
import utility.RideSharingUtil;
@Path("/chat")
public class ChatService {
	DaoI dao = RideSharingUtil.getDaoInstance();
	IChat chatDao = RideSharingUtil.getChatInstance();
	@POST
	@Path("getChat")
//	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public GetChatResult getChat(@FormParam("sender_Id") String sender_Id, @FormParam("receiver_Id") String receiver_id){
		GetChatResult chatResult = new GetChatResult();
		Collection<String> msgs = new ArrayList<String>();
		try{
		Collection <PrivateChat> result = chatDao.getPrivateChats(sender_Id, receiver_id, true);
		if(result!=null && result.size() > 0)
		for(PrivateChat chat : result){
			msgs.add(chat.getMsg());
		} 
		chatResult.setResult(msgs);
		}catch(Exception e){
			chatResult.setSuccess(false);
			chatResult.setErrorMsg(e.getMessage());
			return chatResult;
		}
		chatResult.setSuccess(true);
		return chatResult;
	}
	
	@POST
	@Path("sendChat")
//	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public GetChatResult sendChat(@FormParam("sender_Id") String sender_Id, @FormParam("receiver_Id") String receiver_id,@FormParam("message") String message){
		System.out.println(sender_Id+"\t"+receiver_id);
		GetChatResult chatResult = new GetChatResult();
		PrivateChat chat = new PrivateChat();
		// validate senderId and receiverId
		User sender = dao.getUserDetails(sender_Id);
		User receiver = dao.getUserDetails(receiver_id);
		if(sender == null || receiver == null){
			chatResult.setErrorMsg("Invalid user/reciver ID");
			chatResult.setSuccess(false);
			return chatResult;
		}
		chat.setReceiver(receiver);
		chat.setSender(sender);
		chat.setMsg(message);
		chat.setIsDelivered(false);
		Date currentTime = new Date();
		Timestamp currentTimestamp = new Timestamp(currentTime.getTime());
		chat.setCreateTime(currentTimestamp);
		try{
		chatDao.saveChat(chat);
		System.out.println("chat id -"+chat.getId());
		System.out.println("chat timestampt --- "+chat.getCreateTime());
		}catch(Exception e){
			chatResult.setErrorMsg(e.getMessage());
			chatResult.setSuccess(false);
			return chatResult;
		}
		chatResult.setSuccess(true);
		
		return chatResult;
		
	}
	
	
	
}
