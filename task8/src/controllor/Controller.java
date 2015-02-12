/**
 * 08-600 
 * hw#9
 * Jian Chen 
 * jianc1
 * Dec 06, 2014
 */

package controllor;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Model;
import util.Util;

@SuppressWarnings("serial")
public class Controller extends HttpServlet {
	private Model model;

	public void init() throws ServletException {
		model = new Model(getServletConfig());

		// try {
		// if (model.getEmployeeDAO().getCount() == 0) {
		// createDefaultAccount();
		// }
		// } catch (RollbackException e) {
		// e.printStackTrace();
		// }

		Action.add(new RegisterAction(model));
		Action.add(new LoginAction(model));
		Action.add(new InstagramLoginAction(model));
		Action.add(new TwitterLoginAction(model));
		Action.add(new InstagramLoginCallbackAction(model));
		Action.add(new TwitterLoginCallbackAction(model));
		Action.add(new LogoutAction(model));
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String servletPath = request.getServletPath();
		String action = getActionName(servletPath);
		Util.i("action = ", action);
		String nextPage = null;
		nextPage = performCommonAction(request);
		sendToNextPage(nextPage, request, response);
	}

	private String performCommonAction(HttpServletRequest request) {
		String servletPath = request.getServletPath();
		String action = getActionName(servletPath);
		return Action.perform(action, request);
	}

	private void sendToNextPage(String nextPage, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		if (nextPage == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND,
					request.getServletPath());
			return;
		}

		Util.i("nextPage = ", nextPage);
		if (nextPage.indexOf("://") != -1) {
			response.sendRedirect(nextPage);
			return;
		}

		if (nextPage.endsWith(".do")) {
			response.sendRedirect(nextPage);
			return;
		}

		if (nextPage.endsWith(".jsp")) {
			RequestDispatcher d = request.getRequestDispatcher("WEB-INF/"
					+ nextPage);
			d.forward(request, response);
			return;
		}

		if (nextPage.equals("redirect")) {
			response.sendRedirect((String) request.getAttribute("url"));
			return;
		}

		throw new ServletException(Controller.class.getName()
				+ ".sendToNextPage(\"" + nextPage + "\"): invalid extension.");
	}

	/*
	 * Returns the path component after the last slash removing any "extension"
	 * if present.
	 */
	private String getActionName(String path) {
		// We're guaranteed that the path will start with a slash
		int slash = path.lastIndexOf('/');
		return path.substring(slash + 1);
	}

}
