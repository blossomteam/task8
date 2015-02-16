package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.GenericDAO;
import org.genericdao.MatchArg;
import org.genericdao.RollbackException;
import org.genericdao.Transaction;

import util.Constants;
import util.Util;
import databeans.Photo;

public class PhotoDAO extends GenericDAO<Photo> {

	public PhotoDAO(String tableName, ConnectionPool pool) throws DAOException {
		super(Photo.class, tableName, pool);
	}

	public void create(Photo photo) throws RollbackException {
		try {
			Transaction.begin();
			if (photo.getUrl() != null && readByUrl(photo.getUrl()) != null) {
				throw new RollbackException("Photo already exists");
			}
			super.create(photo);
			Transaction.commit();
		} finally {
			if (Transaction.isActive())
				Transaction.rollback();
		}
	}

	public Photo readByUrl(String url) throws RollbackException {
		Photo[] photos = match(MatchArg.equals("url", url));

		if (photos.length == 0) {
			return null;
		}
		return photos[0];
	}

	public int getMaxId() throws RollbackException {
		Photo[] photo = match(MatchArg.max("id"));
		if (photo == null || photo.length == 0) {
			return 0;
		}
		int maxId = photo[0].getId();
		return maxId;
	}

	static Comparator<Photo> decreaseById = new Comparator<Photo>() {

		@Override
		public int compare(Photo o1, Photo o2) {
			return o2.getId() - o1.getId();
		}
	};

	public Photo[] getNewPhotos(int maxId) throws RollbackException {
		try {
			maxId = Math.min(maxId, getMaxId());
			int minId = maxId - Constants.PHOTO_NUMBER_PER_PAGE;
			Photo[] photos = match(MatchArg.and(
					MatchArg.greaterThan("id", minId),
					MatchArg.lessThanOrEqualTo("id", maxId)));
			Arrays.sort(photos, decreaseById);
			return photos;
		} finally {
			if (Transaction.isActive())
				Transaction.rollback();
		}
	}

	public static Photo[] getTopN(Photo[] photos, int n) {
		if (photos == null || photos.length == 0) {
			return null;
		}
		n = Math.min(n, photos.length);
		PriorityQueue<Photo> latestPhotos = new PriorityQueue<>(photos.length,
				decreaseById);
		for (Photo photo : photos) {
			latestPhotos.add(photo);
		}

		Photo[] orderedPhotos = new Photo[n];
		int i = 0;
		while (i < n) {
			orderedPhotos[i++] = latestPhotos.poll();
		}
		return orderedPhotos;
	}

	public static Photo[] filter(Photo[] photos, int minId, int maxId) {
		if (photos == null) {
			return null;
		}
		ArrayList<Photo> validPhotos = new ArrayList<>();
		for (Photo photo : photos) {
			if (photo.getId() >= minId && photo.getId() <= maxId) {
				validPhotos.add(photo);
			}
		}
		return validPhotos.toArray(new Photo[validPhotos.size()]);
	}

	public Photo[] getPhotosOfTag(String tag) throws RollbackException {
		try {
			String tagPattern = Util.getString("#", tag, " ");
			Photo[] photos = match(MatchArg.containsIgnoreCase("text",
					tagPattern));
			return photos;
		} finally {
			if (Transaction.isActive()) {
				Transaction.rollback();
			}
		}
	}

	public Photo[] getPhotosOfUser(int id) throws RollbackException {
		try {
			Photo[] photos = match(MatchArg.equals("userId", id));
			return photos;
		} finally {
			if (Transaction.isActive()) {
				Transaction.rollback();
			}
		}
	}
}
