package MT_ServerStatus.Server_IPs;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class MT_serverCheck 
{
	@BeforeClass
	public void start(){
		System.out.println("Starting nodes check..");
	}
	
	@DataProvider
	 public Object[][] getnodeips() throws IOException {
		String[][] nodeIPs = {{"ip1", "10.170.107.25"}, {"ip2", "10.170.107.24"}};
		return nodeIPs;
	 }
	
	@Test(dataProvider = "getnodeips")
	 public void checkstatus(String... ips) throws IOException, Exception {
		String currentStatus = "";
		String statuspage = "/nb/__status";
		String downIP = "";
		
		for (int i = 1; i< ips.length; i++ ){
			URL url = new URL("http://" + ips[i] + statuspage);
			currentStatus = "";
			try{
				HttpURLConnection connection = (HttpURLConnection)url.openConnection();
				connection.setRequestMethod("GET");
				connection.connect();
				connection.setConnectTimeout(100);
				
				if(connection.getResponseCode()>=200 && connection.getResponseCode()<400){
					currentStatus = ips[i] + " is OK " + Integer.toString(connection.getResponseCode());
				}
				else{
					currentStatus = ips[i] + " is down" + Integer.toString(connection.getResponseCode());
					downIP = downIP + ips[i] + "\n";
				}
			}
			catch(Exception e){
				currentStatus = ips[i] + " is down";
				downIP = downIP + ips[i] + "\n";
				throw new Exception();
			}
			finally{
				System.out.println(currentStatus);
			}
		}
		
	}

	@AfterClass
	public void ends(){
		System.out.println("Nodes check ends here.");;
	}
}
