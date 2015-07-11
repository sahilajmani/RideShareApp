package rest;


import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import pojos.User;


@Path("/profile")
public class ProfileService {

	@POST
	@Path("updateProfile")
	@Produces(MediaType.TEXT_PLAIN)
	public String updateUserProfile(@FormParam("userId") int userId, @FormParam("userName") int userName, @FormParam("userEmail") int userEmail){
		//insert or update using hibernate
		return "Updated";
	}	
	@GET
	@Path("userDetails")
	@Produces(MediaType.APPLICATION_JSON)
	public User fetchUserDetails(@QueryParam("userId") int userId){
		User  user = new User();
		return user;
	}
	
	
}
