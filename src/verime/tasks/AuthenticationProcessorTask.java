package verime.tasks;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.message.BasicNameValuePair;

public class AuthenticationProcessorTask extends DataProcessorTask
{

	public static AuthenticationProcessorTask initializeTask(String url, String sessionId)
	{
		//Return the AuthenticationProcessTask
		return new AuthenticationProcessorTask(url, sessionId);
	}
	
	public static AuthenticationProcessorTask initializeTask(String url)
	{
		//Return a new instance of AuthenticationProcessTask
		return new AuthenticationProcessorTask(url);
	}
	
	protected AuthenticationProcessorTask(String url, String sessionId) {
		super(url, sessionId);
		// TODO Auto-generated constructor stub
	}
	
	protected AuthenticationProcessorTask(String url)
	{
		super(url);
	}
	
	public void setProcessorData(String userName, String url, String potentialAuthenticationCode, CookieStore cookieStore)
	{
		//Create a basic name value pair list
		List<BasicNameValuePair> pairs = new ArrayList<>();
		//Add the user name for posting
		pairs.add(new BasicNameValuePair("user_name", userName));
		//Add the url for posting
		pairs.add(new BasicNameValuePair("authenticated_url", url));
		//Add the potential authentication code for posting
		pairs.add(new BasicNameValuePair("authentication_code", potentialAuthenticationCode));
		//Pass our data to the servers
		this.setProcessorData("authenticate", pairs, cookieStore);
	}

}
