package vermie.register;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.message.BasicNameValuePair;

import verime.json.JSONArray;
import verime.processor.DataProcessor;

public class RegistrationProcessor extends DataProcessor 
{
	public static RegistrationProcessor initializeProcessor(String authenticationUrl, String sessionId)
	{
		//Return the registration processor
		return new RegistrationProcessor(authenticationUrl, sessionId);
	}
	
	public static RegistrationProcessor initializeProcessor(String authenticationUrl)
	{
		//Return the registration processor
		return new RegistrationProcessor(authenticationUrl);
	}
	
	protected RegistrationProcessor(String authenticationUrl, String sessionId) 
	{
		//Initiate the registration processor
		super(authenticationUrl, sessionId);
	}
	
	protected RegistrationProcessor(String authenticationUrl)
	{
		//Initiate the registration processor
		super(authenticationUrl);
	}
	
	
	public JSONArray process(String userName, String userEmail, String userPassword, CookieStore cookieStore) throws IOException
	{
		//Create the basic name value pair
		List<BasicNameValuePair> pairs = new ArrayList<>();
		//Add the user name
		pairs.add(new BasicNameValuePair("user_name", userName));
		//Add the email to our pairs
		pairs.add(new BasicNameValuePair("user_email", userEmail));
		//Add the password
		pairs.add(new BasicNameValuePair("user_password", userPassword));
		//Pass the pairs for processing
		return this.process("register", pairs, cookieStore);
	}
}
