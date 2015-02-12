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
import model.UserDAO;

import org.mybeans.form.FormBeanFactory;

import util.Util;
import formbeans.LoginForm;

public class HomeAction extends Action {

	private static final String HOME_JSP = "home.jsp";

	public static final String NAME = "home.do";

	private FormBeanFactory<LoginForm> formBeanFactory = FormBeanFactory
			.getInstance(LoginForm.class);

	
	UserDAO userDao;

	public HomeAction(Model model) {
		super(model);
		userDao = model.getUserDAO();
	}

	public String getName() {
		return NAME;
	}

	public String perform(HttpServletRequest request) {
		Util.i();
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);

		return HOME_JSP;
	}
}
