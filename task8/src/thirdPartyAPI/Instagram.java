package thirdPartyAPI;

import instagram.Token;

import java.util.HashMap;

import com.google.gson.Gson;

import util.Http;

public class Instagram {
	private static final String CLIENT_ID = "59a7c2bb6992426db017e62ba204e8c7";
	private static final String CLIENT_SECRET = "188b7e4a15f84010a2f28cf2267aee72";
	private static final String URI = "http://localhost:8080/task8/auth/instagram/callback.do";
	
	public static Token getToken() throws Exception{
		HashMap<String,String> parameters = new HashMap<String,String>();
		parameters.put("client_id", CLIENT_ID);
		parameters.put("client_secret", CLIENT_SECRET);
		parameters.put("grant_type","authorization_code");
		parameters.put("redirect_uri", URI);
		parameters.put("code", "CODE");
		String url = "https://api.instagram.com/oauth/access_token";
		String response = Http.doPost(url, parameters);
		
		Gson gson = new Gson();
		Token token = gson.fromJson(response, Token.class);
		return token;
	}

}
