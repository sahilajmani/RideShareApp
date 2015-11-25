package rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import utility.RideSharingUtil;
import dao.AuthenticationDaoI;
import dao.AuthenticationDaoImpl;

@Path("/authentication")
public class AuthenticationEndpoint {
AuthenticationDaoI authenticationDaoImpl = RideSharingUtil.getAuthenticationDaoInstance();
	
    @POST
    @Path("authenticateuser")
    @Produces("application/json")
    @Consumes("application/x-www-form-urlencoded")
    public Response authenticateUser(@FormParam("username") String username, 
                                     @FormParam("password") String password) {

        try {

            // Authenticate the user using the credentials provided
            authenticate(username, password);
            
            // Issue a token for the user
            String token = issueToken(username);

            // Return the token on the response
            return Response.ok(token).build();

        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }      
    }

    private void authenticate(String username, String password) throws Exception {
        // Authenticate against a database, LDAP, file or whatever
        // Throw an Exception if the credentials are invalid
    	if(!authenticationDaoImpl.authenticate(username,password)){
    		throw new Exception("Username and Password Authentication Error");
    	}
    }

    private String issueToken(String username) throws Exception {
    	Long token = (long) (100000000 + Math.random() * 899999999);
    	try {
			authenticationDaoImpl.insertToken(username,token);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error while inserting token");
		}
		return token.toString();
        // Issue a token (can be a random String persisted to a database or a JWT token)
        // The issued token must be associated to a user
        // Return the issued token
    }
}