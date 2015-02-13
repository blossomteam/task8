package model;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.GenericDAO;
import org.genericdao.MatchArg;
import org.genericdao.RollbackException;
import org.genericdao.Transaction;

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
}
