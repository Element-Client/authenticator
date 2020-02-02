package verime.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.message.BasicNameValuePair;

import verime.json.JSONArray;
import verime.processor.DataProcessor;

public class AccountProcessor extends DataProcessor
{

	public static AccountProcessor initializeProcessor(String authenticationUrl, String sessionId)
	{
		//Return the Account processor
		return new AccountProcessor(authenticationUrl, sessionId);
	}
	
	public static AccountProcessor initializeProcessor(String authenticationUrl)
	{
		//Return the Account processor
		return new AccountProcessor(authenticationUrl);
	}
	
	protected AccountProcessor(String authenticationUrl, String sessionId) 
	{
		//Initiate the Account processor
		super(authenticationUrl, sessionId);
	}
	
	protected AccountProcessor(String authenticationUrl)
	{
		//Initiate the Account processor
		super(authenticationUrl);
	}
	
	public JSONArray getAccountData(CookieStore store) throws IOException
	{
		//Create a list for posting
		List<BasicNameValuePair> postList = new ArrayList<>();
		//Add the get variable for post
		postList.add(new BasicNameValuePair("get", "True"));
		//Process the data
		return this.process("account", postList, store);
	}
	
	public JSONArray updateObjectInAccount(String key, String value, CookieStore store) throws IOException
	{
		//Create a name value pair for updating account data
		List<BasicNameValuePair> updateList = new ArrayList<>();
		//Add the update key to the list
		updateList.add(new BasicNameValuePair("update", key));
		//Add the value to the list
		updateList.add(new BasicNameValuePair("value_updated", value));
		//Post the data to the server
		return this.process("account", updateList, store);
	}
}
