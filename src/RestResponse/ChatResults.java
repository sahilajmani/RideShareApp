package RestResponse;

import java.util.Date;

public class ChatResults {
	
	private String receiverId;
	private String senderId;
	private String message;
	private String senderName;
	private Long createTimeSeconds;
	public String getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}
	public Long getCreateTimeSeconds() {
		return createTimeSeconds;
	}
	public void setCreateTimeSeconds(Long createTimeSeconds) {
		this.createTimeSeconds = createTimeSeconds;
	}
	public String getSenderId() {
		return senderId;
	}
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	private Date createTime;

}
