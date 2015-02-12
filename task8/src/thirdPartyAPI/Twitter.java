package thirdPartyAPI;

import model.TwitterConfig;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import util.Util;

import com.google.gson.Gson;

import databeans.twitter.VerifyCredentials;

public class Twitter {

	/**
	 * See <a href=
	 * "https://dev.twitter.com/rest/reference/get/account/verify_credentials"
	 * >verify_credentials</a>
	 * 
	 * @param userId
	 * @param token
	 * @return
	 */
	public static VerifyCredentials getVerifyCredential(
			TwitterConfig twitterConfig, Token accessToken) {
		OAuthService service = new ServiceBuilder().provider(TwitterApi.class)
				.apiKey(twitterConfig.consumerKey)
				.apiSecret(twitterConfig.consumerSecret)
				.callback(twitterConfig.callbackUrl).build();

		String PROTECTED_RESOURCE_URL = "https://api.twitter.com/1.1/account/verify_credentials.json?include_entities=false&skip_status=true";
		OAuthRequest requestOfApi = new OAuthRequest(Verb.GET,
				PROTECTED_RESOURCE_URL);
		service.signRequest(accessToken, requestOfApi);
		Response response = requestOfApi.send();
		String content = response.getBody();
		Util.i(content);
		return new Gson().fromJson(content, VerifyCredentials.class);
	}
}
