package verime;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;

import verime.login.LoginProcessor;
import verime.processor.DataProcessor;
import verime.user.AccountProcessor;
import verime.user.UserProcessor;
import vermie.register.RegistrationProcessor;


public class Test 
{
	protected static CookieStore store = new BasicCookieStore();
	public static void main(String[] args) throws ClientProtocolException, IOException 
	{
		LoginProcessor login = LoginProcessor.initializeProcessor(LoginProcessor.DEFAULT_PRIVATE_AUTHENTICATION_URL, "Snatch");
		login.process("1dime", "OneDime19!", store);
		
		AccountProcessor account = AccountProcessor.initializeProcessor(AccountProcessor.DEFAULT_PRIVATE_AUTHENTICATION_URL, "Snatch");
		account.updateObjectInAccount("user_email", "onedimemobile@gmail.com", store);
	}

}
