package pojos;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.json.simple.JSONArray;

public class ListPoolRequests implements Serializable{
	
	List<PoolRequestResponse> poolRequests;
	public List<PoolRequestResponse> getPoolRequests() {
		return poolRequests;
	}
	public void setPoolRequests(List<PoolRequestResponse> poolRequests) {
		this.poolRequests = poolRequests;
	}

}
