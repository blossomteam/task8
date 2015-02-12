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

import model.InstagramConfig;
import model.Model;
import util.Http;
import util.Util;

import com.google.gson.Gson;

import databeans.instagram.RequestToken;

public class InstagramLoginCallbackAction extends Action {

	private static final String LOGIN_JSP = "template-result.jsp";

	public static final String NAME = "instagram-callback.do";

	public static final String P_CODE = "code";

	public InstagramLoginCallbackAction(Model model) {
		super(model);
	}

	public String getName() {
		return NAME;
	}

	public String perform(HttpServletRequest request) {
		Util.i();
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);

		try {
			InstagramConfig config = model.instagramConfig;
			String code = request.getParameter("code");

			if (Util.isEmpty(code)) {
				errors.add(Util.getString("authentication failed, response = ",
						request.getQueryString()));
				return LOGIN_JSP;
			}

			String response = Http.doPost(
					"https://api.instagram.com/oauth/access_token",
					"client_id", config.CLIENT_ID, "client_secret",
					config.CLIENT_SECRET, "grant_type", "authorization_code",
					"redirect_uri", config.REDIRECT_URI, "code", code);
			Util.i(response);
			if (Util.isEmpty(response)) {
				errors.add("response of request token faild");
				return LOGIN_JSP;
			}
			RequestToken rq = new Gson().fromJson(response, RequestToken.class);
			request.setAttribute("message", "auth success, user = "
					+ rq.user.username);
			return "template-result.jsp";
		} catch (Exception e) {
			Util.e(e);
			errors.add(e.getMessage());
			return LOGIN_JSP;
		}
	}
}
