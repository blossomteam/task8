package util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class Http {
	public static String doPost(String url, HashMap<String, String> parameters) throws Exception {
		OkHttpClient client = new OkHttpClient();
		
		FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
		Set<String> set = parameters.keySet();
		for(Iterator<String> iterator = set.iterator(); iterator.hasNext();){
			String s =iterator.next();
			formEncodingBuilder.add(s, parameters.get(s));
		}
		RequestBody body = formEncodingBuilder.build();
		Request request = new Request.Builder().url(url).post(body).build();
		Response response = client.newCall(request).execute();
		return response.body().string();		
	}

}
