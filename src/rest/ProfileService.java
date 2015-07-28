package rest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import pojos.RestServiceResponse;
import pojos.User;
import utility.DistanceBwPlaces;
import utility.RideSharingUtil;
import dao.DaoI;

@Path("/profile")
public class ProfileService {
	DaoI dao = RideSharingUtil.getDaoInstance();

	@POST
	@Path("insertUpdateUserProfile")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RestServiceResponse insertUpdateUserProfile(User user) {
		RestServiceResponse serviceResponse = new RestServiceResponse();
		try {
			double distance = DistanceBwPlaces.getDistanceandDuration(user
					.getHomeAddress().getLattitude(), user.getHomeAddress()
					.getLongitude(), user.getOfficeAddress().getLattitude(), user
					.getOfficeAddress().getLongitude());
			user.setDistance((float)distance);
			if (dao.insertUpdateUser(user)) {
				serviceResponse.setResponse(true);
			} else {
				serviceResponse.setResponse(false);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			serviceResponse.setResponse(false);
		} catch (ProtocolException e) {
			e.printStackTrace();
			serviceResponse.setResponse(false);
		} catch (IOException e) {
			e.printStackTrace();
			serviceResponse.setResponse(false);
		}		
		return serviceResponse;
	}

	@POST
	@Path("getUserDetails")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User fetchUserDetails(User user) {
		if (null != dao.getUserDetails(user.getId())) {
			return dao.getUserDetails(user.getId());
		}
		return null;
	}

}
