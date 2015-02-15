package controllor;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import model.Model;
import model.PhotoDAO;

import org.genericdao.Transaction;

import util.Util;
import databeans.Photo;

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
			Photo[] photos = photoDAO.getNewPhotos(maxId);
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
}
