package verime.tasks;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.message.BasicNameValuePair;

public class UserProcessorTask extends DataProcessorTask
{

	public static UserProcessorTask initializeTask(String url, String sessionId)
	{
		//Return the AuthenticationProcessTask
		return new UserProcessorTask(url, sessionId);
	}
	
	public static UserProcessorTask initializeTask(String url)
	{
		//Return a new instance of AuthenticationProcessTask
		return new UserProcessorTask(url);
	}
	
	protected UserProcessorTask(String url, String sessionId) {
		super(url, sessionId);
		// TODO Auto-generated constructor stub
	}
	
	protected UserProcessorTask(String url)
	{
		super(url);
	}
	
	public void setProcessorData(CookieStore cookieStore)
	{
		//Create a basic name value pair list
		List<BasicNameValuePair> pairs = new ArrayList<>();
		//Set the processor data to get
		this.setProcessorData("get", pairs, cookieStore);
	}
}
