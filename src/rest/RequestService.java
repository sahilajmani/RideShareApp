package rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import pojos.ListPoolRequests;
import pojos.Pool;
import pojos.PoolRequest;
import pojos.PoolRequestResponse;
import pojos.RequestResponseVO;
import pojos.RequestStatusVO;
import pojos.RestServiceResponse;
import pojos.User;
import utility.GlobalConstants;
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
	public ListPoolRequests getOutgoingRequest(User user,@Context HttpHeaders hh) throws Exception {
		String authorization = hh.getRequestHeaders().get("Authorization")!=null?hh.getRequestHeaders().get("Authorization").toString():"";
		if(authorization.equals("") || !authorization.equals("["+GlobalConstants.AUTH_STRING+"]")){
			throw new Exception("Not Authorized Exception");
		}
		if (user == null || user.getId() == null)
			return null;

		ListPoolRequests listPoolRequests = new ListPoolRequests();

		List<PoolRequest> poolRequests = dao.getOutgoingPoolRequests(user
				.getId());
		List<PoolRequestResponse> responsePoolRequests = new ArrayList<PoolRequestResponse>();
		for (PoolRequest poolRequest : poolRequests) {
			PoolRequestResponse pReq = new PoolRequestResponse();
			pReq.setCreated(poolRequest.getCreated());
			pReq.setDistance(poolRequest.getDistance().toString());
			pReq.setDescription(poolRequest.getDescription());
			pReq.setId(poolRequest.getId());
			pReq.setStatus(poolRequest.getStatus());
			pReq.setUpdated(poolRequest.getUpdated());
			Pool poolDetails = dao
					.getPoolDetails(poolRequest.getPool().getId());
			pReq.setPool(poolDetails);
			pReq.setUser(dao.getUserDetails(poolRequest.getUser().getId()));
			pReq.setPoolOwnername(dao.getUserDetails(poolDetails.getId()).getName());;
			responsePoolRequests.add(pReq);
		}
		listPoolRequests.setPoolRequests(responsePoolRequests);
		return listPoolRequests;
	}

	@POST
	@Path("incomingrequests")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ListPoolRequests getIncomingRequest(User user,@Context HttpHeaders hh) throws Exception {
		String authorization = hh.getRequestHeaders().get("Authorization")!=null?hh.getRequestHeaders().get("Authorization").toString():"";
		if(authorization.equals("") || !authorization.equals("["+GlobalConstants.AUTH_STRING+"]")){
			throw new Exception("Not Authorized Exception");
		}
		if (user == null || user.getId() == null)
			return null;

		ListPoolRequests listPoolRequests = new ListPoolRequests();

		List<PoolRequest> poolRequests = dao.getIncomingPoolRequests(user
				.getId());
		List<PoolRequestResponse> responsePoolRequests = new ArrayList<PoolRequestResponse>();
		for (PoolRequest poolRequest : poolRequests) {
			PoolRequestResponse pReq = new PoolRequestResponse();
			pReq.setCreated(poolRequest.getCreated());
			pReq.setDescription(poolRequest.getDescription());
			pReq.setId(poolRequest.getId());
			pReq.setStatus(poolRequest.getStatus());
			pReq.setUpdated(poolRequest.getUpdated());
			pReq.setDistance(poolRequest.getDistance().toString());
			pReq.setUser(dao.getUserDetails(poolRequest.getUser().getId()));
			pReq.setPool(dao.getPoolDetails(poolRequest.getPool().getId()));
			responsePoolRequests.add(pReq);
		}
		listPoolRequests.setPoolRequests(responsePoolRequests);
		return listPoolRequests;
	}

	@POST
	@Path("requestresponseservice")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RestServiceResponse sendRequestResponse(
			RequestResponseVO requestResponseVO,@Context HttpHeaders hh) throws Exception {
		String authorization = hh.getRequestHeaders().get("Authorization")!=null?hh.getRequestHeaders().get("Authorization").toString():"";
		if(authorization.equals("") || !authorization.equals("["+GlobalConstants.AUTH_STRING+"]")){
			throw new Exception("Not Authorized Exception");
		}
		RestServiceResponse restServiceResponse = new RestServiceResponse();
		restServiceResponse.setResponse(dao.updatePoolRequest(
				requestResponseVO.getRequestId(),
				requestResponseVO.getResponse()));
		return restServiceResponse;
	}

	@POST
	@Path("joinpoolrequest")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RestServiceResponse joinPoolRequest(UserIdPoolIdVO userIdPoolIdVO,@Context HttpHeaders hh) throws Exception {
		String authorization = hh.getRequestHeaders().get("Authorization")!=null?hh.getRequestHeaders().get("Authorization").toString():"";
		if(authorization.equals("") || !authorization.equals("["+GlobalConstants.AUTH_STRING+"]")){
			throw new Exception("Not Authorized Exception");
		}
		RestServiceResponse restServiceResponse = new RestServiceResponse();
		restServiceResponse.setResponse(dao.joinPoolRequest(userIdPoolIdVO,
				(float) 0.0));
		return restServiceResponse;
	}

	@POST
	@Path("cancelpoolrequest")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean cancelPoolRequest(RequestResponseVO requestResponseVO,@Context HttpHeaders hh) throws Exception {
		String authorization = hh.getRequestHeaders().get("Authorization")!=null?hh.getRequestHeaders().get("Authorization").toString():"";
		if(authorization.equals("") || !authorization.equals("["+GlobalConstants.AUTH_STRING+"]")){
			throw new Exception("Not Authorized Exception");
		}
		String requestId = requestResponseVO.getRequestId();
		try {
			dao.updatePoolRequest(requestId, GlobalConstants.REQUEST_CANCEL);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@POST
	@Path("getrequeststatus")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RequestStatusVO getRequestStatus(UserIdPoolIdVO userIdPoolIdVO,@Context HttpHeaders hh) throws Exception {
		String authorization = hh.getRequestHeaders().get("Authorization")!=null?hh.getRequestHeaders().get("Authorization").toString():"";
		if(authorization.equals("") || !authorization.equals("["+GlobalConstants.AUTH_STRING+"]")){
			throw new Exception("Not Authorized Exception");
		}
		RequestStatusVO requestStatusVO = new RequestStatusVO();
		if (userIdPoolIdVO != null && !userIdPoolIdVO.getPoolId().isEmpty()
				&& !userIdPoolIdVO.getUserId().isEmpty()) {
			try {
				PoolRequest poolRequest = dao.getPoolRequestVO(userIdPoolIdVO);
				if (poolRequest != null) {
					if (poolRequest.getStatus() >= 0) {
						requestStatusVO.setStatus(poolRequest.getStatus());
						requestStatusVO.setRequestId(poolRequest.getId());
					} else {
						requestStatusVO.setStatus(-1);
					}
					requestStatusVO.setResponse(true);
				} else {
					requestStatusVO.setStatus(-1);
					requestStatusVO.setResponse(false);
				}
			} catch (Exception e) {
				requestStatusVO.setStatus(-1);
				requestStatusVO.setResponse(false);
			}
		} else {
			requestStatusVO.setStatus(-1);
			requestStatusVO.setResponse(false);
		}
		return requestStatusVO;
	}
}
