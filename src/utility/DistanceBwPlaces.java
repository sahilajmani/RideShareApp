package utility;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.NumberFormat;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



public class DistanceBwPlaces {

	  
	  
			public static double getDistanceandDuration(double sourceLat,
					double sourceLon, double destLat, double destLon)
					throws MalformedURLException, IOException, ProtocolException {
				System.out.println("Google APi call for lat - "+sourceLat+"\t long "+sourceLon);
				System.out.println("Google APi call for dest lat - "+destLat+"\t long "+destLon);

				String originParams = sourceLat + "%20" + sourceLon;
				String destinationParams = destLat + "%20" + destLon;
if(sourceLat==destLat && sourceLon==destLon)
return 0.0;
				 URL url = new URL(
						"https://maps.googleapis.com/maps/api/distancematrix/json?origins="+ originParams +"&destinations="+destinationParams);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				String line=null, outputString = "",status="";

			String json=null;
			try{	BufferedReader reader = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
                   
            //  BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"utf-8"),8);
              StringBuilder sbuild = new StringBuilder();
              //String line = null;
              while ((line = reader.readLine()) != null) {
                  sbuild.append(line);
              }
           //   inputStream.close();
              json = sbuild.toString();               
          } catch(Exception e) {
          }


          //now parse
          JSONParser parser = new JSONParser();
          Object obj=null;
		try {
			obj = parser.parse(json);
		
          JSONObject jb = (JSONObject) obj;
//System.out.println(jb);
          //now read
          JSONArray jsonObject1 = (JSONArray) jb.get("rows");
          JSONObject jb1 = (JSONObject) jsonObject1.get(0);
          JSONArray jsonObject2 = (JSONArray) jb1.get("elements");
          JSONObject jb2 = (JSONObject) jsonObject2.get(0);
		
          status = (String) jb2.get("status");
          if(!status.equalsIgnoreCase("ok"))
        		return 11000;	  
          
          JSONObject jb3 = (JSONObject) jb2.get("distance");
          
           outputString = (String) jb3.get("text");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Google Call failed.");
			e.printStackTrace();
			return 100D;
		}
          String[] str;
  		boolean isDistanceKm=true;
  		if(outputString.contains("km"))
  		{
  		 str = outputString.split("km");
  		}else
  		{
  	    str = outputString.split("m");
  	    isDistanceKm=false;
  		}
  		String distance="";
  		distance = str[0].trim();
  	//	System.out.println(distance);
  		NumberFormat format = NumberFormat.getInstance();
  		Number number=null;
		try {
			number = format.parse(distance);
			distance=number.toString();
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  //		System.out.println(number.toString());

		if(distance==null)	
			return 11000;
		
			if(isDistanceKm)
				 return((Double.parseDouble(distance)));
			else
				return((Double.parseDouble(distance))/1000);	
			}
}