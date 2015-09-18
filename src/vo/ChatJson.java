package vo;

public class ChatJson {

	private String sender_Id;
	private String receiver_Id;
	private String message;
	public String getSender_Id() {
		return sender_Id;
	}
	public void setSender_Id(String sender_Id) {
		this.sender_Id = sender_Id;
	}
	public String getReceiver_Id() {
		return receiver_Id;
	}
	public void setReceiver_Id(String receiver_Id) {
		this.receiver_Id = receiver_Id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
