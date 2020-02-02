package verime.login;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.message.BasicNameValuePair;

import verime.json.JSONArray;
import verime.processor.DataProcessor;

public class LoginProcessor extends DataProcessor 
{
	public static LoginProcessor initializeProcessor(String authenticationUrl, String sessionId)
	{
		//Return the login processor
		return new LoginProcessor(authenticationUrl, sessionId);
	}
	
	public static LoginProcessor initializeProcessor(String authenticationUrl)
	{
		//Return the login processor
		return new LoginProcessor(authenticationUrl);
	}
	
	protected LoginProcessor(String authenticationUrl, String sessionId) 
	{
		//Initiate the login processor
		super(authenticationUrl, sessionId);
	}
	
	protected LoginProcessor(String authenticationUrl)
	{
		//Initiate the login processor
		super(authenticationUrl);
	}
	
	public JSONArray process(String userName, String userPassword, CookieStore cookieStore) throws IOException
	{
		//Create the basic name value pair
		List<BasicNameValuePair> pairs = new ArrayList<>();
		//Add the user name
		pairs.add(new BasicNameValuePair("user_name", userName));
		//Add the password
		pairs.add(new BasicNameValuePair("user_password", userPassword));
		//Pass the pairs for processing
		return this.process("login", pairs, cookieStore);
	}
}
