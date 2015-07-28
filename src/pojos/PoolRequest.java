package pojos;

import java.sql.Timestamp;

public class PoolRequest {

	private String id;
	private String poolId;
	private String userId;
	private int status;
	private Timestamp created;
	private String description;
	
	

	public PoolRequest(String id, String poolId, String userId, int status,
			Timestamp created, String description) {
		super();
		this.id = id;
		this.poolId = poolId;
		this.userId = userId;
		this.status = status;
		this.created = created;
		this.description = description;
	}

	
	
	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getPoolId() {
		return poolId;
	}

	public void setPoolId(String poolId) {
		this.poolId = poolId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}



	@Override
	public String toString() {
		return "PoolRequest [id=" + id + ", poolId=" + poolId + ", userId="
				+ userId + ", status=" + status + ", created=" + created
				+ ", description=" + description + "]";
	}

	
	
	
}
