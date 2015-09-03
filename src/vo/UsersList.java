package vo;

import java.io.Serializable;
import java.util.List;

import pojos.User;

public class UsersList implements Serializable {
	
	private List<User> users;
	
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}

}
