package verime.user;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.CookieStore;

import verime.json.JSONArray;
import verime.processor.DataProcessor;

public class UserProcessor extends DataProcessor
{
	public static UserProcessor initializeProcessor(String authenticationUrl, String sessionId)
	{
		//Return the user processor
		return new UserProcessor(authenticationUrl, sessionId);
	}
	
	public static UserProcessor initializeProcessor(String authenticationUrl)
	{
		//Return the user processor
		return new UserProcessor(authenticationUrl);
	}
	
	protected UserProcessor(String authenticationUrl, String sessionId) 
	{
		//Initiate the user processor
		super(authenticationUrl, sessionId);
	}
	
	protected UserProcessor(String authenticationUrl)
	{
		//Initiate the user processor
		super(authenticationUrl);
	}
	
	
	public JSONArray process(CookieStore store) throws IOException
	{
		//Process our data
		return this.process("get", new ArrayList<>(), store);
	}
}
