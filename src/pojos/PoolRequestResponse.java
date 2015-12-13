package pojos;

import java.sql.Timestamp;
import java.util.Date;

public class PoolRequestResponse {

	private String id;
	private Pool pool;
	private User user;
	private int status;
	private Long created;
	private Long updated;
	private String description;
	private String distance;
	private String poolOwnername;

	public String getPoolOwnername() {
		return poolOwnername;
	}
	public void setPoolOwnername(String poolOwnername) {
		this.poolOwnername = poolOwnername;
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
	public Long getCreated() {
		return created;
	}
	public void setCreated(Long created) {
		this.created = created;
	}
	public Long getUpdated() {
		return updated;
	}
	public void setUpdated(Long updated) {
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
