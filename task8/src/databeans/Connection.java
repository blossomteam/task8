package databeans;

import org.genericdao.PrimaryKey;

@PrimaryKey("id")
public class Connection {
	private int id;
	private String hero;
	private String fan;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getHero() {
		return hero;
	}

	public void setHero(String hero) {
		this.hero = hero;
	}

	public String getFan() {
		return fan;
	}

	public void setFan(String fan) {
		this.fan = fan;
	}

}
