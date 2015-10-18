package pojos;

import java.sql.Timestamp;
import java.util.Calendar;

public class PoolRequestResponse {

	private String id;
	private Pool pool;
	private User user;
	private int status;
	private Timestamp created;
	private Timestamp updated;
	private String description;
	private String distance;
	public PoolRequestResponse(){
		Calendar calendar = Calendar.getInstance();
		java.util.Date now = calendar.getTime();
				this.created=new Timestamp(now.getTime());
				this.updated= new Timestamp(now.getTime());
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Pool getPool() {
		return pool;
	}
	public void setPool(Pool pool) {
		this.pool = pool;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Timestamp getCreated() {
		return created;
	}
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	public Timestamp getUpdated() {
		return updated;
	}
	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	
	

}
