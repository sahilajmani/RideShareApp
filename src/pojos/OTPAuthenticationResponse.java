package pojos;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class OTPAuthenticationResponse{

	private boolean response;
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isResponse() {
		return response;
	}

	public void setResponse(boolean response) {
		this.response = response;
	}
}
