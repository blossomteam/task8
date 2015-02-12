

package databeans;

import org.genericdao.PrimaryKey;

@PrimaryKey("id")
public class Comment {
	private int id;
	private int userid;
	private int photoid;
	private String comment;

	public int getUserid() {
		return userid;
	}

	public void setUserid(int id) {
		this.userid = id;
	}

	public int getPhotoid() {
		return photoid;
	}

	public void setPhotoid(int id) {
		this.photoid = id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
