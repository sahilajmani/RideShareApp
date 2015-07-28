package pojos;

import java.sql.Timestamp;

public class PoolRequest {

	private String poolId;
	private String userId;
	private boolean status;
	private Timestamp created;
	private String description;
	
	public PoolRequest(String poolId, String userId, boolean status,
			Timestamp created, String description) {
		super();
		this.poolId = poolId;
		this.userId = userId;
		this.status = status;
		this.created = created;
		this.description = description;
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

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
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
		return "PoolRequest [poolId=" + poolId + ", userId=" + userId
				+ ", status=" + status + ", created=" + created
				+ ", description=" + description + "]";
	}
	
	
	
}
