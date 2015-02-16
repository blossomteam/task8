
package controllor;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import model.CommentDAO;
import model.Model;
import model.PhotoDAO;

import org.genericdao.RollbackException;
import org.genericdao.Transaction;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import util.Util;
import databeans.Comment;
import databeans.Photo;
import databeans.User;
import formbeans.CommentForm;

public class CommentAction extends Action {

	public static final String COMMENT_NAME = "comment.do";

	private static final String COMMENT_JSP = "view-photo.jsp";

	private FormBeanFactory<CommentForm> formBeanFactory = FormBeanFactory
			.getInstance(CommentForm.class);
	
	private CommentDAO commentDAO;

	public CommentAction(Model model) {
		super(model);
		commentDAO = model.getCommentDAO();
	}

	public String getName() {
		return COMMENT_NAME;
	}

	public String perform(HttpServletRequest request) {
		Util.i();
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);

		try {
			User user = (User) request.getSession(false).getAttribute("user");
			
			Photo photo = (Photo) request.getSession(false).getAttribute("photo");
			
			Comment[] commentList = commentDAO.read(photo.getId());
			request.setAttribute("comList", commentList);
			
			CommentForm form = formBeanFactory.create(request);
			request.setAttribute("form", form);
			
			if (!form.isPresent()) {
				return COMMENT_JSP;
			}

			errors.addAll(form.getValidationErrors());
			if (errors.size() != 0) {
				for (String string : errors) {
					Util.e(string);
				}
				return COMMENT_JSP;
			}

			
			Comment com = new Comment();
	        com.setUserid(user.getId());
	        com.setPhotoid(photo.getId());
	        com.setComment(form.getComment());
			
	        if (form.getAction().equals("Comment")) {
        		commentDAO.create(com);
        	} else {
        		errors.add("Invalid action: " + form.getAction());
        	}
	  
	        request.setAttribute("commentList", commentDAO.read(photo.getId()));
			
			return COMMENT_NAME;
		} catch (RollbackException e) {
			errors.add(e.getMessage());
			Util.i(e);
			return COMMENT_JSP;
		} catch (FormBeanException e) {
			errors.add(e.getMessage());
			Util.i(e);
			return COMMENT_JSP;
		} finally {
			if (Transaction.isActive()) {
				Transaction.rollback();
			}
		}
	}
}
