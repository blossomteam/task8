package controllor;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import model.Model;
import model.PhotoDAO;

import org.genericdao.RollbackException;
import org.genericdao.Transaction;

import util.Util;
import databeans.Connection;
import databeans.Photo;
import databeans.User;

public class HomeAction extends Action {

	private static final String HOME_JSP = "home.jsp";

	public static final String NAME = "home.do";

	PhotoDAO photoDAO;

	public HomeAction(Model model) {
		super(model);
		photoDAO = model.getPhotoDAO();
	}

	public String getName() {
		return NAME;
	}

	public String perform(HttpServletRequest request) {
		Util.i();
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);
		try {
			User user = (User) request.getSession().getAttribute("user");
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
			ArrayList<Integer> followedIds = getFollowedIdsOf(user);
			Photo[] photos = photoDAO.getNewPhotos(followedIds, maxId);
			if (photos == null || photos.length == 0) {
				errors.add("No photo data");
				return HOME_JSP;
			}
			request.setAttribute("photos", photos);
			request.setAttribute("maxId", photos[0]);
			request.setAttribute("minId", photos[photos.length - 1]);
			request.setAttribute("hasPrev",
					photos[0].getId() < photoDAO.getMaxId());
			request.setAttribute("hasNext",
					photos[photos.length - 1].getId() > 1);
			return HOME_JSP;
		} catch (Exception e) {
			return HOME_JSP;
		} finally {
			if (Transaction.isActive()) {
				Transaction.rollback();
			}
		}

	}

	private ArrayList<Integer> getFollowedIdsOf(User user)
			throws RollbackException {
		ArrayList<Integer> followedIds = new ArrayList<>();
		Connection[] followeds = model.connectionDAO.getFollowedOf(user
				.getUserName());
		for (Connection connection : followeds) {
			User followed = model.getUserDAO().readByUserName(
					connection.getFollowed());
			if (followed != null) {
				Util.i("add ", connection.getFollowed());
				followedIds.add(followed.getId());
			}
		}
		return followedIds;
	}
}
