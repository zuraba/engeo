package ge.android.engeo;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

public class GetTranslateGE {
	static String result;
	static JSONArray jArray;

	public static JSONArray getFromTranslateGe(String url) {
		final HttpClient client = new DefaultHttpClient();
		final HttpGet httpGet = new HttpGet(url);

		try {
			HttpResponse response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			jArray = new JSONArray(result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jArray;
	}
}
