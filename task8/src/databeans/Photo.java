

package databeans;

import org.genericdao.PrimaryKey;

@PrimaryKey("id")
public class Photo {
	private int id;
	private String link;
	private String userName;
	private String type;
	private String category;
	private int hugs;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getHugs() {
		return hugs;
	}

	public void setHugs(int hugs) {
		this.hugs = hugs;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
