package pojos;

import java.util.Date;
import java.sql.Timestamp;

public class PrivateChat {

	private String id;
	private User sender;
	private User receiver;
	private String msg;
	private Date createTime;
	private Long createTimeSeconds;
	
	public Long getCreateTimeSeconds() {
		return createTimeSeconds;
	}
	public void setCreateTimeSeconds(Long createTimeSeconds) {
		this.createTimeSeconds = createTimeSeconds;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	private Boolean isDelivered;
	public String getId() {
		return id;
	}
/*	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}*/
	public void setId(String id) {
		this.id = id;
	}

	public User getSender() {
		return sender;
	}
	public void setSender(User sender) {
		this.sender = sender;
	}
	public User getReceiver() {
		return receiver;
	}
	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	

	/*	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}*/
	public Boolean getIsDelivered() {
		return isDelivered;
	}
	public void setIsDelivered(Boolean isDelivered) {
		this.isDelivered = isDelivered;
	}
	/*@Override
	public boolean equals(Object ob2){
		if(ob2 instanceof PrivateChat){
			if((((PrivateChat) ob2).getSender().getId()).equals(getSender().getId())){
				if((((PrivateChat) ob2).getReceiver().getId()).equals(getReceiver().getId())){
					if(getMsg().equals(((PrivateChat) ob2).getMsg())){
						if(getCreateTimeSeconds()!=null &&(getCreateTimeSeconds() == ((PrivateChat) ob2).getCreateTimeSeconds())){
						return true;
						}
					}
				}	
			}
		}
		return isDelivered;
		
		
	}*/
	
}
