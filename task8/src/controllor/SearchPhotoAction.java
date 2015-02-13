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

import org.genericdao.Transaction;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import thirdPartyAPI.Instagram;
import util.Util;
import databeans.Photo;
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

			String accessToken = (String) request.getSession().getAttribute(
					"InstagramToken");
			List<Photo> photos = Instagram.getPictureOf(accessToken, form.getKeyword());
			request.setAttribute("photos", photos);
			return SEARCH_RESULT_JSP;
		} catch (FormBeanException e) {
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