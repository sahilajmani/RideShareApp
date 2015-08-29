package rest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.logging.Logger;

import javax.transaction.SystemException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import pojos.RestServiceResponse;
import pojos.UpdateUserVO;
import pojos.User;
import utility.DistanceBwPlaces;
import utility.RideSharingUtil;
import dao.DaoI;

@Path("/profile")
public class ProfileService {
	DaoI dao = RideSharingUtil.getDaoInstance();
	Logger logger = Logger.getLogger("debug");

	@POST
	@Path("insertUserProfile")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RestServiceResponse insertUserProfile(User user)
			throws SystemException {
		RestServiceResponse serviceResponse = new RestServiceResponse();
		try {
			double distance = DistanceBwPlaces.getDistanceandDuration(user
					.getHomeAddress().getLattitude(), user.getHomeAddress()
					.getLongitude(), user.getOfficeAddress().getLattitude(),
					user.getOfficeAddress().getLongitude());
			logger.info("distance stored for user : " + distance);
			user.setDistance((float) distance);
			if (dao.insertUser(user)) {
				serviceResponse.setResponse(true);
				logger.info("User["+ user.getId() +"] Inserted");
			} else {
				serviceResponse.setResponse(false);
				logger.info("Failed to insert User["+ user.getId() +"]");
			}
		} catch (MalformedURLException e) {
			logger.info(e.getMessage());
			serviceResponse.setResponse(false);
		} catch (ProtocolException e) {
			logger.info(e.getMessage());
			serviceResponse.setResponse(false);
		} catch (IOException e) {
			logger.info(e.getMessage());
			serviceResponse.setResponse(false);
		} catch(Exception e){
			logger.info(e.getMessage());
			serviceResponse.setResponse(false);			
		}
		return serviceResponse;
	}

	@POST
	@Path("updateUserProfile")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RestServiceResponse updateUserProfile(UpdateUserVO updateUserVO)
			throws SystemException {
		RestServiceResponse serviceResponse = new RestServiceResponse();
		try {
			if (updateUserVO.isChangeAddress()) {
				double distance = DistanceBwPlaces.getDistanceandDuration(
						updateUserVO.getUser().getHomeAddress().getLattitude(),
						updateUserVO.getUser().getHomeAddress().getLongitude(),
						updateUserVO.getUser().getOfficeAddress()
								.getLattitude(), updateUserVO.getUser()
								.getOfficeAddress().getLongitude());
				updateUserVO.getUser().setDistance((float) distance);
			}
			if (dao.updateUser(updateUserVO.getUser(),
					updateUserVO.isChangeAddress())) {
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
			return dao.getUserDetails(user.getId());
	}

}
