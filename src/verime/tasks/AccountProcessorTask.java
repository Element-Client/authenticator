package verime.tasks;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.message.BasicNameValuePair;

public class AccountProcessorTask extends DataProcessorTask
{

	public static AccountProcessorTask initializeTask(String url, String sessionId)
	{
		//Return the AuthenticationProcessTask
		return new AccountProcessorTask(url, sessionId);
	}
	
	public static AccountProcessorTask initializeTask(String url)
	{
		//Return a new instance of AuthenticationProcessTask
		return new AccountProcessorTask(url);
	}
	
	protected AccountProcessorTask(String url, String sessionId) {
		super(url, sessionId);
		// TODO Auto-generated constructor stub
	}
	
	protected AccountProcessorTask(String url)
	{
		super(url);
	}
	
	public void setProcessorDataForGettingAccountData(CookieStore cookieStore)
	{
		//Create a basic name value pair list
		List<BasicNameValuePair> pairs = new ArrayList<>();
		//Set the get post variable
		pairs.add(new BasicNameValuePair("get", "True"));
		//Set the processor data to get
		this.setProcessorData("account", pairs, cookieStore);
	}
	
	public void setProcessorDataForUpdatingAccountData(String keyToLookFor, String valueToUpdate, CookieStore store)
	{
		//Create a name value pair list for updating data
		List<BasicNameValuePair> pairs = new ArrayList<>();
		//Set the update post variable
		pairs.add(new BasicNameValuePair("update", keyToLookFor));
		//Set the value that we will exchange for
		pairs.add(new BasicNameValuePair("value_updated", valueToUpdate));
		//Set the processor data for processing
		this.setProcessorData("account", pairs, store);
	}
}
