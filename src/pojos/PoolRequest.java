package pojos;

import java.sql.Timestamp;

public class PoolRequest {

	private String id;
	private Pool pool;
	private User user;
	private int status;
	private Timestamp created;
	private Timestamp updated;
	private String description;
	

	

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


	@Override
	public String toString() {
		return "PoolRequest [id=" + id + ", pool=" + pool + ", user=" + user
				+ ", status=" + status + ", created=" + created + ", updated="
				+ updated + ", description=" + description + "]";
	}



		
}
