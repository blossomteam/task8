

package model;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.GenericDAO;
import org.genericdao.MatchArg;
import org.genericdao.RollbackException;
import org.genericdao.Transaction;

import databeans.User;

public class UserDAO extends GenericDAO<User> {

	public UserDAO(String tableName, ConnectionPool pool)
			throws DAOException {
		super(User.class, tableName, pool);
	}

	public void create(User user) throws RollbackException {
		try {
			Transaction.begin();
			if (readByUserName(user.getUserName()) != null) {
				throw new RollbackException("User name already exists");
			}
			
			if (readByUserAccount(user.getUserAccount()) != null) {
				throw new RollbackException("User account already exists");
			}
			super.create(user);
			Transaction.commit();
		} finally {
			if (Transaction.isActive())
				Transaction.rollback();
		}
	}

	public void setPassword(String email, String password)
			throws RollbackException {
		try {
			Transaction.begin();
			User dbUser = readByUserName(email);

			if (dbUser == null) {
				throw new RollbackException("User " + email
						+ " no longer exists");
			}
			dbUser.setPassword(password);

			update(dbUser);
			Transaction.commit();
		} finally {
			if (Transaction.isActive())
				Transaction.rollback();
		}
	}

	public User readByUserName(String userName) throws RollbackException {
		User[] users = match(MatchArg.equals("userName", userName));

		if (users.length > 1) {
			throw new RollbackException("More than one account matched");
		}

		if (users.length == 0) {
			return null;
		}
		return users[0];
	}
	
	public User readByUserAccount(String userAccount) throws RollbackException {
		User[] users = match(MatchArg.equals("userAccount", userAccount));

		if (users.length > 1) {
			throw new RollbackException("More than one account matched");
		}

		if (users.length == 0) {
			return null;
		}
		return users[0];
	}


}
