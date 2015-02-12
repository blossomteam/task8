/**
 * 08-600 
 * hw#9
 * Jian Chen 
 * jianc1
 * Dec 06, 2014
 */

package controllor;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import model.Model;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import util.Util;

public class TwitterLoginCallbackAction extends Action {

	private static final String LOGIN_JSP = "template-result.jsp";

	public static final String NAME = "twitter-callback.do";

	public static final String P_CODE = "code";

	public TwitterLoginCallbackAction(Model model) {
		super(model);
	}

	public String getName() {
		return NAME;
	}

	public String perform(HttpServletRequest request) {
		Util.i();
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);

		String oauth_token = request.getParameter("oauth_token");
		String oauth_verifier = request.getParameter("oauth_verifier");
		Util.i("oauth_token = ", oauth_token, ", oauth_verifier = ",
				oauth_verifier);
		if (Util.isEmpty(oauth_verifier)) {
			Util.e("oauth_verifier is null");
			errors.add(Util.getString("authentication failed, response = ",
					request.getQueryString()));
			return LOGIN_JSP;
		}

		Verifier v = new Verifier(oauth_verifier);

		OAuthService service = new ServiceBuilder().provider(TwitterApi.class)
				.apiKey(model.twitterConfig.consumerKey)
				.apiSecret(model.twitterConfig.consumerSecret)
				.callback(model.twitterConfig.callbackUrl).build();
		Token requestToken = new Token(oauth_token,
				model.twitterConfig.consumerSecret);
		Util.i("fetching access token...");
		Token accessToken = service.getAccessToken(requestToken, v);
		Util.i("got access token, token = ", accessToken);

		Util.i("Now we're going to access a protected resource...");
		String PROTECTED_RESOURCE_URL = "https://api.twitter.com/1.1/account/verify_credentials.json";
		OAuthRequest requestOfApi = new OAuthRequest(Verb.GET,
				PROTECTED_RESOURCE_URL);
		service.signRequest(accessToken, requestOfApi);
		Response response = requestOfApi.send();
		Util.i(response.getBody());

		request.setAttribute("message", response.getBody());
		return LOGIN_JSP;
	}
}
