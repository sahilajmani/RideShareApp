package utility;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;

import pojos.Address;

public class TestDistance {

	public static void main(String[] args) throws MalformedURLException, ProtocolException, IOException {
		// TODO Auto-generated method stub
	
double sourceLat=28.7010952;
double sourceLon=77.1612418;
double destLat=28.426852;
double destLon=77.031367;
int i=0;
for(i=0;i<1000;i++)
{
	
System.out.println("count" +i+"  "+DistanceBwPlaces.getDistanceandDuration(sourceLat, sourceLon, destLat, destLon));
sourceLat-=0.01;
sourceLon+=0.01;
destLat+=0.01;
destLon+=0.01;
}
	
	}

}
