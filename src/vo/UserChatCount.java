package vo;

public class UserChatCount {

	private String sender_id;
	private int unreadMsgs=0;
	public String getSender_id() {
		return sender_id;
	}
	public void setSender_id(String sender_id) {
		this.sender_id = sender_id;
	}
	public int getUnreadMsgs() {
		return unreadMsgs;
	}
	public void setUnreadMsgs(int unreadMsgs) {
		this.unreadMsgs = unreadMsgs;
	}
}
