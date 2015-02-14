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
import javax.servlet.http.HttpSession;

import model.InstagramConfig;
import model.Model;

import org.genericdao.Transaction;

import thirdPartyAPI.Instagram;
import util.Http;
import util.Util;
import worker.DefaultInstagramAccountsUpdateTask;

import com.google.gson.Gson;

import databeans.User;
import databeans.instagram.Token;
import databeans.instagram.UserInfo;

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

			String response = Http.contentByPost(
					"https://api.instagram.com/oauth/access_token",
					"client_id", config.CLIENT_ID, "client_secret",
					config.CLIENT_SECRET, "grant_type", "authorization_code",
					"redirect_uri", config.REDIRECT_URI, "code", code);
			Util.i(response);
			if (Util.isEmpty(response)) {
				errors.add("response of request token faild");
				return LOGIN_JSP;
			}
			Token token = new Gson().fromJson(response, Token.class);

			UserInfo userInfo = Instagram.getUserInfo(token.user.id,
					token.access_token);
			if (userInfo == null) {
				errors.add(Util.getString("invalid access token, token = ",
						token));
				return LOGIN_JSP;
			}

			User user = model.getUserDAO().readByInstagramId(token.user.id);
			if (user == null) {
				model.getUserDAO().createByInstagram(token.user.id,
						token.user.username);
				user = model.getUserDAO().readByInstagramId(token.user.id);
			}

			HttpSession httpSession = request.getSession(true);
			httpSession.setAttribute("user", user);
			httpSession.setAttribute("InstagramToken", token.access_token);

			updateDefaultAccount(model, token.access_token);

			return HomeAction.NAME;
		} catch (Exception e) {
			Util.e(e);
			errors.add(e.getMessage());
			return LOGIN_JSP;
		} finally {
			if (Transaction.isActive()) {
				Transaction.rollback();
			}
		}
	}

	private void updateDefaultAccount(Model model, String accessToken) {
		DefaultInstagramAccountsUpdateTask.setValidToken(accessToken);
		if (model.applicationDAO.getNextUpdateTime() == 0) {
			//block when first run this application
			new DefaultInstagramAccountsUpdateTask(model).run();
		} else {
			new Thread(new DefaultInstagramAccountsUpdateTask(model)).start();
		}
	}
}
