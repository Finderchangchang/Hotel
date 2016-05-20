package liuliu.hotel.web;

import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.ServiceConnection;
import org.ksoap2.transport.ServiceConnectionSE;

import java.io.IOException;

public class MyAndroidHttpTransport extends HttpTransportSE {

	ServiceConnectionSE serviceConnection;

	String URL;
	int    Timeout;
	public MyAndroidHttpTransport(String url) {
		super(url);
	}

	public MyAndroidHttpTransport(String url, int timeout) throws IOException {
		super(url);
		URL = url;
		System.out.println("++++++++++++++++++"+url);
		Timeout = timeout;
	}
	
	 protected ServiceConnection getServiceConnection() throws IOException {
	        serviceConnection = new ServiceConnectionSE(URL);  
	        //serviceConnection.setConnectionTimeOut(Timeout);
	        return serviceConnection;  
	    }
}
