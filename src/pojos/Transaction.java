package pojos;

import java.util.Date;

public class Transaction {
	User user;
	String id;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Pool getPool() {
		return pool;
	}
	public void setPool(Pool pool) {
		this.pool = pool;
	}
	public Date getValid_from() {
		return valid_from;
	}
	public void setValid_from(Date valid_from) {
		this.valid_from = valid_from;
	}
	public Date getValid_to() {
		return valid_to;
	}
	public void setValid_to(Date valid_to) {
		this.valid_to = valid_to;
	}
	public Boolean getIs_valid() {
		return is_valid;
	}
	public void setIs_valid(Boolean is_valid) {
		this.is_valid = is_valid;
	}
	Pool pool;
	Date valid_from;
	Date valid_to;
	Boolean is_valid;

}
