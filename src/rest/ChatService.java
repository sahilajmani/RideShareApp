package rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import RestResponse.ChatResults;
import RestResponse.GetChatResult;
import pojos.PrivateChat;
import pojos.User;
import utility.RideSharingUtil;
import vo.ChatJson;
@Path("/chat")
public class ChatService {
	@GET
	@Path("getChat")
	@Produces(MediaType.APPLICATION_JSON)
	public GetChatResult getChat(@PathParam("receiverId")String chatJson){
		GetChatResult chatResult = new GetChatResult();
		Collection<String> msgs = new ArrayList<String>();
		try{
		Collection <PrivateChat> result = RideSharingUtil.getChatInstance().getPrivateChats( chatJson, true);
		Collection <ChatResults> chats = new ArrayList<ChatResults>();
		ChatResults chatres = null;
		if(result!=null && result.size() > 0){
		for(PrivateChat chat : result){
			chatres= new ChatResults();
			chatres.setMessage(chat.getMsg());
			chatres.setReceiverId(chat.getReceiver().getId());
			chatres.setSenderId(chat.getSender().getId());
			chatres.setSenderName(chat.getSender().getName());
			chatres.setCreateTime(chat.getCreateTime());
			chatres.setCreateTimeSeconds(chat.getCreateTimeSeconds());
//			msgs.add(chat.getMsg());
			chats.add(chatres);
		} 
		}
//		chatResult.setResult(msgs);
		chatResult.setChats(chats);
		}catch(Exception e){
			e.printStackTrace();
			chatResult.setSuccess(false);
			chatResult.setErrorMsg(e.getMessage());
			return chatResult;
		}
		chatResult.setSuccess(true);
		return chatResult;
	}
	@POST
	@Path("sendChat")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public GetChatResult sendChat(ChatJson chatJson) throws ParseException{
		GetChatResult chatResult = new GetChatResult();
		PrivateChat chat = new PrivateChat();
		// validate senderId and receiverId
		User sender = RideSharingUtil.getDaoInstance().getUserDetails(chatJson.getSender_Id());
		User receiver = RideSharingUtil.getDaoInstance().getUserDetails(chatJson.getReceiver_Id());
		if( receiver == null ||sender ==null){
			chatResult.setErrorMsg("Invalid user/reciver ID");
			chatResult.setSuccess(false);
			return chatResult;
		}
		chat.setReceiver(receiver);
		chat.setSender(sender);
		chat.setMsg(chatJson.getMessage());
		chat.setIsDelivered(false);
		System.out.println(new Date().getTime());
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		chat.setCreateTime(sdf.parse(sdf.format(date)));//new Date().getTime()));
		chat.setCreateTimeSeconds(System.currentTimeMillis());
		//chat.setCreateTime(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
//		chat.setCreateTime(new Date(System.currentTimeMillis()));
		try{
		RideSharingUtil.getChatInstance().saveChat(chat);
		System.out.println("chat id -"+chat.getId());
		System.out.println("chat timestamp --- "+chat.getCreateTime());
		}catch(Exception e){
			chatResult.setErrorMsg(e.getMessage());
			chatResult.setSuccess(false);
			return chatResult;
		}
		chatResult.setSuccess(true);
		
		return chatResult;
		
	}
	
	
	
}