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
		double rangeLatLong = 0.02;
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
							System.out.println("live within acceptabledistance of"+ acceptDist);
							UserMapping userMatchRow=new UserMapping();
							userMatchRow.setUserA(currentUser);
							userMatchRow.setUserB(user);

							double homeToHome=DistanceBwPlaces.getDistanceandDuration(
									user.getHomeAddress().getLattitude(), user.getHomeAddress().getLongitude(),
									currentUser.getHomeAddress().getLattitude(),
									currentUser.getHomeAddress().getLongitude());
							userMatchRow.setDistance((float) homeToHome);
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
							userMatchRow.setDistance((float) (homeToHome + currentUserDistance
									+ officetoOffice-user
									.getDistance()));
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
					double setDistance=0.0;
					
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
					UserMapping userMatchRow=new UserMapping();
					userMatchRow.setUserA(currentUser);
					userMatchRow.setUserB(user);
					userMatchRow.setDistance((float) setDistance);
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
							userMatchRow.setDistance((float) (homeToHome + user.getDistance()
									+ officetoOffice-currentUser
									.getDistance()));
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
								userMatchRow.setDistance((float) (homeToHome + currentUserDistance
										+ officetoOffice - user.getDistance()));
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
						userMatchRow.setDistance((float) (homeToHome + user.getDistance() + officetoOffice - currentUser
								.getDistance()));
						resultUsers.add(userMatchRow);
						continue;
					}

				}

			}

		}

		return resultUsers;
	}

	
}
