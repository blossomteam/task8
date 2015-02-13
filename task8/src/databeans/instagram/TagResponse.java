package databeans.instagram;

import java.util.List;

import com.google.gson.Gson;

public class TagResponse {
	public List<ImageInfo> data;

	public static class ImageInfo {
		public Images images;
		public Caption caption;
		public Like likes;

		public static class Images {
			public Resolution standard_resolution;

			public static class Resolution {
				public String url;
			}
		}

		public static class Caption {
			public String text;
			public String created_time;
		}

		public static class Like {
			public long count;
		}
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
