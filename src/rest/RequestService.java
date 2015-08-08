package rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import dao.DaoI;
import pojos.PoolRequest;
import pojos.User;
import utility.RideSharingUtil;

@Path("/requests")
public class RequestService {
	DaoI dao = RideSharingUtil.getDaoInstance();

	@POST
	@Path("incomingrequests")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<PoolRequest> getIncomingRequest(User user) {
		
		if(user==null || user.getId()==null)
			return null;
		
		String userId = user.getId();
		return dao.getIncomingPoolRequests(userId);

	}
	
	
	@POST
	@Path("outgoingrequests")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<PoolRequest> getOutgoingRequest(User user) {

		if(user==null || user.getId()==null)
			return null;
		String userId = user.getId();
		return dao.getOutcomingPoolRequests(userId);

	}


}
