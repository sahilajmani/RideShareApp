package rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import RestResponse.ChatResults;
import RestResponse.GetChatResult;
import email.SendMail;
import pojos.PrivateChat;
import pojos.RestServiceResponse;
import pojos.User;
import utility.GlobalConstants;
import utility.RideSharingUtil;
import vo.ChatJson;
@Path("/chat")
public class ChatService {
	@GET
	@Path("getChat")
	@Produces(MediaType.APPLICATION_JSON)
	public GetChatResult getChat(@QueryParam("receiverId") String chatJson1){
		ChatJson chatJson= new ChatJson();
		chatJson.setReceiver_Id(chatJson1);
		GetChatResult chatResult = new GetChatResult();
		Collection<String> msgs = new ArrayList<String>();
		try{
		Collection <PrivateChat> result = RideSharingUtil.getChatInstance().getPrivateChats( chatJson.getReceiver_Id(), true);
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
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		chat.setCreateTime(sdf.parse(sdf.format(date)));//new Date().getTime()));
		Long currentTime=System.currentTimeMillis();
		chat.setCreateTimeSeconds(currentTime);
		try{
		RideSharingUtil.getChatInstance().saveChat(chat);
		}catch(Exception e){
			chatResult.setErrorMsg(e.getMessage());
			chatResult.setSuccess(false);
			return chatResult;
		}
		chatResult.setSuccess(true);
		PrivateChat lastChat = (PrivateChat) RideSharingUtil.getChatInstance().getLastPrivateChat(chatJson.getReceiver_Id());
		if(lastChat!= null){
		Long lastChatTime=lastChat.getCreateTimeSeconds();
		System.out.println("Last Chat --   Message - "+lastChat.getMsg()+"\n Time - "+lastChat.getCreateTimeSeconds()+
				"Diff -- \t"+(currentTime-lastChatTime));
		if(currentTime-lastChatTime > GlobalConstants.NOTIFICATION_CHAT_TIMEOUT){
			notifyUser(receiver,sender,chat);
		}
		}
		return chatResult;
		
	}
	private void notifyUser(User receiver, User sender, PrivateChat chat) {
		// TODO Auto-generated method stub
		System.out.println("Sending mail to user -- "+receiver.getName());
		String userEmail = receiver.getEmail();
			String message = "Hi "+receiver.getName()+", \n You have received a new message from "+
					sender.getName()+
					"Please open the RideEasy app to reply to the user. \n Thanks, \n RideEasy Team";
			String subject = "You have received a new message from "+sender.getName();
			String[] to = { userEmail };
					SendMail.sendEmail(GlobalConstants.FROM_EMAIL,
							GlobalConstants.PASSWORD_EMAIL, subject, message,
							to);
					
	}
	
	
	
}