package verime.processor;

//REQUIRED GRADLE IMPLEMENTATION: compile group: 'org.apache.httpcomponents' , name: 'httpclient-android' , version: '4.3.5.1'
//Also required : useLibrary 'org.apache.http.legacy'!
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import verime.json.JSONArray;
import verime.json.JSONException;
import verime.json.JSONObject;

public class DataProcessor 
{
	//Create a default private authentication url
	public static String DEFAULT_PRIVATE_AUTHENTICATION_URL = "http://localhost/VeriMe/oauth";
	//Create a default public authentication url
	public static String DEFAULT_PUBLIC_AUTHENTICATION_URL = "https://verime.000webhostapp.com/oauth";
	//Set the current authentication url
	private String authenticationUrl = this.DEFAULT_PRIVATE_AUTHENTICATION_URL;
	//Create an empty cookie store
	private CookieStore store;
	//Create an empty session id string
	protected String sessionId;
	//Create a default OnDataProcessed interface
	private OnDataProcessed onProcessed = new OnDataProcessed()
			{

				@Override
				public void onSuccess(String data, JSONArray array) {
					// TODO Auto-generated method stub
					System.out.println("Data process was successful");
					System.out.println(data);
				}

				@Override
				public void onFailure(String data, Exception error) {
					// TODO Auto-generated method stub
					System.out.println(data);
					System.out.println("Data process fail: " + error.getMessage());
				}
		
			};
	//Create a default list of objects to be got later
	private List<Object> classes = new ArrayList<>();
	//For initializing the processor without having the developer create their own session id
	public static DataProcessor initializeProcessor(String authenticationUrl)
	{
		//Return a new instance of our processor
		return new DataProcessor(authenticationUrl);
	}
	//For initializing our processor
	public static DataProcessor initializeProcessor(String authenticationUrl,String sessionId)
	{
		//Create the Data Processor
		DataProcessor processor = new DataProcessor(authenticationUrl, sessionId);
		//Return the data processor
		return processor;
	}
	
	//To be run only via initialization method
	protected DataProcessor(String authenticationUrl)
	{
		//Set the authentication url
		this.authenticationUrl = authenticationUrl;
		//Set the session id
		this.sessionId = this.generateRandomText("AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789", 10);
	}
	
	//To be only run using the initialization method
	protected DataProcessor(String authenticationUrl, String sessionId)
	{
		//Initiate our authentication url
		this.authenticationUrl = authenticationUrl;
		this.sessionId = sessionId;
	}
	
	//For setting the OnDataProcessed interface to something else
	public void setOnDataProcessed(OnDataProcessed onProcessed)
	{
		//Set our class-wide data processed event to onProcessed
		this.onProcessed = onProcessed;
	}
	
	//For generating random text
	protected String generateRandomText(String alphaNumeric, int length)
	{
		//Results string to be returned
		String results = "";
		//Loop through the length
		for(int index = 0; index < length; index++)
		{
			//Get the character at a random index
			Character character = (Character) alphaNumeric.charAt(((int)(Math.random() * alphaNumeric.length() - 1) + 1));
			//Update the results string
			results += character;
		}
		
		//Return the results
		return results;
	}
	
	//Process the data, return 
	public List<Object> processData(String processorPath, List<BasicNameValuePair> pairs, CookieStore store) throws ClientProtocolException, IOException
	{
		//Add the session variable to our pairs
		pairs.add(new BasicNameValuePair("SESSION", this.sessionId));
		//Initiate the cookie store
		this.store = store;
		//Initiate a classes list
		List<Object> classes = new ArrayList<>();
		//Create a context
		HttpContext context = new BasicHttpContext();
		//Bind context
		context.setAttribute("http.cookie-store", this.store);
		//Initiate a client builder
		HttpClientBuilder builder = HttpClients.custom();
		//Create our http client
		HttpClient client = builder.build();
		//Create an http post
		HttpPost post = new HttpPost(this.authenticationUrl + "/" + processorPath);
		//Create a url encoded form entity
		post.setEntity(new UrlEncodedFormEntity(pairs));
		//Execute our data
		HttpResponse response = client.execute(post, context);
		//Create an entity based on the response
		HttpEntity entity = response.getEntity();
		//Add the input stream
		classes.add(new BufferedInputStream(entity.getContent()));
		//Add the cookie
		classes.add(store);
		//Add the context
		classes.add(context);
		//Add the builder
		classes.add(builder);
		//Set the class-wide classes variable to function classes
		this.classes = classes;
		//Return our entity's content as a buffered input stream
		return classes;
	}
	
	public JSONArray process(String processorPath, List<BasicNameValuePair> pairs, CookieStore store) throws IOException
	{
		//Process the data
		List<Object> processedData = this.processData(processorPath, pairs, store);
		//Get the results as a buffered input stream
		BufferedInputStream stream = (BufferedInputStream) processedData.get(0);
		//Create a buffer for reading the stream's content
		byte[] buffer = new byte[1024];
		//Read the stream's content
		stream.read(buffer);
		//Convert the byte array to a string
		String data = new String(buffer);
		try
		{
			//Load the data into a json array
			JSONArray dataArray = new JSONArray(data);
			//Loop through the json array
			for(int index = 0; index < dataArray.length(); index++)
			{
				//Get the default json object
				JSONObject object = dataArray.getJSONObject(index);
				//Check if we have an error
				if(object.getBoolean("error"))
				{
					//Fail with the error
					this.onProcessed.onFailure(object.getString("result"), new Exception(object.getString("result")));
				}
				else
				{
					//Do not fail, this succeeded
					this.onProcessed.onSuccess(data, dataArray);
				}
					
			}
			
			//Return our json array
			return dataArray;
		}catch(JSONException error)
		{
			//Notify that we failed
			this.onProcessed.onFailure(data, error);
			//Return the fail cause
			return new JSONArray("[{'error': True, 'result': \"" + error.getMessage() + "\"}]");
		}
	}
	
	public String getAuthenticationUrl()
	{
		//Return the authentication url
		return this.authenticationUrl;
	}
	
	public OnDataProcessed getOnDataProcessed()
	{
		//Return our on data processed interface
		return this.onProcessed;
	}
	
	public CookieStore getCookieStore()
	{
		//Return the cookie store
		return this.store;
	}
	
	public String getSessionID()
	{
		//Return the session id
		return this.sessionId;
	}
	
	public List<Object> getClassesPostProcessing()
	{
		//Return our list of classes that were added after processing
		return this.classes;
	}
}
