package vo;

public class UserIdPoolIdVO {
	private String userId;
	private String poolId;
	private int status;
	private float distance;
	

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
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
}
