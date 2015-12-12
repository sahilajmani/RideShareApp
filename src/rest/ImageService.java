package rest;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.multipart.FormDataParam;

import utility.RideSharingUtil;
import dao.DaoI;
import dao.ImageDao;

@Path("/image")
public class ImageService {

	ImageDao dao = RideSharingUtil.getImageDaoInstance();
	
	@POST
	@Path("upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean uploadImage(@FormDataParam("file")InputStream uploadImage,
			@FormDataParam("userId")String userId, @FormDataParam("desciption")String description,
			@FormDataParam("imageType")String imageType)
			{
		
		if(uploadImage!= null)
	return dao.uploadImage(userId, uploadImage, imageType, description);
	else 
		{
		return false;
		}
}
	
	@Path("download")
	@GET
	//@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response downloadImage(@QueryParam("userId")String userId){
		byte[] img64=dao.downloadImage(userId);
		return Response.ok(img64).build();
	}
}
