package pojos;

import java.io.Serializable;

public class RequestStatusVO implements Serializable {
	private boolean response;
	private int status;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isResponse() {
		return response;
	}

	public void setResponse(boolean response) {
		this.response = response;
	}
}
