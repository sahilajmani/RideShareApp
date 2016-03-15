package pojos;

import java.util.Date;

import javax.persistence.Id;

public class OTPbySMS {
	private String number;
	private Integer id;
	private Date create_time;
	private int passcode;
	public String getNumber() {
		return number;
	}
	public void setNumber(String email) {
		this.number = email;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
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
