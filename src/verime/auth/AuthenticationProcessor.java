package verime.auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.message.BasicNameValuePair;

import verime.json.JSONArray;
import verime.processor.DataProcessor;

public class AuthenticationProcessor extends DataProcessor 
{
	public static AuthenticationProcessor initializeProcessor(String authenticationUrl, String sessionId)
	{
		//Return the authentication processor
		return new AuthenticationProcessor(authenticationUrl, sessionId);
	}
	
	public static AuthenticationProcessor initializeProcessor(String authenticationUrl)
	{
		//Return the authentication processor
		return new AuthenticationProcessor(authenticationUrl);
	}
	
	protected AuthenticationProcessor(String authenticationUrl, String sessionId) 
	{
		//Initiate the authentication processor
		super(authenticationUrl, sessionId);
	}
	
	protected AuthenticationProcessor(String authenticationUrl)
	{
		//Initiate the authentication processor
		super(authenticationUrl);
	}
	
	public JSONArray process(String userName, String url, String potentialAuthenticationCode, CookieStore cookieStore) throws IOException
	{
		//Create the basic name value pair
		List<BasicNameValuePair> pairs = new ArrayList<>();
		//Add the user name for posting
		pairs.add(new BasicNameValuePair("user_name", userName));
		//Add the url for posting
		pairs.add(new BasicNameValuePair("authenticated_url", url));
		//Add the potential authentication code for posting
		pairs.add(new BasicNameValuePair("authentication_code", potentialAuthenticationCode));
		//Pass the pairs for processing
		return this.process("authenticate", pairs, cookieStore);
	}
}
