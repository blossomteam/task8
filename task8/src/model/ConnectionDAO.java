package model;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.GenericDAO;
import org.genericdao.MatchArg;
import org.genericdao.RollbackException;
import org.genericdao.Transaction;

import util.Util;
import databeans.Connection;

public class ConnectionDAO extends GenericDAO<Connection> {

	public ConnectionDAO(String tableName, ConnectionPool pool)
			throws DAOException {
		super(Connection.class, tableName, pool);
	}

	public void create(Connection connection) throws RollbackException {
		try {
			Transaction.begin();
			Connection[] connections = match(MatchArg.and(
					MatchArg.equals("hero", connection.getHero()),
					MatchArg.equals("fan", connection.getFan())));
			if (connections == null || connections.length == 0) {
				create(connection);
			}
			Transaction.commit();
		} catch (RollbackException e) {
			Util.e(e);
			throw e;
		} finally {
			if (Transaction.isActive()) {
				Transaction.rollback();
			}
		}
	}

	public Connection[] getHerosOf(String userName) throws RollbackException {
		Connection[] connections = match(MatchArg.equals("fan", userName));
		return connections;
	}

	public Connection[] getFansOf(String userName) throws RollbackException {
		Connection[] connections = match(MatchArg.equals("hero", userName));
		return connections;
	}

}
