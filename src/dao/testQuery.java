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
	//	User user1=new User();
		//user1.setId(id);
//	System.out.println(db.getUserPoolRecord("user3").size());
	//List<PoolRequest> userPoolRequest =	db.getIncomingPoolRequests("user2");
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
//	db.leavePool("user6", "user2");
		//System.out.println(db.updatePoolRequest("acc615af-488c-11e5-b52e-02b93fe38caf",1));
		//System.out.println(db.leavePool("user6","user2"));
	//	System.out.println(db.leavePool("user2","user2"));
	//	System.out.println(db.getmatchedPool("user6").get(0).getDistance());
	//	System.out.println(db.joinPoolRequest("user6","user4",(float) 30.5));
	//	System.out.println(db.updatePoolRequest("3f39221d-4e32-11e5-a509-02b93fe38caf", 1));
		User user=db.getUserDetailsByEmail("rohan_h255@hotmail.com");
		System.out.println(user.getName());
//	System.out.println(db.getOutgoingPoolRequests("user2").size());
	//	System.out.println(db.getmatchedPool("user2").size());
	//	System.out.println(db.leavePool("user6","user4"));
	//	db.matchTest("745be031-5167-11e5-a9c3-02b93fe38caf");
	}

}
