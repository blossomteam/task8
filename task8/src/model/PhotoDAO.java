package model;

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

	private int getMaxId() throws RollbackException {
		Photo[] photo = match(MatchArg.max("id"));
		if (photo == null || photo.length == 0) {
			return 0;
		}
		int maxId = photo[0].getId();
		return maxId;
	}

	Comparator<Photo> decreaseById = new Comparator<Photo>() {

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

	private Photo[] getTopN(Photo[] photos, int n) {
		PriorityQueue<Photo> latestPhotos = new PriorityQueue<>(n, decreaseById);
		for (Photo photo : photos) {
			latestPhotos.add(photo);
		}

		Photo[] orderedPhotos = new Photo[latestPhotos.size()];
		int i = 0;
		while (!latestPhotos.isEmpty()) {
			orderedPhotos[i++] = latestPhotos.poll();
		}
		return orderedPhotos;
	}

	public Photo[] getPhotosOfTag(String tag, int maxId)
			throws RollbackException {
		try {
			String tagPattern = Util.getString("#", tag, " ");
			Photo[] photos = match(MatchArg.and(
					MatchArg.containsIgnoreCase("text", tagPattern),
					MatchArg.lessThanOrEqualTo("id", maxId)));
			return getTopN(photos, Constants.PHOTO_NUMBER_PER_PAGE);
		} finally {
			if (Transaction.isActive()) {
				Transaction.rollback();
			}
		}
	}

	public Photo[] getPhotosOfUser(int id, int maxId) throws RollbackException {
		try {
			Photo[] photos = match(MatchArg.and(MatchArg.equals("userId", id),
					MatchArg.lessThanOrEqualTo("id", maxId)));
			return getTopN(photos, Constants.PHOTO_NUMBER_PER_PAGE);
		} finally {
			if (Transaction.isActive()) {
				Transaction.rollback();
			}
		}
	}
}
