package rest;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import dao.DaoI;
import pojos.MatchedPoolsVO;
import pojos.Pool;
import pojos.User;
import utility.RideSharingUtil;
import vo.UsersList;

@Path("/poolservice")
public class PoolService {
	DaoI dao = RideSharingUtil.getDaoInstance();
	Logger logger = Logger.getLogger("debug");

	@POST
	@Path("getpoolservice")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Pool getPoolService(Pool pool) {
		Pool responsePool = new Pool();
		try {
			responsePool = dao.getPoolDetails(pool.getId());
		} catch (Exception e) {
			logger.info(e.getMessage());
			return null;
		}
		return responsePool;
	}

	@POST
	@Path("getmatchedpools")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<MatchedPoolsVO> getMatchedPoolsService(User user) {
		List<MatchedPoolsVO> matchPoolsList = new ArrayList<MatchedPoolsVO>();
		try {
			matchPoolsList = dao.getmatchedPool(user.getId());
		} catch (Exception e) {
			logger.info(e.getMessage());
			return null;
		}
		return matchPoolsList;
	}
	
	@POST
	@Path("getpoolusers")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public UsersList getPoolUsers(Pool pool) {
		UsersList users = new UsersList();
		List<User> participants = new ArrayList<User>();
		try {
			participants = dao.fetchPoolParticipants(pool.getId());
			users.setUsers(participants);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return null;
		}
		return users;
	}
	
	@GET
	@Path("leavepoolservice")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean leavePoolService(@QueryParam("userId")String userId) {
		if(!userId.isEmpty()){
		try {
			User user = dao.getUserDetails(userId);
			if(user!=null){
			dao.leavePool(userId, user.getPool().getId());	
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
			return false;
		}
		return true;
		}
		return false;
	}
}
