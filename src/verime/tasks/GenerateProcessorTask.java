package verime.tasks;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.message.BasicNameValuePair;

public class GenerateProcessorTask extends DataProcessorTask
{
	public static GenerateProcessorTask initializeTask(String url, String sessionId)
	{
		//Return the GenerateProcessorTask
		return new GenerateProcessorTask(url, sessionId);
	}
	
	public static GenerateProcessorTask initializeTask(String url)
	{
		//Return a new instance of GenerateProcessorTask
		return new GenerateProcessorTask(url);
	}
	
	protected GenerateProcessorTask(String url, String sessionId) {
		super(url, sessionId);
		// TODO Auto-generated constructor stub
	}
	
	protected GenerateProcessorTask(String url)
	{
		super(url);
	}
	
	public void setProcessorData(String userName, String url, String potentialAuthenticationCode, CookieStore cookieStore)
	{
		//Create a basic name value pair list
		List<BasicNameValuePair> pairs = new ArrayList<>();
		//Add the url for posting
		pairs.add(new BasicNameValuePair("url", url));
		//Pass our data to the servers
		this.setProcessorData("generate", pairs, cookieStore);
	}

}
