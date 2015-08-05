package RestResponse;

import java.util.Collection;

import pojos.PrivateChat;

public class GetChatResult {

	Collection <String> result ;
	boolean success;
	String errorMsg;
	public Collection<String> getResult() {
		return result;
	}
	public void setResult(Collection<String> result) {
		this.result = result;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
}
