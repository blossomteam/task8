package thirdPartyAPI;

import util.Http;
import util.Util;
import databeans.instagram.UserInfo;

public class Instagram {

	/**
	 * See <a href="http://instagram.com/developer/endpoints/users/#get_users">/users/user-id</a>
	 * 
	 * @param userId
	 * @param token
	 * @return
	 */
	public static UserInfo getUserInfo(String userId, String token) {
		String url = Util.getString("https://api.instagram.com/v1/users/",
				userId, "/");
		return Http.contentByGet(UserInfo.class, url, "access_token", token);
	}
}
