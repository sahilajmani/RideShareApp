package pojos;

import java.io.Serializable;
import java.util.Date;

public class OTP implements Serializable{

	private String email;
	private String id;
	private Date create_time;
	private int passcode;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public int getPasscode() {
		return passcode;
	}
	public void setPasscode(int passcode) {
		this.passcode = passcode;
	}
	
}
