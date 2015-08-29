package rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import dao.DaoI;
import pojos.ListMatchPools;
import pojos.MatchedPoolsVO;
import pojos.Pool;
import pojos.User;
import utility.RideSharingUtil;

@Path("/poolservice")
public class PoolService {
	DaoI dao = RideSharingUtil.getDaoInstance();
	
	@POST
	@Path("getpoolservice")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Pool getPoolService(Pool pool){
	Pool responsePool = new Pool();
	responsePool=dao.getPoolDetails(pool.getId());	
	return responsePool;
	}	
	
	@POST
	@Path("getmatchedpools")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<MatchedPoolsVO> getMatchedPoolsService(User user){	
		 List<MatchedPoolsVO> matchPoolsList= new ArrayList<MatchedPoolsVO>();
		 matchPoolsList = dao.getmatchedPool(user.getId());
	//	 System.out.println(matchPoolsList.get(0).getDistance());
		 return  matchPoolsList;
	}
}
