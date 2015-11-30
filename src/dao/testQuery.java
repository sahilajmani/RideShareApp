package dao;

import org.hibernate.Session;

import pojos.MailNotifierThread;
import pojos.User;
import utility.RideSharingUtil;
import vo.UserIdPoolIdVO;

public class testQuery {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DaoImpl db=new DaoImpl();
		/*
		Session session = RideSharingUtil.getSessionFactoryInstance().openSession();
		User user = db.getUserDetails("123");
		session.close();*/
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
	//	User user=db.getUserDetailsByEmail("rohan_h255@hotmail.com");
		//System.out.println(user.getName());
//	System.out.println(db.getOutgoingPoolRequests("user2").size());
	//	System.out.println(db.getmatchedPool("user2").size());
	//	System.out.println(db.leavePool("user6","user4"));
	//	db.matchTest("745be031-5167-11e5-a9c3-02b93fe38caf");
	//	List<MatchedPoolsVO> map =db.getmatchedPool("7d3697e0-59f2-11e5-9624-02b93fe38caf");
	//	System.out.println(db.matchTest("fef5eb9a-59f3-11e5-9624-02b93fe38caf"));
//	System.out.println(db.updatePoolRequest("df7bc0fe-9446-11e5-b8ad-027a1dc8a973",1));
//	new MailNotifierThread("dsfsd", "dsfs", "dsfsd").start();
//	new MailNotifierThread("dsfsd", "dsfs", "dsfsd").start();

//	new MailNotifierThread("dsfsd", "dsfs", "dsfsd").start();

//List<Transactions> t =	db.getUserPoolRecord("17e1794b-5a20-11e5-869d-02b93fe38caf");
//		System.out.println(db.leavePool("0c3c2ff1-817e-11e5-be8a-027a1dc8a973", "0adea238-71da-11e5-899a-027a1dc8a973"));
	
		/*UserIdPoolIdVO userIdPoolIdVO = new UserIdPoolIdVO();
		userIdPoolIdVO.setPoolId("2d7f7218-95f9-11e5-b8ad-027a1dc8a973");
		userIdPoolIdVO.setUserId("74dda80c-95fb-11e5-b8ad-027a1dc8a973");
		userIdPoolIdVO.setStatus(2);
	System.out.println(db.joinPoolRequest(userIdPoolIdVO, 0));
*/
	//System.out.println(db.updatePoolRequest("cb1ebec3-9602-11e5-b8ad-027a1dc8a973", 1));
		System.out.println(db.leavePool("2f88d39d-9615-11e5-b8ad-027a1dc8a973", "f4de0c33-9614-11e5-b8ad-027a1dc8a973"));
	}

}
