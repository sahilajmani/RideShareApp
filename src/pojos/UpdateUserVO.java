package pojos;

public class UpdateUserVO {
private User user;
private boolean changeAddress;

public User getUser() {
	return user;
}
public void setUser(User user) {
	this.user = user;
}
public boolean isChangeAddress() {
	return changeAddress;
}
public void setChangeAddress(boolean changeAddress) {
	this.changeAddress = changeAddress;
}
}
