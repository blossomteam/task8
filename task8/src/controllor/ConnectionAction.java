/**
 * 08-600 
 * hw#9
 * Jian Chen 
 * jianc1
 * Dec 06, 2014
 */

package controllor;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import model.Model;

import org.genericdao.RollbackException;
import org.genericdao.Transaction;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import util.Util;
import databeans.Connection;
import databeans.User;
import formbeans.ConnectionForm;

public class ConnectionAction extends Action {

	private static final String HOME_JSP = "template-result.jsp";

	public static final String NAME = "connection.do";

	private FormBeanFactory<ConnectionForm> formBeanFactory = FormBeanFactory
			.getInstance(ConnectionForm.class);

	public ConnectionAction(Model model) {
		super(model);
	}

	public String getName() {
		return NAME;
	}

	public String perform(HttpServletRequest request) {
		Util.i();
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);

		try {
			// login user info
			User user = (User) request.getSession().getAttribute("user");
			request.setAttribute("user", user);

			// comment form
			ConnectionForm form = formBeanFactory.create(request);
			if (!form.isPresent()) {
				return HOME_JSP;
			}
			errors.addAll(form.getValidationErrors());
			if (errors.size() != 0) {
				return HOME_JSP;
			}

			User targetUser = model.getUserDAO().read(form.getIdValue());
			if (targetUser == null) {
				errors.add("invalid user id");
				return HOME_JSP;
			}

			Connection connection = new Connection();
			connection.setFollowed(targetUser.getUserName());
			connection.setFollower(user.getUserName());

			if (form.isFollow()) {
				model.connectionDAO.createIfNotExists(connection);
			} else {
				model.connectionDAO.deleteIfExists(connection);
			}
			return Util.getString(ViewUserAction.NAME, "?userName=",
					targetUser.getUserName());
		} catch (RollbackException e) {
			errors.add(e.toString());
			Util.e(e);
			return HOME_JSP;
		} catch (FormBeanException e1) {
			errors.add(e1.toString());
			Util.e(e1);
			return HOME_JSP;
		} finally {
			if (Transaction.isActive()) {
				Transaction.rollback();
			}
		}
	}
}