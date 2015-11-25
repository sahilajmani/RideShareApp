package pojos;

import java.io.Serializable;
import java.util.Date;

public class Authentication implements Serializable{
	private String id;
	private String userName;
	private long tokenId;
	private Date currentTimestamp;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getCurrentTimestamp() {
		return currentTimestamp;
	}
	public void setCurrentTimestamp(Date currentTimestamp) {
		this.currentTimestamp = currentTimestamp;
	}
	public long getTokenId() {
		return tokenId;
	}
	public void setTokenId(long tokenId) {
		this.tokenId = tokenId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	

}
