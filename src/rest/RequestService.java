package rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import dao.DaoI;
import pojos.ListPoolRequests;
import pojos.Pool;
import pojos.PoolRequest;
import pojos.User;
import utility.RideSharingUtil;

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

		List<PoolRequest> poolRequests = dao.getOutGoingPoolRequests(user.getId());
		List<PoolRequest> responsePoolRequests = new ArrayList<PoolRequest>();
		for(PoolRequest poolRequest : poolRequests){
			PoolRequest pReq = new PoolRequest();
			pReq.setCreated(poolRequest.getCreated());
			pReq.setDescription(poolRequest.getDescription());
			pReq.setId(poolRequest.getId());
			pReq.setStatus(poolRequest.getStatus());
			pReq.setUpdated(poolRequest.getUpdated());
			Pool pool = new Pool();
			Pool poolDetails = dao.getPoolDetails(poolRequest.getPool().getId());
			 pool.setId(poolDetails.getId());
			 pool.setCar(poolDetails.getCar());
			 pool.setComments(poolDetails.getComments());
			 pool.setCostPerMonth(poolDetails.getCostPerMonth());
			 pool.setDestinationAddress(poolDetails.getDestinationAddress());
			 pool.setHostUserId(poolDetails.getHostUserId());
			 pool.setIs_active(poolDetails.isIs_active());
			 pool.setIsAvailable(poolDetails.getIsAvailable());
			 pool.setMax_members(poolDetails.getMax_members());
			 pool.setModifyDate(poolDetails.getModifyDate());
			 pool.setNumberOfMembers(poolDetails.getNumberOfMembers());
			 pool.setOfficeTime_drop(poolDetails.getOfficeTime_drop());
			 pool.setOfficeTime_pickup(poolDetails.getOfficeTime_pickup());
			 pool.setParticipants(poolDetails.getParticipants());
			 pool.setSourceAddress(poolDetails.getSourceAddress());
			 pool.setStartTime_drop(poolDetails.getStartTime_drop());
			 pool.setStartTime_pickup(poolDetails.getStartTime_pickup());
			pReq.setPool(pool);
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


}
