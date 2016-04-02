package rest;

import java.io.InputStream;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import pojos.Pool;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import utility.GlobalConstants;
import utility.RideSharingUtil;
import dao.DaoI;
import dao.ImageDao;

@Path("/image")
public class ImageService {

	ImageDao dao = RideSharingUtil.getImageDaoInstance();
	Logger logger = Logger.getLogger("debug");
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

	
	@POST
	@Path("uploadnew")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean uploadImageNew(@FormDataParam("image")InputStream uploadImage,
			@FormDataParam("image")FormDataContentDisposition fileDetail)
			{
		logger.info("done1");
		System.out.println(uploadImage.toString());
		
		logger.info("done2");
		System.out.println(fileDetail.getFileName()+"  "+fileDetail.getType()+"  ");
	System.out.println("yes");
//	return dao.uploadImage(fileDetail.getFileName(), uploadImage, fileDetail.get, description);	
	return true;
}
	
	
	@POST
	@Path("uploadjson")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean getPoolService(byte[] url,String userId) throws Exception {
		if(url==null)
			return false;
		System.out.println("I got "+url);
		return true;
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
