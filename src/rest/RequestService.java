package rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import pojos.ListPoolRequests;
import pojos.Pool;
import pojos.PoolRequest;
import pojos.RequestResponseVO;
import pojos.RestServiceResponse;
import pojos.User;
import utility.RideSharingUtil;
import vo.UserIdPoolIdVO;
import dao.DaoI;

@Path("/requests")
public class RequestService {
	DaoI dao = RideSharingUtil.getDaoInstance();
	@POST
	@Path("outgoingrequests")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ListPoolRequests getOutgoingRequest(User user){
		if(user==null || user.getId()==null)
			return null;
		
		ListPoolRequests listPoolRequests = new ListPoolRequests();

		List<PoolRequest> poolRequests = dao.getOutgoingPoolRequests(user.getId());
		List<PoolRequest> responsePoolRequests = new ArrayList<PoolRequest>();
		for(PoolRequest poolRequest : poolRequests){
			PoolRequest pReq = new PoolRequest();
			pReq.setCreated(poolRequest.getCreated());
			pReq.setDescription(poolRequest.getDescription());
			pReq.setId(poolRequest.getId());
			pReq.setStatus(poolRequest.getStatus());
			pReq.setUpdated(poolRequest.getUpdated());
			Pool poolDetails = dao.getPoolDetails(poolRequest.getPool().getId());
			pReq.setPool(poolDetails);
			responsePoolRequests.add(pReq);
		}
		listPoolRequests.setPoolRequests(responsePoolRequests);
		return listPoolRequests;
	}	
	
	@POST
	@Path("incomingrequests")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ListPoolRequests getIncomingRequest(User user) {
		if(user==null || user.getId()==null)
			return null;
		
		ListPoolRequests listPoolRequests = new ListPoolRequests();

		List<PoolRequest> poolRequests = dao.getIncomingPoolRequests(user.getId());
		List<PoolRequest> responsePoolRequests = new ArrayList<PoolRequest>();
		for(PoolRequest poolRequest : poolRequests){
			PoolRequest pReq = new PoolRequest();
			pReq.setCreated(poolRequest.getCreated());
			pReq.setDescription(poolRequest.getDescription());
			pReq.setId(poolRequest.getId());
			pReq.setStatus(poolRequest.getStatus());
			pReq.setUpdated(poolRequest.getUpdated());
			pReq.setUser(dao.getUserDetails(poolRequest.getUser().getId()));
			responsePoolRequests.add(pReq);
		}
		listPoolRequests.setPoolRequests(responsePoolRequests);
		return listPoolRequests;
	}

	@POST
	@Path("requestresponseservice")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RestServiceResponse sendRequestResponse(RequestResponseVO requestResponseVO) {
		RestServiceResponse restServiceResponse = new RestServiceResponse();
		restServiceResponse.setResponse(dao.updatePoolRequest(requestResponseVO.getRequestId(), requestResponseVO.getResponse()));
		return restServiceResponse;
	}
	
	@POST
	@Path("joinpoolrequest")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RestServiceResponse joinPoolRequest(UserIdPoolIdVO userIdPoolIdVO) {
		RestServiceResponse restServiceResponse = new RestServiceResponse();
		restServiceResponse.setResponse(dao.joinPoolRequest(userIdPoolIdVO.getUserId(), userIdPoolIdVO.getPoolId()));
		return restServiceResponse;
	}
}
