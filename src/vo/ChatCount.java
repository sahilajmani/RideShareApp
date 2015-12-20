package vo;

import java.util.ArrayList;
import java.util.Collection;

public class ChatCount {
	private Collection<UserChatCount> countList= new ArrayList<UserChatCount>();
	private boolean success=false;
	public Collection<UserChatCount> getCountList() {
		return countList;
	}
	public void setCountList(Collection<UserChatCount> countList) {
		this.countList = countList;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}

}
