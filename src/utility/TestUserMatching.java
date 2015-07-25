package utility;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;

import pojos.Address;
import pojos.User;
import pojos.UserMapping;


public class TestUserMatching {
	
	public static void main(String[] args) {
	Address add1=new Address();
	add1.setId("1");
	add1.setLattitude(28.7010952);
	add1.setLongitude(77.1612418);
	Address add2=new Address();
	add2.setId("2");
	add2.setLattitude(28.426852);
	add2.setLongitude(77.031367);
	Address add3=new Address();
	add3.setId("3");
	add3.setLattitude(28.673751);
	add3.setLongitude(77.127338);
	Address add4=new Address();
	add4.setId("4");
	add4.setLattitude(28.495781);
	add4.setLongitude(77.08826);
	Address add5=new Address();
	add5.setId("5");
	add5.setLattitude(28.5951154);
	add5.setLongitude(77.1631865);
	Address add6=new Address();
	add6.setId("6");
	add6.setLattitude(28.5707966);
	add6.setLongitude(77.3261091);
	Address add7=new Address();
	add7.setId("7");
	add7.setLattitude(28.7069921);
	add7.setLongitude(77.1805385);
	Address add8=new Address();
	add8.setId("8");
	add8.setLattitude(28.6960432);
	add8.setLongitude(77.1526005);
	Address add9=new Address();
	add9.setId("8");
	add9.setLattitude(28.426852);
	add9.setLongitude(77.081367);
	
	User user1=new User();
	user1.setId("1001");
	user1.setHomeAddress(add1);
	user1.setOfficeAddress(add2);
	user1.setHasCar(false);
	user1.setDistance((float) 41.5);
	User user2=new User();
	user2.setId("1002");
	user2.setHasCar(false);
	user2.setHomeAddress(add3);
	user2.setOfficeAddress(add2);
	user2.setDistance((float) 36.9);
	User user3=new User();
	user3.setId("1003");
	user3.setHomeAddress(add3);
	user3.setOfficeAddress(add4);
	user3.setDistance((float) 28);
	user3.setHasCar(false);
	User user4=new User();
	user4.setId("1004");
	user4.setHomeAddress(add3);
	user4.setOfficeAddress(add5);
	user4.setDistance((float) 13.8);
	user4.setHasCar(false);
	User user5=new User();
	user5.setId("1005");
	user5.setHomeAddress(add3);
	user5.setOfficeAddress(add6);
	user5.setDistance((float) 34.0);
	user5.setHasCar(false);
	User user6=new User();
	user6.setId("1006");
	user6.setHomeAddress(add7);
	user6.setOfficeAddress(add2);
	user6.setDistance((float) 43.0);
	user6.setHasCar(false);
	User user7=new User();
	user7.setId("1007");
	user7.setHomeAddress(add8);
	user7.setOfficeAddress(add9);
	user7.setDistance((float) 41.4);
	user7.setHasCar(false);
	
	
//	User user1 = new User(1001, "userName", true, 41.5, 28.7010952,77.1612418, 28.426852, 77.031367);
//	User user2 = new User(1002, "userName", false, 36.9,  28.673751, 77.127338, 28.426852, 77.031367);//pbagh-sec21 ggn
//	User user3 = new User(1003, "userName", false, 45,  40.0, 78.0, 40.01, 78.01);
//	User user4 = new User(1004, "userName", false, 28,  28.673751, 77.127338, 28.495781,77.08826); //pdagh-cyberhub
//	User user5 = new User(1005, "userName", false, 13.8,  28.673751, 77.127338, 28.5951154,77.1631865);//pbag-dhaula kaun
//	User user6 = new User(1006, "userName", false, 34,  28.673751, 77.127338, 28.5707966,77.3261091);//pbagh-noida
//	User user7 = new User(1007, "userName", false, 43,  28.7069921,77.1805385, 28.426852, 77.031367);//azadpur--ggn
//	User user8 = new User(1008, "userName", false, 41.4, 28.6960432,77.1526005, 28.426852, 77.081367);//nsp-shushant lok
	List<User> userList=new ArrayList<User>();
//	userList.add(user2);
//	userList.add(user3);
	userList.add(user4);
	userList.add(user5);
//	userList.add(user6);
//	userList.add(user1);
//	userList.add(user7);
	UserMatching userMatch=new UserMatching();
try {
	//System.out.println(
			for(UserMapping userMap:userMatch.getMatchedUsers(userList,user5)){
				System.out.println(userMap.getUserB().getId() +"  "+ userMap.getDistance());
			}
} catch (MalformedURLException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
} catch (ProtocolException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
	
	
}



}
