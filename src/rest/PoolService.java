package rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import dao.DaoI;
import pojos.Pool;
import utility.RideSharingUtil;

@Path("/poolservice")
public class PoolService {
	DaoI dao = RideSharingUtil.getDaoInstance();
	
	@POST
	@Path("getpoolservice")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Pool getPoolService(String poolId){
	Pool pool = new Pool();
	pool=dao.getPoolDetails(poolId);	
	return pool;
	}
}
