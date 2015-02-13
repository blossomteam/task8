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

import org.genericdao.Transaction;

import util.Util;
import databeans.User;

public class ViewUserAction extends Action {

	private static final String HOME_JSP = "template-result.jsp";

	public static final String NAME = "view-user.do";

	public ViewUserAction(Model model) {
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
			String userName = request.getParameter("user_name");
			if (userName == null) {
				errors.add("user name is required");
				return HOME_JSP;
			}

			User user = model.getUserDAO().readByUserName(userName);
			if (user == null) {
				errors.add("invalid user name");
				return HOME_JSP;
			}

			request.setAttribute("message", "to be impletemented");
			request.setAttribute("user", user);
			return HOME_JSP;
		} catch (Exception e) {
			errors.add(e.toString());
			Util.e(e);
			return HOME_JSP;
		} finally {
			if (Transaction.isActive()) {
				Transaction.rollback();
			}
		}

	}
}
