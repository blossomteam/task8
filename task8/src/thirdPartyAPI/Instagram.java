package thirdPartyAPI;

import java.util.ArrayList;
import java.util.List;

import util.Http;
import util.Util;
import databeans.Photo;
import databeans.instagram.TagResponse;
import databeans.instagram.UserInfo;

public class Instagram {

	/**
	 * See <a
	 * href="http://instagram.com/developer/endpoints/users/#get_users">/users
	 * /user-id</a>
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

	/**
	 * See <a href=
	 * "http://instagram.com/developer/endpoints/tags/#get_tags_media_recent"
	 * >/tags/tag-name/media/recent</a>
	 * 
	 * @param accessToken
	 * @param tag
	 */
	public static List<Photo> getPictureOf(String accessToken, String tag) {
		String url = Util.getString("https://api.instagram.com/v1/tags/", tag,
				"/media/recent");
		TagResponse response = Http.contentByGet(TagResponse.class, url,
				"access_token", accessToken, "count", 100);
		if (response == null) {
			Util.e("exit, response is null");
			return null;
		}
		Util.i(response);
		List<Photo> photos = new ArrayList<Photo>();
		for (TagResponse.ImageInfo info : response.data) {
			if (info == null) {
				continue;
			}
			Photo photo = new Photo();
			photo.setLikes(info.likes.count);
			photo.setTag(tag);
			photo.setTime(info.caption.created_time);
			photo.setUrl(info.images.standard_resolution.url);
			photo.setText(info.caption.text);
			photos.add(photo);
		}
		return photos;
	}
}
