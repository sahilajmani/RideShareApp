package pojos;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.json.simple.JSONArray;

public class ListPoolRequests implements Serializable{
	
	List<PoolRequest> poolRequests;
	public List<PoolRequest> getPoolRequests() {
		return poolRequests;
	}
	public void setPoolRequests(List<PoolRequest> poolRequests) {
		this.poolRequests = poolRequests;
	}

}
