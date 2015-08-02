package dao;

import java.util.List;

import pojos.PoolRequest;
import pojos.User;
import pojos.UserMapping;

public class testQuery {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DaoImpl db=new DaoImpl();
	//	List<UserMapping> userMapping=db.findMatchedUser("user7");
	//   db.persistUserMatch(userMapping);
		//db.matchedPool("user1");
	//	db.matchedPool("user1");
		User user1=new User();
		//user1.setId(id);
//	System.out.println(db.getUserPoolRecord("user3").size());
//	List<PoolRequest> userPoolRequest =	db.getPoolRequests("user1");
//	System.out.println(userPoolRequest.size());
//	for(PoolRequest individual : userPoolRequest)
//	{
//		System.out.println(individual.getId());
//		System.out.println(individual.getStatus());
//		System.out.println(individual.getPool().getId());
//		System.out.println(individual.getUser().getId());
//		System.out.println(individual.getDescription());
//		
//	}
	
	//	System.out.println(db.updatePoolRequest("3",1));
		//System.out.println(db.leavePool("user6","user2"));
		System.out.println(db.leavePool("user2","user2"));
		
	}

}
