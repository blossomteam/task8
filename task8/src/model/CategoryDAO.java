

package model;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.GenericDAO;
import org.genericdao.MatchArg;
import org.genericdao.RollbackException;
import org.genericdao.Transaction;

import databeans.Category;

public class CategoryDAO extends GenericDAO<Category> {

	public CategoryDAO(String tableName, ConnectionPool pool) throws DAOException {
		super(Category.class, tableName, pool);
	}

	public void create(Category category) throws RollbackException {
		try {
			Transaction.begin();
			if (read(category.getCategory()) != null) {
				throw new RollbackException("Category already exists");
			}
			super.create(category);
			Transaction.commit();
		} finally {
			if (Transaction.isActive())
				Transaction.rollback();
		}
	}

	public Category[] readAll() throws RollbackException {
		return match();
	}

	public Category read(String category) throws RollbackException {
		Category[] categorys = match(MatchArg.equals("category", category));

		if (categorys.length == 0) {
			return null;
		}
		return categorys[0];
	}

}
