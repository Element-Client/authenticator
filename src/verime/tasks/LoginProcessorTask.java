package verime.tasks;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.message.BasicNameValuePair;

public class LoginProcessorTask extends DataProcessorTask
{

	public static LoginProcessorTask initializeTask(String url, String sessionId)
	{
		//Return the LoginProcessorTask
		return new LoginProcessorTask(url, sessionId);
	}
	
	public static LoginProcessorTask initializeTask(String url)
	{
		//Return a new instance of LoginProcessorTask
		return new LoginProcessorTask(url);
	}
	
	protected LoginProcessorTask(String url, String sessionId) {
		super(url, sessionId);
		// TODO Auto-generated constructor stub
	}
	
	protected LoginProcessorTask(String url)
	{
		super(url);
	}
	
	public void setProcessorData(String userName, String userEmail, String userPassword, CookieStore cookieStore)
	{
		//Initiate a name value pair for login
		List<BasicNameValuePair> loginList = new ArrayList<>();
		//Set the user name for posting
		loginList.add(new BasicNameValuePair("user_name", userName));
		//Set the password
		loginList.add(new BasicNameValuePair("user_password", userPassword));
		
		//Set the processor data
		this.setProcessorData("login", loginList, cookieStore);
	}

}
