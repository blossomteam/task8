package instagram;

public class Token {
	private String access_token;
	private InstagramUser user;
	public InstagramUser getUser() {
		return user;
	}
	public void setUser(InstagramUser user) {
		this.user = user;
	}
	public String getAccessToken() {
		return access_token;
	}
	public void setAccessToken(String accessToken) {
		this.access_token = accessToken;
	}

}
