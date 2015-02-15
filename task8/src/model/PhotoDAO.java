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

	public Photo[] getNewPhotos() throws RollbackException {
		try {
			int maxId = getMaxId();
			int minId = maxId - Constants.photoNumbers;
			Photo[] photos = match(MatchArg.greaterThan("id", minId));
			Arrays.sort(photos, new Comparator<Photo>() {

				@Override
				public int compare(Photo o1, Photo o2) {
					return -((Long) o1.getTime()).compareTo(o2.getTime());
				}
			});
			return photos;
		} finally {
			if (Transaction.isActive())
				Transaction.rollback();
		}
	}

	private int getMaxId() throws RollbackException {
		Photo[] photo = match(MatchArg.max("id"));
		if (photo == null || photo.length == 0) {
			return 0;
		}
		int maxId = photo[0].getId();
		return maxId;
	}

	private Photo[] getLatestPhotos(Photo[] photos, int count) {
		PriorityQueue<Photo> latestPhotos = new PriorityQueue<>(count,
				new Comparator<Photo>() {

					@Override
					public int compare(Photo o1, Photo o2) {
						return -((Long) o1.getTime()).compareTo(o2.getTime());
					}
				});
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

	public Photo[] getPhotosOf(String tag) throws RollbackException {
		try {
			String tagPattern = Util.getString("#", tag, " ");
			Photo[] photos = match(MatchArg.containsIgnoreCase("text", tagPattern));
			return getLatestPhotos(photos, Constants.photoNumbers);
		} finally {
			if (Transaction.isActive())
				Transaction.rollback();
		}
	}
}
