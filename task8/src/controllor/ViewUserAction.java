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

import org.genericdao.RollbackException;
import org.genericdao.Transaction;

import util.Constants;
import util.Util;
import databeans.Connection;
import databeans.LikeHistory;
import databeans.Photo;
import databeans.User;
import databeans.VisitHistory;

public class ViewUserAction extends Action {

	private static final String ERROR_PAGE = "template-result.jsp";

	private static final String HOME_JSP = "userpage.jsp";

	private static final String FOLLOWER_JSP = "follower.jsp";

	private static final String FOLLOWED_JSP = "followed.jsp";

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
			// login user
			User user = (User) request.getSession().getAttribute("user");
			request.setAttribute("user", user);

			// target user
			String userName = request.getParameter("userName");
			if (userName == null) {
				errors.add("user name is required");
				return ERROR_PAGE;
			}
			User viewUser = model.getUserDAO().readByUserName(userName);
			if (viewUser == null) {
				errors.add("invalid user name");
				return ERROR_PAGE;
			}
			request.setAttribute("viewUser", viewUser);
			boolean isMyself = user.getId() == viewUser.getId();

			// calculate visits
			if (!isMyself) {
				model.visitHistoryDAO.inc(viewUser.getId());
			}

			// visit history
			VisitHistory[] visitHistory = model.visitHistoryDAO
					.getWeeklyHistory(viewUser.getId());
			request.setAttribute("visitHistory", visitHistory);

			// like history
			LikeHistory[] likeHistory = model.likeHistoryDAO
					.getWeeklyHistory(viewUser.getId());
			request.setAttribute("likeHistory", likeHistory);

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

			// follow info
			Connection[] followers = model.connectionDAO.getFollowerOf(viewUser
					.getUserName());
			Connection[] followeds = model.connectionDAO.getFollowedOf(viewUser
					.getUserName());
			request.setAttribute("followers", followers);
			request.setAttribute("followeds", followeds);
			Util.i("followers.length = ", followers.length);
			if (isMyself) {
				request.setAttribute("followable", null);
			} else if (isFollowed(followers, viewUser)) {
				request.setAttribute("followable", "followed");
			} else {
				request.setAttribute("followable", "follow");
			}

			// get photos
			PhotoDAO photoDAO = model.getPhotoDAO();
			Photo[] photos = photoDAO.getPhotosOfUser(viewUser.getId());
			Photo[] validPhotos = PhotoDAO.getTopN(
					PhotoDAO.filter(photos, 0, maxId),
					Constants.PHOTO_NUMBER_PER_PAGE);

			request.setAttribute("likes", calculateLike(photos));

			String action = request.getParameter("action");
			if ("follower".equals(action)) {
				return FOLLOWER_JSP;
			} else if ("followed".equals(action)) {
				return FOLLOWED_JSP;
			} else {
				if (validPhotos == null || validPhotos.length == 0) {
					return HOME_JSP;
				}
				request.setAttribute("photos", validPhotos);
				request.setAttribute("hasPrev", validPhotos[0] != photos[0]);
				request.setAttribute("maxId", validPhotos[0]);
				request.setAttribute("minId",
						validPhotos[validPhotos.length - 1]);
				request.setAttribute(
						"hasNext",
						validPhotos[validPhotos.length - 1] != photos[photos.length - 1]);
				return HOME_JSP;
			}
		} catch (RollbackException e) {
			errors.add(e.toString());
			Util.e(e);
			return ERROR_PAGE;
		} finally {
			if (Transaction.isActive()) {
				Transaction.rollback();
			}
		}
	}

	private boolean isFollowed(Connection[] followers, User myself) {
		for (Connection follower : followers) {
			if (myself.getUserName().equals(follower.getFollowed())) {
				return true;
			}
		}
		return false;
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
