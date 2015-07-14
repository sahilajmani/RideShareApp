package rest;


import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import dao.DaoI;
import pojos.User;
import utility.RideSharingUtil;


@Path("/profile")
public class ProfileService {
	DaoI dao = RideSharingUtil.getDaoInstance();
	
	@POST
	@Path("insertUserProfile")
	@Produces(MediaType.TEXT_PLAIN)
	public String insertUserProfile(@FormParam("name") String name, @FormParam("userEmail") String userEmail){
		User user = new User();
		user.setName(name);
		user.setEmail(userEmail);
		if(dao.insertUser(user)){
		return "Insert Successful";
		}else{
			return "Insert Failed";			
		}
	}
	
	@POST
	@Path("updateUserProfile")
	@Produces(MediaType.TEXT_PLAIN)
	public String updateUserProfile(@FormParam("id") String id, @FormParam("name") String name, @FormParam("userEmail") String userEmail){
		User user = new User();
		user.setId(id);
		user.setName(name);
		user.setEmail(userEmail);
		if(dao.updateUser(user)){
		return "Update Successful";
		}else{
			return "Update Failed";
		}
	}	
	@GET
	@Path("getUserDetails")
	@Produces(MediaType.APPLICATION_JSON)
	public User fetchUserDetails(@QueryParam("userId") String userId){
		return dao.getUserDetails(userId);
	}
	
	
}
