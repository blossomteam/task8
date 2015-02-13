

package controllor;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import model.Model;
import model.PhotoDAO;

import org.genericdao.RollbackException;
import org.genericdao.Transaction;

import databeans.Photo;
import util.Util;

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

	public String perform(HttpServletRequest request)  {
		Util.i();
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);
		try {
			Photo[] photos = photoDAO.getNewPhotos();
			if(photos == null || photos.length == 0) {
				errors.add("No photo data");
				return HOME_JSP;
			}
			request.setAttribute("photos", photos);
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
