package verime.tasks;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import verime.json.JSONArray;
import verime.json.JSONException;
import verime.json.JSONObject;
import verime.processor.DataProcessor;
import verime.processor.OnDataProcessed;

public class DataProcessorTask extends AsyncTask<String, Void, String> {
	// Create a default private authentication url
	public static String DEFAULT_PRIVATE_AUTHENTICATION_URL = "http://localhost/VeriMe/oauth";
	// Create a default public authentication url
	public static String DEFAULT_PUBLIC_AUTHENTICATION_URL = "https://verime.000webhostapp.com/oauth";
	// Set the current authentication url
	private String authenticationUrl = this.DEFAULT_PRIVATE_AUTHENTICATION_URL;
	// Create an empty cookie store
	private CookieStore store;
	// Create an empty session id string
	protected String sessionId;
	// Create a basic name value pair array
	protected List<BasicNameValuePair> pairs = new ArrayList<>();
	// Create a class-wide processor path
	protected String processorPath = "";
	// Protect the cookies!
	protected CookieStore cookieStore = new BasicCookieStore();
	// Initiate a new data processor
	protected DataProcessor dataProcessor = DataProcessor.initializeProcessor(authenticationUrl, sessionId);
	// Create a json array instance
	protected JSONArray results = new JSONArray();
	// Initialize an empty classes list
	protected List<Object> classes = new ArrayList<>();

	// For initializing the thread
	public static DataProcessorTask initializeTask(String url, String sessionId) 
	{
		// Return the DataProcessorTask
		return new DataProcessorTask(url, sessionId);
	}
	
	public static DataProcessorTask initializeTask(String authenticationUrl)
	{
		//Return a new instance of a data process task
		return new DataProcessorTask(authenticationUrl);
	}
	
	protected DataProcessorTask(String url, String sessionId) {
		// Set the authentication url
		this.authenticationUrl = url;
		// Set the session id
		this.sessionId = sessionId;
		// Initiate the data processor
		this.dataProcessor = DataProcessor.initializeProcessor(this.authenticationUrl, sessionId);
	}
	
	protected DataProcessorTask(String authenticationUrl)
	{
		//Set the authentication url
		this.authenticationUrl = authenticationUrl;
		//Set the session id
		this.sessionId = this.generateRandomText("AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz", 10);
		//Initiate the data processor
		this.dataProcessor = DataProcessor.initializeProcessor(authenticationUrl, this.sessionId);
	}
	
	protected String generateRandomText(String alphaNumeric, int length)
	{
		//Create an empty results
		String results = "";
		//Loop through the length
		for(int index = 0; index < length; index++)
		{
			//Get the character located at a random index
			Character character = (Character) alphaNumeric.charAt(((int)(Math.random() * alphaNumeric.length()) + 1));
			//Update the results string
			results += character;
		}
		
		//Return the results
		return results;
	}
	
	public void setProcessorData(String processorPath, List<BasicNameValuePair> pairs, CookieStore store) 
	{
		// Update the class-wide processor path
		this.processorPath = processorPath;
		// Update our pairs
		this.pairs = pairs;
		// Update the cookie store
		this.cookieStore = store;
	}

	@Override
	protected String doInBackground(String... arg0) 
	{
		// Return the first part of the processed data list
		try {
			this.classes = dataProcessor.processData(processorPath, pairs, cookieStore);
			// Get the buffered input stream
			BufferedInputStream stream = (BufferedInputStream) classes.get(0);
			// Create a byte array for buffering
			byte[] buffer = new byte[1024];
			// Read the stream's data into the buffer
			stream.read(buffer);
			// Return the buffer in a string
			return new String(buffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// Return our error as a json array
			return "[{'error' => True, 'result' => \"" + e.getMessage() + "\"}]";
		}
	}

	@Override
	public void onPostExecute(String data) 
	{
		try {
			// Load the data into a json array
			JSONArray dataArray = new JSONArray(data);
			// Loop through the json array
			for (int index = 0; index < dataArray.length(); index++) {
				// Get the default json object
				JSONObject object = dataArray.getJSONObject(index);
				// Check if we have an error
				if (object.getBoolean("error")) {
					// Fail with the error
					this.dataProcessor.getOnDataProcessed().onFailure(object.getString("result"),
							new Exception(object.getString("result")));
				} else {
					// Do not fail, this succeeded
					this.dataProcessor.getOnDataProcessed().onSuccess(data, dataArray);
				}

			}

			// Return our json array
			this.results = dataArray;
		} catch (JSONException error) {
			// Notify that we failed
			this.dataProcessor.getOnDataProcessed().onFailure(data, error);
			// Return the fail cause
			this.results = new JSONArray("[{'error': True, 'result': \"" + error.getMessage() + "\"}]");
		}
	}

	public void setOnDataProcessed(OnDataProcessed onProcessed) 
	{
		// Set on data processed interface to the onProcessed variable
		this.dataProcessor.setOnDataProcessed(onProcessed);
	}

	public JSONArray getResults() 
	{
		return this.results;
	}

	public List<Object> getClasses() 
	{
		return this.classes;
	}

	public DataProcessor getDataProcessor() 
	{
		return this.dataProcessor;
	}
	
	public String getSessionID()
	{
		return this.sessionId;
	}
}
