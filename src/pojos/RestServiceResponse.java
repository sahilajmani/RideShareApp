package pojos;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement()
public class RestServiceResponse {	
private boolean response;

public boolean isResponse() {
	return response;
}
public void setResponse(boolean response) {
	this.response = response;
}
}
