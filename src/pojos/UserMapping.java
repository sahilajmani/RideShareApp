package pojos;


public class UserMapping {
	private String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	private User userA;
	private User userB;
	private float distance;
	public User getUserA() {
		return userA;
	}
	public void setUserA(User userA) {
		this.userA = userA;
	}
	public User getUserB() {
		return userB;
	}
	public void setUserB(User userB) {
		this.userB = userB;
	}
	public float getDistance() {
		return distance;
	}
	public void setDistance(float distance) {
		this.distance = distance;
	}
	
}
