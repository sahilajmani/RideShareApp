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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import pojos.MatchedPoolsVO;
import pojos.Pool;
import pojos.RestServiceResponse;
import pojos.User;
import utility.DistanceBwPlaces;
import utility.GlobalConstants;
import utility.RideSharingUtil;
import vo.UsersList;
import dao.DaoI;

@Path("/poolservice")
public class PoolService {
	DaoI dao = RideSharingUtil.getDaoInstance();
	Logger logger = Logger.getLogger("debug");

	@POST
	@Path("getpoolservice")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Pool getPoolService(Pool pool,@Context HttpHeaders hh) throws Exception {
		String authorization = hh.getRequestHeaders().get("Authorization")!=null?hh.getRequestHeaders().get("Authorization").toString():"";
		if(authorization.equals("") || !authorization.equals("["+GlobalConstants.AUTH_STRING+"]")){
			throw new Exception("Not Authorized Exception");
		}
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
	public List<MatchedPoolsVO> getMatchedPoolsService(User user,@Context HttpHeaders hh) throws Exception {
		String authorization = hh.getRequestHeaders().get("Authorization")!=null?hh.getRequestHeaders().get("Authorization").toString():"";
		if(authorization.equals("") || !authorization.equals("["+GlobalConstants.AUTH_STRING+"]")){
			throw new Exception("Not Authorized Exception");
		}
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
	@Path("getmatchedpoolsforunregistered")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<MatchedPoolsVO> getMatchedPoolsForUnregistered(User user,@Context HttpHeaders hh) throws Exception {
		String authorization = hh.getRequestHeaders().get("Authorization")!=null?hh.getRequestHeaders().get("Authorization").toString():"";
		if(authorization.equals("") || !authorization.equals("["+GlobalConstants.AUTH_STRING+"]")){
			throw new Exception("Not Authorized Exception");
		}
		List<MatchedPoolsVO> matchPoolsList = new ArrayList<MatchedPoolsVO>();
		try {
			double distance = DistanceBwPlaces.getDistanceandDuration(user
					.getHomeAddress().getLattitude(), user.getHomeAddress()
					.getLongitude(), user.getOfficeAddress().getLattitude(),
					user.getOfficeAddress().getLongitude());
			logger.info("distance stored for user : " + distance);
			user.setDistance((float) distance);
			user.setId("temp1234");
			matchPoolsList = dao.getmatchedPoolForUnregistered(user);
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
	public UsersList getPoolUsers(Pool pool,@Context HttpHeaders hh) throws Exception {
		String authorization = hh.getRequestHeaders().get("Authorization")!=null?hh.getRequestHeaders().get("Authorization").toString():"";
		if(authorization.equals("") || !authorization.equals("["+GlobalConstants.AUTH_STRING+"]")){
			throw new Exception("Not Authorized Exception");
		}
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
	public RestServiceResponse leavePoolService(
			@QueryParam("userId") String userId,@Context HttpHeaders hh) throws Exception {
		String authorization = hh.getRequestHeaders().get("Authorization")!=null?hh.getRequestHeaders().get("Authorization").toString():"";
		if(authorization.equals("") || !authorization.equals("["+GlobalConstants.AUTH_STRING+"]")){
			throw new Exception("Not Authorized Exception");
		}
		RestServiceResponse response = new RestServiceResponse();
		if (!userId.isEmpty()) {
			try {
				User user = dao.getUserDetails(userId);
				if (user != null) {
					if (dao.leavePool(userId, user.getPool().getId())) {
						response.setUser(dao.getUserDetails(userId));
						response.setResponse(true);
					} else {
						response.setUser(user);
						response.setResponse(false);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				response.setResponse(false);

			}
		}
		return response;
	}

}
