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
import databeans.Photo;
import databeans.User;

public class ViewUserAction extends Action {

	private static final String HOME_JSP = "view-user.jsp";

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
			// get user
			String userName = request.getParameter("userName");
			if (userName == null) {
				errors.add("user name is required");
				return HOME_JSP;
			}
			User user = model.getUserDAO().readByUserName(userName);
			if (user == null) {
				errors.add("invalid user name");
				return HOME_JSP;
			}
			request.setAttribute("user", user);

			// get maxId
			String maxIdString = request.getParameter("maxId");
			int maxId = Integer.MAX_VALUE;
			try {
				if (maxIdString != null) {
					maxId = Integer.valueOf(maxIdString);
				}
			} catch (NumberFormatException e) {
				Util.e(e);
				errors.add("invalid maxId");
				return HOME_JSP;
			}

			// get photos
			Photo[] photos = model.getPhotoDAO().getPhotosOfUser(user.getId(),
					maxId);
			if (photos == null || photos.length == 0) {
				errors.add("No photo data");
				return HOME_JSP;
			}
			request.setAttribute("photos", photos);
			request.setAttribute("maxId", photos[0]);
			request.setAttribute("minId", photos[photos.length - 1]);

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
