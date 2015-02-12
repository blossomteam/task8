/**
 * 08-600 
 * hw#9
 * Jian Chen 
 * jianc1
 * Dec 06, 2014
 */

package model;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;

import util.Util;

public class Model {
	private UserDAO userDAO;
	private ConnectionPool pool = null;
	public InstagramConfig instagramConfig = null;
	public TwitterConfig twitterConfig = null;

	public Model(ServletConfig config) throws ServletException {
		try {
			String configFileDir = config.getServletContext().getRealPath(
					"/config");
			Util.i("configFileDir = ", configFileDir);

			// SQL
			String jdbcDriver = config.getInitParameter("jdbcDriverName");
			String jdbcURL = config.getInitParameter("jdbcURL");
			MySQLConfig mySQLConfig = new MySQLConfig(configFileDir);
			pool = new ConnectionPool(jdbcDriver, jdbcURL,
					mySQLConfig.username, mySQLConfig.password);

			// INSTAGRAM
			instagramConfig = new InstagramConfig(configFileDir);

			// TWITTER
			twitterConfig = new TwitterConfig(configFileDir);

			// DAOs
			userDAO = new UserDAO("user", pool);
		} catch (DAOException e) {
			throw new ServletException(e);
		}
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

}
