
package formbeans;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

public class CommentForm extends FormBean{
	private String comment;
	private String action;
	
	public String getComment()     { return comment; }
	public String getAction()      { return action;  }
	
	public void setComment(String s)  { comment = trimAndConvert(s,"<>\""); }
	public void setAction(String s)   { action = s;         }

	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();
		
		if (comment == null || comment.length() == 0) {
			errors.add("Comment is required");
		}
		
		if (action == null || action.length() == 0) {
			errors.add("Action is required");
		}
        
        if (!action.equals("Comment")) errors.add("Invalid action");

		return errors;
	}

}

