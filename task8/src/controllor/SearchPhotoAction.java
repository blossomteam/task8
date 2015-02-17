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
import model.UserDAO;

import org.genericdao.RollbackException;
import org.genericdao.Transaction;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import util.Constants;
import util.Util;
import databeans.Photo;
import databeans.User;
import formbeans.SearchForm;

public class SearchPhotoAction extends Action {

	private static final String SEARCH_RESULT_JSP = "search.jsp";

	public static final String NAME = "search-photo.do";

	private FormBeanFactory<SearchForm> formBeanFactory = FormBeanFactory
			.getInstance(SearchForm.class);

	UserDAO userDao;

	public SearchPhotoAction(Model model) {
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

		try {
			User user = (User) request.getSession().getAttribute("user");
			request.setAttribute("user", user);
			
			SearchForm form = formBeanFactory.create(request);
			request.setAttribute("form", form);
			Util.i(form);
			if (!form.isPresent()) {
				return SEARCH_RESULT_JSP;
			}
			errors.addAll(form.getValidationErrors());
			if (errors.size() != 0) {
				return SEARCH_RESULT_JSP;
			}

			PhotoDAO photoDAO = model.getPhotoDAO();
			Photo[] photos = photoDAO.getPhotosOf(form.getKeyword());
			Photo[] validPhotos = PhotoDAO.getTopN(
					PhotoDAO.filter(photos, 0, form.getMaxIdValue()),
					Constants.PHOTO_NUMBER_PER_PAGE);

			Util.i("photos.length = ", photos.length,
					", validPhotos.length = ", validPhotos.length);
			if (validPhotos == null || validPhotos.length == 0) {
				errors.add("No photo data");
				return SEARCH_RESULT_JSP;
			}

			request.setAttribute("photos", validPhotos);
			request.setAttribute("hasPrev", validPhotos[0] != photos[0]);
			request.setAttribute("maxId", validPhotos[0]);
			request.setAttribute("minId", validPhotos[validPhotos.length - 1]);
			request.setAttribute(
					"hasNext",
					validPhotos[validPhotos.length - 1] != photos[photos.length - 1]);

			return SEARCH_RESULT_JSP;
		} catch (FormBeanException e) {
			Util.e(e);
			errors.add(e.getMessage());
			return SEARCH_RESULT_JSP;
		} catch (RollbackException e) {
			Util.e(e);
			errors.add(e.getMessage());
			return SEARCH_RESULT_JSP;
		} finally {
			if (Transaction.isActive()) {
				Transaction.rollback();
			}
		}
	}
}
