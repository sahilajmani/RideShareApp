package pojos;

import java.io.Serializable;
import java.util.List;

public class ListMatchPools implements Serializable{

	List<MatchedPoolsVO> matchedPoolsList;
	public List<MatchedPoolsVO> getPoolRequests() {
		return matchedPoolsList;
	}
	public void setPoolRequests(List<MatchedPoolsVO> matchedPoolsList) {
		this.matchedPoolsList = matchedPoolsList;
	}

}
