package verime.auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.message.BasicNameValuePair;

import verime.json.JSONArray;
import verime.processor.DataProcessor;

public class GenerateProcessor extends DataProcessor 
{

	public static GenerateProcessor initializeProcessor(String authenticationUrl, String sessionId)
	{
		//Return the generate processor
		return new GenerateProcessor(authenticationUrl, sessionId);
	}
	
	public static GenerateProcessor initializeProcessor(String authenticationUrl)
	{
		//Return the generate processor
		return new GenerateProcessor(authenticationUrl);
	}
	
	protected GenerateProcessor(String authenticationUrl, String sessionId) 
	{
		//Initiate the generate processor
		super(authenticationUrl, sessionId);
	}
	
	protected GenerateProcessor(String authenticationUrl)
	{
		//Initiate the generate processor
		super(authenticationUrl);
	}
	
	public JSONArray process(String url, CookieStore cookieStore) throws IOException
	{
		//Create a basic name value list
		List<BasicNameValuePair> pairs = new ArrayList<>();
		//Add the url to the pairs
		pairs.add(new BasicNameValuePair("url", url));
		//Process the data
		return process("generate", pairs, cookieStore);
	}
}
