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

public class ViewPhotoAction extends Action {

	private static final String VIEW_PHOTO_JSP = "view-photo.jsp";

	public static final String NAME = "view-photo.do";

	public ViewPhotoAction(Model model) {
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
			String idString = request.getParameter("user_name");
			if (Util.isEmpty(idString)) {
				errors.add("id is required");
				return VIEW_PHOTO_JSP;
			}

			int id = 0;
			try {
				id = Integer.valueOf(idString);
			} catch (NumberFormatException e) {
				Util.i(e);
			}

			Photo photo = model.getPhotoDAO().read(id);
			if (photo == null) {
				errors.add("invalid photo id");
				return VIEW_PHOTO_JSP;
			}

			User user = model.getUserDAO().read(photo.getUserId());
			request.setAttribute("photo", photo);
			request.setAttribute("user", user);
			return VIEW_PHOTO_JSP;
		} catch (Exception e) {
			errors.add(e.toString());
			Util.e(e);
			return VIEW_PHOTO_JSP;
		} finally {
			if (Transaction.isActive()) {
				Transaction.rollback();
			}
		}

	}
}
