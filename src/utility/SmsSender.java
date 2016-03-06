package utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.jboss.logging.Logger;


public class SmsSender {

	private static SmsSender sender = new SmsSender();
	private static String url = "https://control.msg91.com/api/sendhttp.php?";
	private static String authKey="106328A6w61fm37Do56d5b552";
	private static String senderId="RIDESY";
	private static String route="4";
	private Logger logger = Logger.getLogger("debug");
	private SmsSender(){
		
	}
	
	public static SmsSender getInstance(){
		return sender;
	}
	static {
	    disableSslVerification();
	}

	private static void disableSslVerification() {
	    try
	    {
	        // Create a trust manager that does not validate certificate chains
	        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
	            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	                return null;
	            }
	            public void checkClientTrusted(X509Certificate[] certs, String authType) {
	            }
	            public void checkServerTrusted(X509Certificate[] certs, String authType) {
	            }
	        }
	        };

	        // Install the all-trusting trust manager
	        SSLContext sc = SSLContext.getInstance("SSL");
	        sc.init(null, trustAllCerts, new java.security.SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

	        // Create all-trusting host name verifier
	        HostnameVerifier allHostsValid = new HostnameVerifier() {
	            public boolean verify(String hostname, SSLSession session) {
	                return true;
	            }
	        };

	        // Install the all-trusting host verifier
	        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    } catch (KeyManagementException e) {
	        e.printStackTrace();
	    }
	}
	public boolean sendSms(String number, String msg){
		 URL url;
		try {
			logger.debug("sending sms to - "+number);
			url = new URL(prepareSmsQueryString(number, msg));
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			int res = conn.getResponseCode();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
               
        //  BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"utf-8"),8);
          StringBuilder sbuild = new StringBuilder();
          //String line = null;
          String line;
          while ((line = reader.readLine()) != null) {
              sbuild.append(line);
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
			
		return false;
	}
	private String prepareSmsQueryString(String number,String msg){
		/*https://control.msg91.com/api/sendhttp.php?authkey=106328A6w61fm37Do56d5b552&
			mobiles=919899235576,919899519537&
			message=OTP%20is%204587&sender=RIDESY&route=4*/
		if(number.length() == 10){
			number = "91"+number;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(SmsSender.url);
		sb.append("authkey="+authKey);
		sb.append("&mobiles="+number);
		sb.append("&message="+URLEncoder.encode(msg));
		sb.append("&sender="+senderId);
		sb.append("&route="+route);
		return sb.toString();
	}
	public static void main(String[] args) {
		SmsSender.getInstance().sendSms("919899235576", "OTP is 4587");
	}
}
