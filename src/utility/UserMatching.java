package utility;



import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;

import pojos.User;
import pojos.UserMapping;



public class UserMatching {
	
	public  List<UserMapping> getMatchedUsers(List<User> users, User currentUser)
			throws MalformedURLException, ProtocolException, IOException {
		double rangeLatLong = 0.15;
		double acceptDist = 5.0;
		double currentUserDistance=currentUser.getDistance();
		List<UserMapping> resultUsers = new ArrayList<UserMapping>();
		if (!currentUser.isHasCar()) {// current user has no car
			for (User user : users) {
				if (user.isHasCar()) {// other person has car
					if (currentUserDistance > user.getDistance()) {// but
																			// i
																			// travel
																			// more
						// we can send homelat/long of both user to find if they
						// live within acceptabledistance
						if (((user.getHomeAddress().getLattitude() - rangeLatLong) < currentUser
								.getHomeAddress().getLattitude())
								&& ((user.getHomeAddress().getLattitude() + rangeLatLong) > currentUser
										.getHomeAddress().getLattitude())
								&& ((user.getHomeAddress().getLongitude() - rangeLatLong) < currentUser
										.getHomeAddress().getLongitude())
								&& ((user.getHomeAddress().getLongitude() + rangeLatLong) > currentUser
										.getHomeAddress().getLongitude())
								&& ((user.getOfficeAddress().getLattitude() - rangeLatLong) < currentUser
										.getOfficeAddress().getLattitude())
								&& ((user.getOfficeAddress().getLattitude() + rangeLatLong) > currentUser
										.getOfficeAddress().getLattitude())
								&& ((user.getOfficeAddress().getLongitude() - rangeLatLong) < currentUser
										.getOfficeAddress().getLongitude())
								&& ((user.getOfficeAddress().getLongitude() + rangeLatLong) > currentUser
										.getOfficeAddress().getLongitude())) {
						//	System.out.println("live within acceptabledistance of"+ acceptDist);
							

							double homeToHome=DistanceBwPlaces.getDistanceandDuration(
									user.getHomeAddress().getLattitude(), user.getHomeAddress().getLongitude(),
									currentUser.getHomeAddress().getLattitude(),
									currentUser.getHomeAddress().getLongitude());
							
							if(homeToHome >acceptDist )
								continue;
							UserMapping userMatchRow=new UserMapping();
							userMatchRow.setUserA(currentUser);
							userMatchRow.setUserB(user);
							userMatchRow.setDistance((float) Math.abs(homeToHome));
							resultUsers.add(userMatchRow);
							continue;
						} else {
							continue;// reject me
						}
					} else { // other person travel more

						double homeToHome=DistanceBwPlaces.getDistanceandDuration(
								user.getHomeAddress().getLattitude(), user.getHomeAddress().getLongitude(),
								currentUser.getHomeAddress().getLattitude(),
								currentUser.getHomeAddress().getLongitude());
					
						double officetoOffice = DistanceBwPlaces
								.getDistanceandDuration(currentUser.getOfficeAddress().getLattitude(),
										currentUser.getOfficeAddress().getLongitude(),
										user.getOfficeAddress().getLattitude(), user.getOfficeAddress().getLongitude());
						
						if (homeToHome + currentUserDistance
								+ officetoOffice < user
								.getDistance() + acceptDist)// feasaible
						// for
						// other
						{
							System.out.println("Extra distance "+(homeToHome + currentUserDistance
									+ officetoOffice-user
									.getDistance()));
							UserMapping userMatchRow=new UserMapping();
							userMatchRow.setUserA(currentUser);
							userMatchRow.setUserB(user);
							float dist=(float)( (homeToHome + currentUserDistance
									+ officetoOffice-user
									.getDistance()));
							if(dist<0)
								dist=2.2f;
							userMatchRow.setDistance(dist);
							resultUsers.add(userMatchRow);
							continue;
						} else {
							continue;// else reject
						}
					}
				} else {// the other person has no car

					double homeToHomme = DistanceBwPlaces
							.getDistanceandDuration(user.getHomeAddress().getLattitude(),
									user.getHomeAddress().getLongitude(),
									currentUser.getHomeAddress().getLattitude(),
									currentUser.getHomeAddress().getLongitude());
					double officetoOffice = DistanceBwPlaces
							.getDistanceandDuration(currentUser.getOfficeAddress().getLattitude(),
									currentUser.getOfficeAddress().getLongitude(),
									user.getOfficeAddress().getLattitude(), user.getOfficeAddress().getLongitude());
					double hisHomeToMyOffice = DistanceBwPlaces
							.getDistanceandDuration(user.getHomeAddress().getLattitude(),
									user.getHomeAddress().getLongitude(),
									currentUser.getOfficeAddress().getLattitude(),
									currentUser.getOfficeAddress().getLongitude());
					double myHomeToHisOffice = DistanceBwPlaces
							.getDistanceandDuration(currentUser.getHomeAddress().getLattitude(),
									currentUser.getHomeAddress().getLongitude(),
									user.getOfficeAddress().getLattitude(), user.getOfficeAddress().getLongitude());
					double setDistance=0.0000001;
					
					if (homeToHomme + user.getDistance() + officetoOffice < currentUser
							.getDistance() + acceptDist
							)
					{
						setDistance=homeToHomme + user.getDistance() + officetoOffice - currentUser
								.getDistance();
					}
					else if (homeToHomme + hisHomeToMyOffice < currentUser
									.getDistance() + acceptDist && hisHomeToMyOffice
									+ officetoOffice < user.getDistance()
									+ acceptDist)
					{
						if((homeToHomme + hisHomeToMyOffice - currentUser
									.getDistance()) >( hisHomeToMyOffice
									+ officetoOffice - user.getDistance()))
					setDistance=homeToHomme + hisHomeToMyOffice - currentUser.getDistance();
						else
							setDistance=hisHomeToMyOffice
									+ officetoOffice - user.getDistance();
						}
					else if (homeToHomme + myHomeToHisOffice < user
									.getDistance() + acceptDist && myHomeToHisOffice
									+ officetoOffice < currentUser
									.getDistance() + acceptDist)
					{
					if((homeToHomme + myHomeToHisOffice - user.getDistance())>(myHomeToHisOffice
							+ officetoOffice - currentUser
							.getDistance()))
						setDistance=homeToHomme + myHomeToHisOffice - user.getDistance();
					else
						setDistance=myHomeToHisOffice
								+ officetoOffice - currentUser
								.getDistance();
						
					}else if(homeToHomme + currentUserDistance
									+ officetoOffice < user.getDistance()
									+ acceptDist) {
						setDistance=user.getDistance()
								+ acceptDist-homeToHomme + currentUserDistance
								+ officetoOffice ;						
					}
					else
					{continue;}
					if(setDistance>acceptDist)
						continue;
					if(setDistance<0)
						setDistance=2.2;
					UserMapping userMatchRow=new UserMapping();
					userMatchRow.setUserA(currentUser);
					userMatchRow.setUserB(user);
					userMatchRow.setDistance((float)(setDistance));
					resultUsers.add(userMatchRow);
						continue;
					}
				}
			}
		else { // current user has car

			for (User user : users) {

				double homeToHome = DistanceBwPlaces.getDistanceandDuration(
						user.getHomeAddress().getLattitude(), user.getHomeAddress().getLongitude(),
						currentUser.getHomeAddress().getLattitude(), currentUser.getHomeAddress().getLongitude());
				double officetoOffice = DistanceBwPlaces
						.getDistanceandDuration(currentUser.getOfficeAddress().getLattitude(),
								currentUser.getOfficeAddress().getLongitude(),
								user.getOfficeAddress().getLattitude(), user.getOfficeAddress().getLongitude());
				if (user.isHasCar()) {
					if (currentUserDistance > user.getDistance())// i
																		// travel
																		// more
					{
						if (homeToHome + user.getDistance() + officetoOffice < currentUser
								.getDistance() + acceptDist) {
							System.out.println("extra distance by me "+(homeToHome + user.getDistance() + officetoOffice-currentUser
								.getDistance()));
							UserMapping userMatchRow=new UserMapping();
							userMatchRow.setUserA(currentUser);
							userMatchRow.setUserB(user);
							float dis=(float) ((homeToHome + user.getDistance()
									+ officetoOffice-currentUser
									.getDistance()));
							if(dis<0)
								dis=2.2f;
							userMatchRow.setDistance(dis);
							resultUsers.add(userMatchRow);
							continue;
						}
					} else// user travel more
					{
						if (homeToHome + currentUserDistance
								+ officetoOffice < user.getDistance()
								+ acceptDist) {
							{
								System.out.println("extra distance by other person "+(homeToHome + currentUserDistance
										+ officetoOffice - user.getDistance()
										));
								UserMapping userMatchRow=new UserMapping();
								userMatchRow.setUserA(currentUser);
								userMatchRow.setUserB(user);
								float dis = (float) Math.abs(homeToHome + currentUserDistance
										+ officetoOffice - user.getDistance());
								if(dis<0)
									dis=2.2f;
								userMatchRow.setDistance(dis);
								resultUsers.add(userMatchRow);
								continue;
							}

						}

					}

				} else// i have the car--i need to see if it is feasable for me
						// to take the other prson.
				{
					if (homeToHome + user.getDistance() + officetoOffice < currentUser
							.getDistance() + acceptDist) {
						System.out.println("extra distance by me "+(homeToHome + user.getDistance() + officetoOffice - currentUser
								.getDistance()
								));
						UserMapping userMatchRow=new UserMapping();
						userMatchRow.setUserA(currentUser);
						userMatchRow.setUserB(user);
						float dis = (float)((homeToHome + user.getDistance() + officetoOffice - currentUser
								.getDistance()));
						if(dis<0)
							dis=2.2f;
						userMatchRow.setDistance(dis);
						resultUsers.add(userMatchRow);
					
					}

				}

			}

		}

		return resultUsers;
	}
	
	public  List<UserMapping> checkMatchedUsersForUser(List<User> users, User comparedUser)
			throws MalformedURLException, ProtocolException, IOException {
		double rangeLatLong = 0.15;
		double acceptDist = 5.0;
		double comparedUserDistance=comparedUser.getDistance();
		List<UserMapping> resultUsers = new ArrayList<UserMapping>();
		System.out.println("Compared User-  "+comparedUser.getId());
		// current user has no car
			for (User user : users) {
				System.out.println(user.getId()+"-- user Id");
				if (!user.isHasCar()) {//i have no car   user A is user..User B is Compare
				if(comparedUser.isHasCar()) {// other person has car
					if (comparedUserDistance < user.getDistance()) {// but
																			// i
																			// travel
																			// more
						// we can send homelat/long of both user to find if they
						// live within acceptabledistance
						if (((user.getHomeAddress().getLattitude() - rangeLatLong) < comparedUser
								.getHomeAddress().getLattitude())
								&& ((user.getHomeAddress().getLattitude() + rangeLatLong) > comparedUser
										.getHomeAddress().getLattitude())
								&& ((user.getHomeAddress().getLongitude() - rangeLatLong) < comparedUser
										.getHomeAddress().getLongitude())
								&& ((user.getHomeAddress().getLongitude() + rangeLatLong) > comparedUser
										.getHomeAddress().getLongitude())
								&& ((user.getOfficeAddress().getLattitude() - rangeLatLong) < comparedUser
										.getOfficeAddress().getLattitude())
								&& ((user.getOfficeAddress().getLattitude() + rangeLatLong) > comparedUser
										.getOfficeAddress().getLattitude())
								&& ((user.getOfficeAddress().getLongitude() - rangeLatLong) < comparedUser
										.getOfficeAddress().getLongitude())
								&& ((user.getOfficeAddress().getLongitude() + rangeLatLong) > comparedUser
										.getOfficeAddress().getLongitude())) {
						//	System.out.println("live within acceptabledistance of"+ acceptDist);
							

							double homeToHome=DistanceBwPlaces.getDistanceandDuration(
									user.getHomeAddress().getLattitude(), user.getHomeAddress().getLongitude(),
									comparedUser.getHomeAddress().getLattitude(),
									comparedUser.getHomeAddress().getLongitude());
						
							if(homeToHome>acceptDist)
								continue;
							
							UserMapping userMatchRow=new UserMapping();
							userMatchRow.setUserA(user);
							userMatchRow.setUserB(comparedUser);

							userMatchRow.setDistance((float) Math.abs(homeToHome));
							
							resultUsers.add(userMatchRow);
							continue;
						} else {
							continue;// reject me
						}
					} else { // other person travel more

						double homeToHome=DistanceBwPlaces.getDistanceandDuration(
								user.getHomeAddress().getLattitude(), user.getHomeAddress().getLongitude(),
								comparedUser.getHomeAddress().getLattitude(),
								comparedUser.getHomeAddress().getLongitude());
					
						double officetoOffice = DistanceBwPlaces
								.getDistanceandDuration(comparedUser.getOfficeAddress().getLattitude(),
										comparedUser.getOfficeAddress().getLongitude(),
										user.getOfficeAddress().getLattitude(), user.getOfficeAddress().getLongitude());
						
						if (homeToHome + user.getDistance()
								+ officetoOffice < comparedUserDistance
								 + acceptDist)// feasaible
						// for
						// other
						{
							System.out.println("Extra distance "+(homeToHome + comparedUserDistance
									+ officetoOffice-user
									.getDistance()));
							UserMapping userMatchRow=new UserMapping();
							userMatchRow.setUserA(user);
							userMatchRow.setUserB(comparedUser);
							userMatchRow.setDistance((float) Math.abs( (homeToHome + user.getDistance()
									+ officetoOffice-comparedUserDistance)));
							resultUsers.add(userMatchRow);
							continue;
						} else {
							continue;// else reject
						}
					}
				}
				 else {// the other person has no car

					double homeToHomme = DistanceBwPlaces
							.getDistanceandDuration(user.getHomeAddress().getLattitude(),
									user.getHomeAddress().getLongitude(),
									comparedUser.getHomeAddress().getLattitude(),
									comparedUser.getHomeAddress().getLongitude());
					double officetoOffice = DistanceBwPlaces
							.getDistanceandDuration(comparedUser.getOfficeAddress().getLattitude(),
									comparedUser.getOfficeAddress().getLongitude(),
									user.getOfficeAddress().getLattitude(), user.getOfficeAddress().getLongitude());
					double hisHomeToMyOffice = DistanceBwPlaces
							.getDistanceandDuration(comparedUser.getHomeAddress().getLattitude(),
									comparedUser.getHomeAddress().getLongitude(),
									user.getOfficeAddress().getLattitude(),
									user.getOfficeAddress().getLongitude());
					double myHomeToHisOffice = DistanceBwPlaces
							.getDistanceandDuration(user.getHomeAddress().getLattitude(),
									user.getHomeAddress().getLongitude(),
									comparedUser.getOfficeAddress().getLattitude(), comparedUser.getOfficeAddress().getLongitude());
					double setDistance=0.0000001;
					
					if (homeToHomme + comparedUserDistance + officetoOffice < user
							.getDistance() + acceptDist
							)
					{
						setDistance=homeToHomme + comparedUserDistance + officetoOffice - user
								.getDistance();
					}
					else if (homeToHomme + hisHomeToMyOffice < user.getDistance() + acceptDist && hisHomeToMyOffice
									+ officetoOffice < comparedUserDistance
									+ acceptDist)
					{
						if((homeToHomme + hisHomeToMyOffice - user
									.getDistance()) >( hisHomeToMyOffice
									+ officetoOffice - comparedUserDistance))
					setDistance=homeToHomme + hisHomeToMyOffice - comparedUserDistance;
						else
							setDistance=hisHomeToMyOffice
									+ officetoOffice - comparedUserDistance;
						}
					else if (homeToHomme + myHomeToHisOffice < user
									.getDistance() + acceptDist && myHomeToHisOffice
									+ officetoOffice < comparedUserDistance + acceptDist)
					{
					if((homeToHomme + myHomeToHisOffice - user.getDistance())>(myHomeToHisOffice
							+ officetoOffice - comparedUserDistance
							))
						setDistance=homeToHomme + myHomeToHisOffice - user.getDistance();
					else
						setDistance=myHomeToHisOffice
								+ officetoOffice - user
								.getDistance();
						
					}else if(homeToHomme + comparedUserDistance
									+ officetoOffice < user.getDistance()
									+ acceptDist) {
						setDistance=user.getDistance()
								+ acceptDist-(homeToHomme + comparedUserDistance
								+ officetoOffice) ;						
					}
					else
					{continue;}
					if(setDistance>acceptDist)
						continue;
					UserMapping userMatchRow=new UserMapping();
					userMatchRow.setUserA(user);
					userMatchRow.setUserB(comparedUser);
					userMatchRow.setDistance((float) Math.abs(setDistance));
					resultUsers.add(userMatchRow);
						continue;
					}
				}		
			
		else { // current user has car

			

				double homeToHome = DistanceBwPlaces.getDistanceandDuration(
						comparedUser.getHomeAddress().getLattitude(), comparedUser.getHomeAddress().getLongitude(),
						user.getHomeAddress().getLattitude(), user.getHomeAddress().getLongitude());
				double officetoOffice = DistanceBwPlaces
						.getDistanceandDuration(comparedUser.getOfficeAddress().getLattitude(),
								comparedUser.getOfficeAddress().getLongitude(),
								user.getOfficeAddress().getLattitude(), user.getOfficeAddress().getLongitude());
				if (comparedUser.isHasCar()) {
					if (comparedUserDistance < user.getDistance())// i
																		// travel
																		// more
					{
						if (homeToHome +  comparedUserDistance + officetoOffice < user.getDistance()
								+ acceptDist) {
							System.out.println("extra distance by me "+(homeToHome + comparedUserDistance + officetoOffice-user
								.getDistance()));
							UserMapping userMatchRow=new UserMapping();
							userMatchRow.setUserA(user);
							userMatchRow.setUserB(comparedUser);
							userMatchRow.setDistance((float) Math.abs((homeToHome + comparedUserDistance
									+ officetoOffice-user
									.getDistance())));
							resultUsers.add(userMatchRow);
							continue;
						}
					} else// user travel more
					{
						if (homeToHome + user.getDistance()
								+ officetoOffice < comparedUserDistance
								+ acceptDist) {
							{
								System.out.println("extra distance by other person "+(homeToHome + user.getDistance()
										+ officetoOffice - comparedUserDistance
										));
								UserMapping userMatchRow=new UserMapping();
								userMatchRow.setUserA(user);
								userMatchRow.setUserB(comparedUser);
								userMatchRow.setDistance((float) Math.abs(homeToHome + user.getDistance()
										+ officetoOffice - comparedUserDistance
										));
								resultUsers.add(userMatchRow);
								continue;
							}

						}

					}

				} else// i have the car--i need to see if it is feasable for me
						// to take the other prson.
				{
					if (homeToHome + comparedUserDistance + officetoOffice < user
							.getDistance() + acceptDist) {
						System.out.println("extra distance by me "+(homeToHome + comparedUserDistance + officetoOffice - user
								.getDistance()
								));
						UserMapping userMatchRow=new UserMapping();
						userMatchRow.setUserA(user);
						userMatchRow.setUserB(comparedUser);
						userMatchRow.setDistance((float) Math.abs((homeToHome + comparedUserDistance + officetoOffice - user
								.getDistance())));
						resultUsers.add(userMatchRow);
					
					}

				}

			}

		
			}
		return resultUsers;
		
	}


	
}
