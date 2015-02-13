package databeans;

import org.genericdao.PrimaryKey;

@PrimaryKey("id")
public class ApplicationData {
	int id;
	private long nextUpdateTime;

	public long getNextUpdateTime() {
		return nextUpdateTime;
	}

	public void setNextUpdateTime(long nextUpdateTime) {
		this.nextUpdateTime = nextUpdateTime;
	}
}
