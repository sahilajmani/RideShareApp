package dao;

import java.util.List;

import pojos.UserMapping;

public class testQuery {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DaoImpl db=new DaoImpl();
	//	List<UserMapping> userMapping=db.findMatchedUser("user7");
	//   db.persistUserMatch(userMapping);
		db.matchedPool("user1");
	//System.out.println("DONE2");
	}

}
