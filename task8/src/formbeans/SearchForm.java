/**
 * 08-600 
 * hw#9
 * Jian Chen 
 * jianc1
 * Dec 06, 2014
 */

package formbeans;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

import com.google.gson.Gson;

public class SearchForm extends FormBean {
	private String keyword;

	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();

		if (keyword == null || keyword.length() == 0)
			errors.add("Keyword is required");

		if (errors.size() > 0)
			return errors;

		if (keyword.matches(".*[<>\"].*"))
			errors.add("keyword may not contain angle brackets or quotes");

		return errors;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String userName) {
		this.keyword = userName.trim();
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
