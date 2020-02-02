package verime.tasks;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.message.BasicNameValuePair;

public class RegistrationProcessorTask extends DataProcessorTask
{

	public static RegistrationProcessorTask initializeTask(String url, String sessionId)
	{
		//Return the RegistrationProcessorTask
		return new RegistrationProcessorTask(url, sessionId);
	}
	
	public static RegistrationProcessorTask initializeTask(String url)
	{
		//Return a new instance of RegistrationProcessorTask
		return new RegistrationProcessorTask(url);
	}
	
	protected RegistrationProcessorTask(String url, String sessionId) {
		super(url, sessionId);
		// TODO Auto-generated constructor stub
	}
	
	protected RegistrationProcessorTask(String url)
	{
		super(url);
	}
	
	public void setProcessorData(String userName, String userEmail, String userPassword, CookieStore cookieStore)
	{
		//Initiate a name value pair for register
		List<BasicNameValuePair> registerList = new ArrayList<>();
		//Set the user name for posting
		registerList.add(new BasicNameValuePair("user_name", userName));
		//Set the email
		registerList.add(new BasicNameValuePair("user_email", userEmail));
		//Set the password
		registerList.add(new BasicNameValuePair("user_password", userPassword));
		
		//Create our processor path
		String processorPath = "register";
		//Set the processor data
		this.setProcessorData(processorPath, registerList, cookieStore);
	}

}
