package util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.google.gson.Gson;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class Http {
	public static String doPost(String url, HashMap<String, String> parameters)
			throws Exception {
		OkHttpClient client = new OkHttpClient();

		FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
		Set<String> set = parameters.keySet();
		for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
			String s = iterator.next();
			formEncodingBuilder.add(s, parameters.get(s));
		}
		RequestBody body = formEncodingBuilder.build();
		Request request = new Request.Builder().url(url).post(body).build();
		Response response = client.newCall(request).execute();
		return response.body().string();
	}

	public static final MediaType JSON = MediaType
			.parse("application/json; charset=utf-8");
	public static final MediaType FormData = MediaType
			.parse("application/x-www-form-urlencoded; charset=utf-8");
	private static OkHttpClient client = null;

	private static OkHttpClient getClient() {
		if (client == null) {
			client = new OkHttpClient();
			CookieManager cookieManager = new CookieManager();
			cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
			client.setCookieHandler(cookieManager);
		}
		return client;
	}

	public static String contentByGet(String url) {
		Util.i(url);
		Request request = new Request.Builder().url(url).build();
		Response response;
		try {
			response = getClient().newCall(request).execute();
			return response.body().string();
		} catch (IOException e) {
			Util.e(e);
		}
		return null;
	}

	public static String contentByGet(String host, Object... args) {
		String queryString;
		try {
			queryString = queryString(args);
		} catch (UnsupportedEncodingException e) {
			Util.e(e);
			return null;
		}
		String url = Util.getString(host, queryString);
		return contentByGet(url);
	}

	public static Response responseByGet(String host, Object... args) {
		String queryString;
		try {
			queryString = queryString(args);
		} catch (UnsupportedEncodingException e) {
			Util.e(e);
			return null;
		}
		String url = Util.getString(host, queryString);
		Util.i(url);
		Request request = new Request.Builder().url(url).build();
		Response response;
		try {
			response = getClient().newCall(request).execute();
			return response;
		} catch (IOException e) {
			Util.e(e);
		}
		return null;
	}

	public static <T> T contentByGet(Class<T> clazz, String host,
			Object... args) {
		String content = contentByGet(host, args);
		return new Gson().fromJson(content, clazz);
	}

	public static String contentByPost(String url, Object... args) {
		FormEncodingBuilder builder = new FormEncodingBuilder();
		for (int i = 0; i < args.length; i++) {
			if (i % 2 == 0) {
				if (i + 1 < args.length) {
					builder.add(args[i].toString(), args[i + 1].toString());
				} else {
					builder.add(args[i].toString(), "");
				}
			}
		}
		RequestBody body = builder.build();
		System.out.println("body = " + body.toString());
		Request request = new Request.Builder().url(url).post(body).build();
		int code = -1;
		String content = null;
		Response response = null;
		try {
			response = getClient().newCall(request).execute();
			code = response.code();
			content = response.body().string();
		} catch (IOException e) {
			Util.e(e);
		}
		Util.i("code = ", code);
		return content;
	}

	public static String urlEncode(String string)
			throws UnsupportedEncodingException {
		return URLEncoder.encode(string, "utf-8");
	}

	public static String queryString(Object... args)
			throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < args.length; i++) {
			if (i % 2 == 0) {
				if (i == 0) {
					sb.append("?");
				} else {
					sb.append("&");
				}
				sb.append(urlEncode(args[i].toString()));
				if (i + 1 < args.length) {
					sb.append("=");
					if (args[i + 1] != null) {
						sb.append(urlEncode(args[i + 1].toString()));
					}
				}
			}
		}
		return sb.toString();
	}

	public static String urlString(Object... args)
			throws UnsupportedEncodingException {
		if (args == null || args.length == 0) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		if (args[0].toString().contains("://")) {
			sb.append(args[0]);
		}
		sb.append("?");
		for (int i = 1; i < args.length; i++) {
			if (i % 2 == 1) {
				if (i != 0) {
					sb.append("&");
				}
				sb.append(urlEncode(args[i].toString()));
				if (i + 1 < args.length) {
					sb.append("=");
					sb.append(urlEncode(args[i + 1].toString()));
				}
			}
		}
		return sb.toString();
	}
}
