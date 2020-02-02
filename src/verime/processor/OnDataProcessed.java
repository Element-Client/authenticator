package verime.processor;

import verime.json.JSONArray;

public interface OnDataProcessed 
{
	public void onSuccess(String data, JSONArray array);
	public void onFailure(String data, Exception error);
}
