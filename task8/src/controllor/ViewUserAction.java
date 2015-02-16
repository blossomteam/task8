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
import model.PhotoDAO;

import org.genericdao.Transaction;

import util.Constants;
import util.Util;
import databeans.Photo;
import databeans.User;

public class ViewUserAction extends Action {

	private static final String HOME_JSP = "userpage.jsp";

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
			// set user info
			User user = (User) request.getSession().getAttribute("user");
			request.setAttribute("user", user);

			// get user
			String userName = request.getParameter("userName");
			if (userName == null) {
				errors.add("user name is required");
				return HOME_JSP;
			}
			User viewUser = model.getUserDAO().readByUserName(userName);
			if (viewUser == null) {
				errors.add("invalid user name");
				return HOME_JSP;
			}
			request.setAttribute("viewUser", viewUser);

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
			}

			// get photos
			PhotoDAO photoDAO = model.getPhotoDAO();
			Photo[] photos = photoDAO.getPhotosOfUser(viewUser.getId());
			Photo[] validPhotos = PhotoDAO.getTopN(
					PhotoDAO.filter(photos, 0, maxId),
					Constants.PHOTO_NUMBER_PER_PAGE);

			request.setAttribute("likes", calculateLike(photos));
			if (validPhotos == null || validPhotos.length == 0) {
				errors.add("No photo data");
				return HOME_JSP;
			}

			request.setAttribute("photos", validPhotos);
			request.setAttribute("hasPrev", validPhotos[0] != photos[0]);
			request.setAttribute("maxId", validPhotos[0]);
			request.setAttribute("minId", validPhotos[validPhotos.length - 1]);
			request.setAttribute(
					"hasNext",
					validPhotos[validPhotos.length - 1] != photos[photos.length - 1]);

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

	private int calculateLike(Photo[] photos) {
		if (photos == null) {
			return 0;
		}
		int like = 0;
		for (Photo photo : photos) {
			like += photo.getLikes();
		}
		return like;
	}
}
